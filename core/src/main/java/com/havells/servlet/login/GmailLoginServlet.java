package com.havells.servlet.login;

import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.security.AccountManager;
import com.day.cq.security.AccountManagerFactory;
import com.havells.services.GenericService;
import org.apache.felix.scr.annotations.*;
import org.apache.felix.scr.annotations.Property;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.util.Base64;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Component(immediate = true, metatype = false, label = "GmailLoginServlet")
@Service
@Properties(value = {
        @Property(name = "sling.servlet.methods", value = "POST"),
        @Property(name = "sling.servlet.paths", value = "/bin/servlets/gmailLoginServlet")
})
public class GmailLoginServlet extends SlingAllMethodsServlet {

    private final Logger log = LoggerFactory.getLogger(GmailLoginServlet.class);
    private static final String PASSWORD_KEY = "unotallowed$havells-default@123";
    private static final String PARENT_NODE_NAME = "gmail";

    @Reference
    private AccountManagerFactory accountManagerFactory;

    @Reference
    private GenericService genericService;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws
            IOException {
        ResourceResolver resourceResolver = null;
        try {
            resourceResolver = genericService.getAdminResourceResolver();
            if (resourceResolver != null) {
                String jsonString = request.getParameter("json");
                JSONObject userInfoObject = new JSONObject(jsonString);
                String userGroup = request.getParameter("userGroup");
                String loginID = userInfoObject.get("id").toString();
                final Session session = resourceResolver.adaptTo(Session.class);
                AccountManager accountManager = accountManagerFactory.createAccountManager(session);
                Map<String, RequestParameter[]> userProps = filterParameter(userInfoObject);
                accountManager.getOrCreateAccount(PARENT_NODE_NAME + "-" + loginID, (Base64.encode(PASSWORD_KEY + loginID)), userGroup, userProps);
                addDefaultProfile(session, PARENT_NODE_NAME + "-" + loginID, userInfoObject.getJSONObject("image").get("url").toString());

                response.setStatus(SlingHttpServletResponse.SC_OK);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("token", (Base64.encode(PASSWORD_KEY + loginID)));
                response.getWriter().write(jsonObject.toString());
            }
        } catch (AccessDeniedException exc) {
            log.error("Access Denied." + exc);
            response.setStatus(SlingHttpServletResponse.SC_FORBIDDEN);
        } catch (RepositoryException repoExc) {
            log.error("Resource not found." + repoExc);
            response.setStatus(SlingHttpServletResponse.SC_EXPECTATION_FAILED);
        } catch (JSONException jsonExc) {
            log.error("Json Exception." + jsonExc);
            response.setStatus(SlingHttpServletResponse.SC_EXPECTATION_FAILED);
        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
    }


    private Map<String, RequestParameter[]> filterParameter(JSONObject obj) throws JSONException {

        Map<String, RequestParameter[]> props = new HashMap<String, RequestParameter[]>();
        String familyName = obj.getJSONObject("name").get("familyName").toString();
        String givenName = obj.getJSONObject("name").get("givenName").toString();
        String email = obj.getJSONArray("emails").getJSONObject(0).getString("value");
        props.put("rep:intermediatePath", new RequestParameter[]{new Parameters(PARENT_NODE_NAME)});
        props.put("familyName", new RequestParameter[]{new Parameters(familyName)});
        props.put("givenName", new RequestParameter[]{new Parameters(givenName)});
        props.put("email", new RequestParameter[]{new Parameters(email)});
        return props;
    }


    final class Parameters implements RequestParameter {

        private final String parameter;

        private Parameters(String parameter) {
            this.parameter = parameter;
        }

        public boolean isFormField() {
            return true;
        }

        public String getContentType() {
            return null;
        }

        public long getSize() {
            return parameter.length();
        }

        public byte[] get() {
            return parameter.getBytes();
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
            return parameter;
        }

        public String getString(String s) throws UnsupportedEncodingException {
            return new String(parameter.getBytes(s));
        }
    }

    /**
     * This method will add a default image from gmail account as profile picture to the user profile.
     *
     * @param session : Helps to get session to save image.
     * @param userNodeName      : specifies the userId node, under which the default image is to be placed.
     * @param imageUrl         : url from where image needs to be picked.
     */
    private void addDefaultProfile(Session session, String userNodeName, String imageUrl) {
        try {
            String path = "/home/users/" + PARENT_NODE_NAME + "/" + userNodeName + "/profile/photos/primary";
            Node root = JcrUtil.createPath(path, "nt:folder", "nt:folder", session, true);
            if (imageUrl != null  && root != null) {
                URL url = new URL(imageUrl);
                JcrUtils.putFile(root, "image", "image/jpeg", new BufferedInputStream(url.openStream()));
                session.save();
            }
        } catch (ItemExistsException itemExistsException) {
            log.error("Item Exists Exception  While placing default image to the profile ", itemExistsException);
        } catch (InvalidItemStateException invalidItemStateException) {
            log.error("Invalid Items State Exception  While placing default image to the profile ", invalidItemStateException);
        } catch (RepositoryException repositoryException) {
            log.error("repository Exception While placing default image to the profile ", repositoryException);
        } catch (IOException iOException){
            log.error("IO Exception while fetching image", iOException);
        }
    }
}
