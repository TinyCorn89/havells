package com.havells.servlet.myprofile;

import com.havells.services.GenericService;
import org.apache.felix.scr.annotations.*;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import java.io.IOException;

@Component(immediate = true, metatype = false, label = "VerifyUserName")
@Service
@Properties(value = {
        @Property(name = "sling.servlet.methods", value = "Post"),
        @Property(name = "sling.servlet.paths", value = "/bin/servlets/VerifyUserNameServlet")
})
public class VerifyUserNameServlet extends SlingAllMethodsServlet {

    protected static final Logger LOG = LoggerFactory.getLogger(VerifyUserNameServlet.class);

    @Reference
    GenericService genericService;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws
            IOException {
        ResourceResolver resourceResolver = null;
        String auth = request.getParameter("email");
        try {
            resourceResolver = genericService.getAdminResourceResolver();
            if (resourceResolver != null) {
                UserManager manager = resourceResolver.adaptTo(UserManager.class);
                Authorizable authorizable = manager.getAuthorizable(auth);
                if (authorizable != null) {
                    response.getWriter().write("true");
                } else {
                    response.getWriter().write("false");
                }
            }
        } catch (RepositoryException e) {
            LOG.error("RepositoryException:: " + e);
        } finally {
            if (resourceResolver != null && resourceResolver.isLive()) {
                resourceResolver.close();
            }
        }
    }
}

