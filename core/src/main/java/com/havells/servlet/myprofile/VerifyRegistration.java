package com.havells.servlet.myprofile;

import com.adobe.granite.crypto.CryptoException;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.security.AccountManager;
import com.day.cq.security.AccountManagerFactory;
import com.havells.services.GenericService;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.jackrabbit.JcrConstants;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.util.Base64;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.granite.crypto.CryptoSupport;

import javax.jcr.*;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.query.Query;
import javax.jcr.version.VersionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang.StringUtils.split;

@Component(immediate = true, metatype = false, label = "VerifyRegistration")
@Service
@Properties(value = {
        @Property(name = "sling.servlet.methods", value = "GET"),
        @Property(name = "sling.servlet.paths", value = "/bin/servlets/VerifyRegistrationServlet")
})
/**
 * This class will build the profile of the user in /home/user/...
 *
 * Each user is verified by the email he provide. As the URL, send to his mail id,
 * is clicked by him, he is verified and his profile is build in the /home/user...
 * He is also being verified by the token sent in his mail.
 */
public class VerifyRegistration extends SlingSafeMethodsServlet {

    @Property(unbounded= PropertyUnbounded.DEFAULT,description="User Group")
    private static final String USER_GROUP = "newUserGroup";

    protected static final Logger LOG = LoggerFactory.getLogger(VerifyRegistration.class);
    public final String MESSAGE = "message";
    public String userGroup;

    @Reference
    CryptoSupport cryptoSupport;

    @Reference
    AccountManagerFactory af;

    @Reference
    GenericService genericService;

    @Activate
    protected void activate(final Map<String, Object> config) {
        this.userGroup = genericService.getProps("org.havells.usergroup", USER_GROUP);
    }

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws
            IOException {
        String pwd = null;
        String userEmailId;
        ResourceResolver resourceResolver1 = genericService.getAdminResourceResolver();
        JSONObject jsonObject = new JSONObject();

        Session session = resourceResolver1.adaptTo(Session.class);
        //c stand for token & l for path
        String token = request.getParameter("c");
        String path = request.getParameter("l");

        String intermediatePath = request.getParameter("intermediatePath");
        String tokenExpiryDate;
        try {
            path = Base64.decode(path);
            if (StringUtils.isNotBlank(token) && !(token.equals("null"))) {
                token = Base64.decode(token);
                boolean userExistanceStatus = false;
                String[] str = split(token, '$');
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                if (str!= null && str.length == 2) {
                    userEmailId = str[0];
                    tokenExpiryDate = str[1];
                    
                    Date currentDate = new Date();
                    Date tokenExpiry = formatter.parse(tokenExpiryDate);
                    if (tokenExpiry.compareTo(currentDate) > 0) {
                        
                        if (StringUtils.isNotBlank(path) && !(path.equals("null"))) {
                            
                            Resource profileResource = resourceResolver1.getResource(path);
                            if (profileResource != null) {
                                ValueMap profileProperties = profileResource.adaptTo(ValueMap.class);
                                pwd = cryptoSupport.unprotect(profileProperties.get("passwordConfirm", ""));
                                userExistanceStatus = createUserAccount(resourceResolver1, profileResource.adaptTo(Node.class), intermediatePath, pwd, userGroup, userEmailId);
                            }
                            
                        } else {
                            String searchQuery = "SELECT * FROM [nt:unstructured] AS s WHERE ISDESCENDANTNODE([/var/user]) and CONTAINS(s.email, '" + userEmailId + "') ORDER BY s.creationTime desc ";
                            Iterator<Resource> result = resourceResolver1.findResources(searchQuery, Query.JCR_SQL2);

                            if (result.hasNext()) {
                                Resource resource = result.next();
                                ValueMap nodeProperties = resource.adaptTo(ValueMap.class);
                                if (StringUtils.isNotBlank(nodeProperties.get("passwordConfirm", ""))) {
                                    pwd = cryptoSupport.unprotect(nodeProperties.get("passwordConfirm", ""));
                                }
                                userExistanceStatus = createUserAccount(resourceResolver1, resource.adaptTo(Node.class), intermediatePath, pwd, userGroup, userEmailId);
                            }
                        }
                        if (userExistanceStatus) {
                            jsonObject.put(MESSAGE, 1);   // user Created
                        } else {
                            jsonObject.put(MESSAGE, 2);   //   user Exist
                        }
                    } else {
                        jsonObject.put(MESSAGE, 3);   // token Expired
                    }
                } else {
                    jsonObject.put(MESSAGE, 4);    // token not exist or invalid
                }
                
            } else {
                jsonObject.put(MESSAGE, 4);    // token not exist or invalid
            }
            response.getWriter().write(String.valueOf(jsonObject));
        } catch (RepositoryException repositoryException) {
            LOG.error("repository Exception While Verifying user email Id. ", repositoryException);
        } catch (JSONException jSONException) {
            LOG.error("JSON Exception when sending data to jsp While Verifying user email Id. ", jSONException);
        }  catch (ParseException parseException) {
            LOG.error("JSON Exception when parsing date While Verifying user email Id. ", parseException);
        } catch (CryptoException cryptoException) {
            LOG.error("Crypto Exception when user password is decoded. ", cryptoException);
        } finally {
            if (session != null && session.isLive()) {
                session.logout();
            }
        }
    }

