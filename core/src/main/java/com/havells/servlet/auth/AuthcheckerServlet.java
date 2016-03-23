package com.havells.servlet.auth;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.Property;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.Session;

@Component(metatype=false)
@Service
public class AuthcheckerServlet extends SlingSafeMethodsServlet {

    @Property(value="/bin/permissioncheck")
    static final String SERVLET_PATH="sling.servlet.paths";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void doHead(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try{
            //retrieve the requested URL
            String uri = request.getParameter("uri");
            //obtain the session from the request
            Session session = request.getResourceResolver().adaptTo(javax.jcr.Session.class);
            //perform the permissions check
            try {
                session.checkPermission(uri, Session.ACTION_READ);
                logger.info("authchecker says OK");
                response.setStatus(SlingHttpServletResponse.SC_OK);
            } catch(Exception e) {
                logger.info("authchecker says READ access DENIED!");
                response.setStatus(SlingHttpServletResponse.SC_FORBIDDEN);
            }
        }catch(Exception e){
            logger.error("authchecker servlet exception: " + e.getMessage());
            response.setStatus(SlingHttpServletResponse.SC_FORBIDDEN);
        }
    }
}