package com.havells.servlet.myprofile;

import com.havells.services.GenericService;
import com.havells.services.SendEmailService;
import org.apache.commons.mail.EmailException;
import org.apache.felix.scr.annotations.*;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.util.Base64;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.*;
import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Component(immediate = true, metatype = true, label = "ForgotPasswordServlet")
@Service
@Properties(value = {
        @Property(name = "sling.servlet.methods", value = "POST", propertyPrivate = true),
        @Property(name = "sling.servlet.paths", value = "/bin/servlets/ForgotPasswordServlet", propertyPrivate = true)
})
public class ForgotPasswordServlet extends SlingAllMethodsServlet {

    public static final String PROFILE_TOKEN = "profile/token";
    @Property(unbounded = PropertyUnbounded.DEFAULT, label = "hostname", description = "this is hostname")
    private static final String HOST_NAME = "host.name";
    @Property(unbounded = PropertyUnbounded.DEFAULT, label = "templatepath", description = "Path of a template")
    private static final String TEMPLATE_PATH = "template.path";
    @Property(unbounded = PropertyUnbounded.DEFAULT, label = "reset_password_path", description = "Reset Password Page Location, For ex: /content/geometrixx/en")
    private static final String RESET_PATH = "reset.path";

    private String hostName;
    private String templatePath;
    private String resetpath;

    @Activate
    private void activate(final ComponentContext componentContext) {
        final Dictionary<?, ?> props = componentContext.getProperties();
        this.hostName = (String) props.get(HOST_NAME);
        this.templatePath = (String) props.get(TEMPLATE_PATH);
        this.resetpath = (String) props.get(RESET_PATH);
    }

    @Reference
    private SendEmailService sendEmailService;

    @Reference
    GenericService genericService;

    protected static final Logger LOG = LoggerFactory.getLogger(ForgotPasswordServlet.class);

    /**
     * This function is called when the request to reset his password. A mail is sent to his mailid in which
     * which will take him to the password reset page, from where user can change his password.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws
            IOException {

        ResourceResolver resourceResolver = genericService.getAdminResourceResolver();
        Session session = resourceResolver.adaptTo(Session.class);
        int status = SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR;

        try {
            if (resourceResolver != null) {
                String userid = request.getParameter("user-id");
                String subject = request.getParameter("subject");
                setUserToken(userid, resourceResolver, session);
                Map<String, String> userDetails = getUserDetails(userid, resourceResolver);
                String formattedName = userDetails.get("formattedName");
                String userEmail = userDetails.get("email");
                String token = userDetails.get("token");

                Map<String, String> map = getParameterMap(token, formattedName);
                response.setContentType("text/plain");
                if (!formattedName.isEmpty()) {
                    sendEmailService.sendEmail(userEmail, session, subject, map, templatePath);
                    session.save();
                    status = SlingHttpServletResponse.SC_OK;
                } else {
                    status = SlingHttpServletResponse.SC_FORBIDDEN;
                }
            } else {
                LOG.error("Resource resolver is null.");
            }
        } catch (EmailException ex) {
            LOG.error("Email Error." + ex);
        } catch (MessagingException exc) {
            LOG.error("Messaging Error." + exc);
        } catch (UnsupportedOperationException ue) {
            LOG.error("Operation Not Supported. " + ue);
        } catch (RepositoryException repositoryException) {
            LOG.error("Repository Exception " + repositoryException);
        } finally {
            response.setStatus(status);
            if (session != null){
                session.logout();
            }
            if(resourceResolver != null && resourceResolver.isLive()){
                resourceResolver.close();
            }
        }
    }

    /**
     * This method sets a token and the expiry time of the token(which is one day later) to the mailid send as parameter.
     *
     * @param userEmailId
     * @param resourceResolver
     * @param session
     */
    private void setUserToken(String userEmailId, ResourceResolver resourceResolver, Session session) {
        UserManager manager = resourceResolver.adaptTo(UserManager.class);
        String token = Base64.encode(userEmailId + "$" + new Date());
        Date currentTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentTime);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Date tokenExpiryTime = cal.getTime();