    /**
     * This method will create a user in /home/user/... after it has been authenticated by the user himself.
     *
     * @prams ::
     * @param resourceResolver1 : Helps to get session or resources used to build the profile of user
     * @param node              : brings the details of the user stored in /var/user/... and which are to be stored in the
     *                          profile of the user.
     * @param intermediatePath  : path after /home/user provided by the author to create user there.
     * @param pwd               : sets the password provided by the user
     * @param group             : sets the value of a group to the node to which the user belongs
     * @param userEmailId       : builds the profile with this email.
     *
     * @return a boolean which tells wheather a user is created or not
     */
    private boolean createUserAccount(ResourceResolver resourceResolver1, Node node, String intermediatePath, String pwd, String group, String userEmailId) throws AccessDeniedException {

        try {
            Session session = resourceResolver1.adaptTo(Session.class);
            final AccountManager am = af.createAccountManager(session);
            UserManager manager = resourceResolver1.adaptTo(UserManager.class);
            Authorizable authorizable = manager.getAuthorizable(userEmailId);
            if (authorizable == null) {
                Map<String, RequestParameter[]> userProps = filterParameter(node);
                if (intermediatePath != null && (!intermediatePath.equals("null") || !intermediatePath.equals(""))) {
                    intermediatePath += "/" + userEmailId.substring(0, 1);
                    userProps.put("rep:intermediatePath", new RequestParameter[]{new IntermediatePathParam(intermediatePath)});
                }
                am.getOrCreateAccount(userEmailId, pwd, group, userProps);
                addDefaultProfile(resourceResolver1, intermediatePath, userEmailId);
                return true;
            } else {
                return false;
            }
        } catch (RepositoryException e) {
            LOG.error("Repository Exception When a user profile is created in /home/user :: " + e);
        }
        return false;
    }

    /**
     * This method filters the property, which are provided by user or not provided,
     * and fetch those value from a node in /var/user... and sets them to a map
     * which will pass as a parameter so as to create a user.
     *
     * @params ::
     * @param node  : from which all the properties are stored in the profile of the user.
     *
     * @return Map : in which all the details, provided by user, are stored and
     *         these values are to be set in profile.
     */
    private Map<String, RequestParameter[]> filterParameter(Node node) {
        Map<String, RequestParameter[]> prefs = new HashMap<String, RequestParameter[]>();
        String value;
        try {
            if (node.hasProperty("email")) {
                value = node.getProperty("email").getString();
                prefs.put("email", new RequestParameter[]{new IntermediatePathParam(value)});
            }
            if (node.hasProperty("givenName")) {
                value = node.getProperty("givenName").getString();
                prefs.put("givenName", new RequestParameter[]{new IntermediatePathParam(value)});
            }
            if (node.hasProperty("familyName")) {
                value = node.getProperty("familyName").getString();
                prefs.put("familyName", new RequestParameter[]{new IntermediatePathParam(value)});
            }
            if (node.hasProperty("mobile")) {
                value = node.getProperty("mobile").getString();
                prefs.put("mobile", new RequestParameter[]{new IntermediatePathParam(value)});
            }
            if (node.hasProperty("dateOfBirth")) {
                value = node.getProperty("dateOfBirth").getString();
                prefs.put("dateOfBirth", new RequestParameter[]{new IntermediatePathParam(value)});
            }
            if (node.hasProperty("billingAddress")) {
                value = node.getProperty("billingAddress").getString();
                prefs.put("billingAddress", new RequestParameter[]{new IntermediatePathParam(value)});
            }
            if (node.hasProperty("shippingAddress")) {
                value = node.getProperty("shippingAddress").getString();
                prefs.put("shippingAddress", new RequestParameter[]{new IntermediatePathParam(value)});
            }
        } catch (RepositoryException e) {
            LOG.error("Repository Exception when user profile is created. And the profile data from a node is stored in map to create user. ", e);
        }
        return prefs;
    }

