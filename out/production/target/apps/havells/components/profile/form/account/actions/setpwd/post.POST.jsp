<%@page session="false" contentType="text/javascript"
    import="java.util.HashMap,
            javax.jcr.Session,
            org.apache.jackrabbit.api.JackrabbitSession,
            com.havells.services.GenericService,
            org.apache.jackrabbit.api.security.user.Authorizable,
            org.apache.sling.api.resource.ResourceResolver,
            org.apache.jackrabbit.api.security.user.User,
            com.day.cq.wcm.foundation.forms.FormsHelper,
            org.apache.jackrabbit.api.security.user.UserManager"
%>
<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %>

        <%!
            final String USER_ID = "userid";
            final String EM = "mailid";
            final String repPassword = "confirmpassword";
         %>
        <sling:defineObjects/>
        <%
            final GenericService genericeService = sling.getService(GenericService.class);
            JackrabbitSession session = null;
            String error = null;
            UserManager manager = null;
            ResourceResolver adminResourceResolver = null;
            try {
                adminResourceResolver = genericeService.getAdminResourceResolver();
                session = (JackrabbitSession) adminResourceResolver.adaptTo(javax.jcr.Session.class);
                String userid = request.getParameter(USER_ID);
                String newpassword = request.getParameter(repPassword);
                String auth = "";
                if(userid != null && !userid.isEmpty() && userid.equals("anonymous")) {
                    auth = userid;
                } else {
                    auth = userid==null? null : slingRequest.getRequestParameter(USER_ID).getString();
                }
                if (auth!=null) {
                    manager = session.getUserManager();
                    Authorizable authorizable = manager.getAuthorizable(auth);
                    if (authorizable != null) {

                        User user = (User)authorizable;
                        user.changePassword(newpassword);

                        if (!manager.isAutoSave()) {
                            session.save();
                         log.debug("session is saved");
                        }
                    } else {
                    error = "user does not exist.";
                    }
                }

                } catch (Exception e) {
                log.error(" Exception thrown while resetting a user password ", e);
                    error = e.getMessage();
                } finally {
                    if (session!=null && session.isLive()) {
                        session.logout();
                    }
                    if (adminResourceResolver!=null && adminResourceResolver.isLive()) {
                        adminResourceResolver.close();
                    }
                }
                String path = request.getParameter("redirectTothank");
            if (error!=null) {
                log.error(error);
                path = request.getParameter("redirectTo404");
            }

            path = path==null? "":path;
            if ("".equals(path)) {
                FormsHelper.redirectToReferrer(slingRequest, slingResponse, new HashMap<String, String[]>());
            } else {
                if (path.indexOf(".")<0) {
                    path += ".html";
                }
                response.sendRedirect(slingRequest.getResourceResolver().map(request, path));
            }
        %>