        try {
            Authorizable auth = manager.getAuthorizable(userEmailId);
            if (auth != null) {
                if (auth.hasProperty("profile/tokenExpiryTime")) {
                    auth.removeProperty("profile/tokenExpiryTime");
                }
                if (auth.hasProperty(PROFILE_TOKEN)) {
                    auth.removeProperty(PROFILE_TOKEN);
                }
                session.save();

                auth.setProperty("profile/tokenExpiryTime", new ValueToken(tokenExpiryTime.toString()));
                auth.setProperty(PROFILE_TOKEN, new ValueToken(token));
                session.save();
            }
        } catch (ValueFormatException e) {
            LOG.error("Value Format Exception" + e);
        } catch (RepositoryException e) {
            LOG.error("Repository Exception" + e);
        }

    }

    /**
     * This method gets the user details and sets them to the Map.
     *
     * @param userEmailId
     * @param resourceResolver
     * @return
     */
    private Map<String, String> getUserDetails(String userEmailId, ResourceResolver resourceResolver) {
        UserManager manager = resourceResolver.adaptTo(UserManager.class);
        HashMap<String, String> userDetails = new HashMap<String, String>();
        String formattedName = "";
        String emailid = "";
        String token = "";
        try {
            Authorizable auth = manager.getAuthorizable(userEmailId);
            if (auth != null) {
                if (auth.hasProperty("profile/givenName")) {
                    formattedName = formattedName + auth.getProperty("profile/givenName")[0].getString();
                }
                if (auth.hasProperty("profile/familyName")) {
                    formattedName = formattedName + " " + auth.getProperty("profile/familyName")[0].getString();
                }
                if (auth.hasProperty("profile/email")) {
                    emailid = auth.getProperty("profile/email")[0].getString();
                }
                if (auth.hasProperty(PROFILE_TOKEN)) {
                    token = auth.getProperty(PROFILE_TOKEN)[0].getString();
                }
            }
            userDetails.put("email", emailid);
            userDetails.put("formattedName", formattedName);
            userDetails.put("token", token);

        } catch (RepositoryException exc) {
            LOG.error("Repository not found" + exc);
        }

        return userDetails;
    }

    /**
     * This method uses a map, which is used to map the keys and values of mail template.
     *
     * @param token
     * @param username
     * @return parameters
     */
    private Map<String, String> getParameterMap(String token, String username) {
        final Map<String, String> parameters = new HashMap<String, String>();
        if (resetpath != null && hostName != null) {

            String resetPathValue = hostName + resetpath + ".html?token=" + token;
            parameters.put("user", username);
            parameters.put("resetpath", resetPathValue);
        }
        return parameters;
    }

    /**
     * This class is used to set the token in the user profile.
     */
    class ValueToken implements Value {
        private String tokenValue;
        private Calendar tokenExpireDate;

        ValueToken(String tokenValue) {
            this.tokenValue = tokenValue;
        }

        @Override
        public String getString() throws ValueFormatException, IllegalStateException, RepositoryException {
            return tokenValue;
        }

        @Override
        public InputStream getStream() throws RepositoryException {
            return null;
        }

        @Override
        public Binary getBinary() throws RepositoryException {
            return null;
        }

        @Override
        public long getLong() throws ValueFormatException, RepositoryException {
            return 0;
        }

        @Override
        public double getDouble() throws ValueFormatException, RepositoryException {
            return 0;
        }

        @Override
        public BigDecimal getDecimal() throws ValueFormatException, RepositoryException {
            return null;
        }

        @Override
        public Calendar getDate() throws ValueFormatException, RepositoryException {
            return tokenExpireDate;
        }

        @Override
        public boolean getBoolean() throws ValueFormatException, RepositoryException {
            return false;
        }

        @Override
        public int getType() {
            return 0;
        }
    }

}

