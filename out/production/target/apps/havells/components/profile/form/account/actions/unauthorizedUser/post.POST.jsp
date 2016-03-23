<%@include file="/libs/foundation/global.jsp" %>
<%@page session="false"
        import="com.havells.services.SendEmailService,
                com.adobe.granite.crypto.CryptoSupport,
                com.havells.core.model.signup.UserProfileModel,
                java.io.IOException,
                org.apache.jackrabbit.api.security.user.UserManager,
                org.apache.jackrabbit.api.security.user.Authorizable,
                com.havells.services.GenericService" %>

<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling/1.0" %>
<sling:defineObjects/>

<%
    String errorPage = currentPage.getPath() + ".500.html";
    GenericService service = sling.getService(GenericService.class);
    UserManager manager = service.getAdminResourceResolver().adaptTo(UserManager.class);
    String email = request.getParameter("email");
    try {
        Authorizable auth = manager.getAuthorizable(email);
        if (auth == null) {
            UserProfileModel userProfileModel = new UserProfileModel(
                    sling.getService(CryptoSupport.class),
                    sling.getService(SendEmailService.class), slingRequest, slingResponse
            );
            userProfileModel.setProfileData(request, response, resource, errorPage, service.getAdminResourceResolver());
        } else {
            errorPage = currentPage.getPath() + ".600.html";
            response.sendRedirect(resourceResolver.map(request, errorPage));
        }
    } catch (IOException ioEx) {
        response.sendRedirect(resourceResolver.map(request, errorPage));
    }
%>
