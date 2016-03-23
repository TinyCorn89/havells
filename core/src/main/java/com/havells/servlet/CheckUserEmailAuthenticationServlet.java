package com.havells.servlet;

import com.havells.services.GenericService;
import com.havells.servlet.myprofile.ForgotPasswordServlet;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.felix.scr.annotations.Property;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.jackrabbit.util.Base64;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component(immediate = true, metatype = true, label = "Checks User Email Authentication")
@Service
@Properties(value = {
        @Property(name = "sling.servlet.methods", value = "GET"),
        @Property(name = "sling.servlet.paths", value = "/bin/havells/checkUserEmail")
})
public class CheckUserEmailAuthenticationServlet extends SlingSafeMethodsServlet {

    @Reference
    GenericService genericService;
    protected static final Logger LOG = LoggerFactory.getLogger(ForgotPasswordServlet.class);

    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        ResourceResolver resourceResolver = genericService.getAdminResourceResolver();
        Session session= resourceResolver.adaptTo(Session.class);
        UserManager manager = resourceResolver.adaptTo(UserManager.class);
        JSONObject jsonObject = new JSONObject();
        String userEmailId = "";
        String token = request.getParameter("token");
        String tokenExpiryDateUrl = Base64.decode(token);
        String[] tempToken = StringUtils.split(tokenExpiryDateUrl, '$');

        try {
            if (tempToken.length == 2) {
                userEmailId = tempToken[0];
                tokenExpiryDateUrl = tempToken[1];
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
                Date tokenExpiryDateFromUrl = formatter.parse(tokenExpiryDateUrl);
                Authorizable auth = manager != null ? manager.getAuthorizable(userEmailId) : null;
                if (auth != null) {
                    if (auth.hasProperty("profile/token") && auth.hasProperty("profile/tokenExpiryTime")) {
                        String tokenExpiryDateProfile = Base64.decode(auth.getProperty("profile/tokenExpiryTime")[0].getString());
                        Date tokenExpiryDateFromProfile = formatter.parse(tokenExpiryDateProfile);

                        if (tokenExpiryDateFromProfile.compareTo(tokenExpiryDateFromUrl) > 0) {
                            jsonObject.put("mailIds", userEmailId);
                            LOG.debug("User Authenticated... To reset Password.");
                            auth.removeProperty("profile/token");
                            auth.removeProperty("profile/tokenExpiryTime");
                            session.save();
                            response.setStatus(SlingHttpServletResponse.SC_OK);
                            response.getWriter().write(String.valueOf(jsonObject));
                        } else {
                            response.setStatus(SlingHttpServletResponse.SC_UNAUTHORIZED);
                        }
                    }else{
                        response.setStatus(SlingHttpServletResponse.SC_UNAUTHORIZED);
                    }
                }
            } else {
                response.setStatus(SlingHttpServletResponse.SC_UNAUTHORIZED);
            }
        } catch (RepositoryException exc) {
            LOG.error("Repository not found" + exc);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (ParseException parseException) {
            LOG.error("Parse Exception " + parseException);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (JSONException jSONException) {
            LOG.error("JSON Exception when sending json data from server to jsp " + jsonObject);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            if (session != null) {
                session.logout();
            }
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
    }
}