    /**
     * This method will add a default image as profile picture to the user profile.
     *
     * @params ::
     * @param resourceResolver : Helps to get session or resources used store image.
     * @param intermediatePath : gives the intermediate path after /home/user...
     * @param userEmailId      : specifies the userId, under which the default image is to be placed.
     */
    private void addDefaultProfile(ResourceResolver resourceResolver, String intermediatePath, String userEmailId) {

        try {
            Session mySession = resourceResolver.adaptTo(Session.class);
            String path = "/home/users/" + intermediatePath + "/" + userEmailId + "/profile/photos/primary";
            Node root = JcrUtil.createPath(path, "nt:folder", "nt:folder", mySession, true);

            Node myNewNode = root.addNode("image", "nt:file");
            Node contentNode = myNewNode.addNode("jcr:content", "nt:resource");
            Resource resource = resourceResolver.getResource("/content/dam/havells/profile.jpg/jcr:content/renditions/cq5dam.thumbnail.140.100.png/jcr:content");
            if (resource != null) {
                javax.jcr.Node jcrDataNode = resource.adaptTo(javax.jcr.Node.class);
                Binary binaryProp = jcrDataNode.getProperty(JcrConstants.JCR_DATA).getValue().getBinary();
                InputStream inputStream = binaryProp.getStream();
                contentNode.setProperty(JcrConstants.JCR_LASTMODIFIED, Calendar.getInstance());
                contentNode.setProperty(JcrConstants.JCR_MIMETYPE, "image/jpeg");
                contentNode.setProperty(JcrConstants.JCR_DATA, inputStream);
                mySession.save();
            }
        } catch (ItemExistsException itemExistsException) {
            LOG.error("Item Exists Exception  While placing default image to the profile ", itemExistsException);
        } catch (AccessDeniedException accessDeniedException) {
            LOG.error("Access Denied Exception  While placing default image to the profile ", accessDeniedException);
        } catch (InvalidItemStateException invalidItemStateException) {
            LOG.error("Invalid Items State Exception  While placing default image to the profile ", invalidItemStateException);
        } catch (NoSuchNodeTypeException noSuchNodeTypeException) {
            LOG.error("No Such Node Type Exception  While placing default image to the profile ", noSuchNodeTypeException);
        } catch (VersionException versionException) {
            LOG.error("Version  Exception  While placing default image to the profile ", versionException);
        } catch (ConstraintViolationException constraintViolationException) {
            LOG.info("Constraint Voilation Exception  While placing default image to the profile ", constraintViolationException);
        } catch (LockException lockException) {
            LOG.error("Lock Exception  While placing default image to the profile ", lockException);
        } catch (ReferentialIntegrityException referentialIntegrityException) {
            LOG.error("Refrential Integrity Exception While placing default image to the profile ", referentialIntegrityException);
        } catch (RepositoryException repositoryException) {
            LOG.error("repository Exception While placing default image to the profile ", repositoryException);
        }
    }

    final class IntermediatePathParam implements RequestParameter {

        private final String intermediatePath;

        private IntermediatePathParam(String intermediatePath) {
            this.intermediatePath = intermediatePath;
        }

        public boolean isFormField() {
            return true;
        }

        public String getContentType() {
            return null;
        }

        public long getSize() {
            return intermediatePath.length();
        }

        public byte[] get() {
            return intermediatePath.getBytes();
        }

        public InputStream getInputStream() throws IOException {
            return null;
        }

        public String getFileName() {
            return null;
        }

        public String getName() {
            return getString();
        }

        public String getString() {
            return intermediatePath;
        }

        public String getString(String s) throws UnsupportedEncodingException {
            return new String(intermediatePath.getBytes(s));
        }
    }

}


