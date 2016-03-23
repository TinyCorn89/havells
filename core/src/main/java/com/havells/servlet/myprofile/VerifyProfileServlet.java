package com.havells.servlet.myprofile;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component(immediate = true, metatype = false, label = "ProfileServlet")
@Service
@Properties(value = {
        @Property(name = "sling.servlet.paths", value = "/bin/profile")
})
public class VerifyProfileServlet extends SlingSafeMethodsServlet {

    private final Logger log = LoggerFactory.getLogger(VerifyProfileServlet.class);

    private final String DEFAULT_PROFILE_IMAGE = "/etc/clientlibs/havells/image/profile.jpg";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws
            IOException {
        JSONObject jsonObject = new JSONObject();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String path = request.getParameter("path");
        Resource resource = request.getResourceResolver().getResource(path);
        try {
            if (resource == null) {
                jsonObject.put("path", DEFAULT_PROFILE_IMAGE);
            } else {
                jsonObject.put("path", path);
            }
            response.getWriter().write(jsonObject.toString());
        } catch (Exception e) {
            log.error("Error while verifying profile: " + e);
        }
    }
}
