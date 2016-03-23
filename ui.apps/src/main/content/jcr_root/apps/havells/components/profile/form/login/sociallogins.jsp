<%@include file="/apps/havells/global.jsp" %>
<%@page contentType="text/html; charset=utf-8" import="javax.jcr.Session,
                                                       java.util.Iterator,
                                                       org.apache.sling.api.resource.Resource,
                                                       com.day.cq.wcm.api.WCMMode,
                                                       com.day.cq.wcm.webservicesupport.Configuration,
                                                       com.day.cq.wcm.webservicesupport.ConfigurationManager" %>
<script src="https://apis.google.com/js/platform.js" async defer></script>

<%
    // getting the username of the logged-in user
    Session sess = resourceResolver.adaptTo(Session.class);
    final String userId = sess.getUserID().replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n");
    pageContext.setAttribute("userId", userId);

    ConfigurationManager cfgMgr = sling.getService(ConfigurationManager.class);

    Configuration facebookConfiguration = null, twitterConfiguration = null, gmailConfiguration = null;
    String gmail_client_id = "", gmail_user_group = "";

    String[] services = pageProperties.getInherited("cq:cloudserviceconfigs", new String[]{});
    if (cfgMgr != null) {
        facebookConfiguration = cfgMgr.getConfiguration("facebookconnect", services);
        twitterConfiguration = cfgMgr.getConfiguration("twitterconnect", services);
        gmailConfiguration = cfgMgr.getConfiguration("gmailconnect", services);
    }


    final String uniqSuffix = resource.getPath().replaceAll("/", "-").replaceAll(":", "-");
    final String divID = "sociallogin" + uniqSuffix;

	final boolean isAnonymous = userId.equals("anonymous");
	final boolean isDisabled = WCMMode.DISABLED.equals(WCMMode.fromRequest(request));
	final String loginSuffix = isDisabled && isAnonymous ? "/j_security_check" : "/connect";
    String redirectTo = currentPage.getPath();
    if( !redirectTo.endsWith(".html")) {
        redirectTo += ".html";
    }
    final String contextPath = slingRequest.getContextPath();
  if (facebookConfiguration != null || twitterConfiguration != null || gmailConfiguration!= null) { %>
    <div class="or"><span>OR</span></div>
    <div class="socialLoginsWrap">
    <%

        if (facebookConfiguration != null) {
            Resource configResource = facebookConfiguration.getResource();
            Page configPage = configResource.adaptTo(Page.class);
            final String configid = configPage.getProperties().get("oauth.config.id", String.class);
    %>

    <input type="submit" tabindex="9993" class="form_button_submit login_facebook" name="create"
           value=""
           onclick="$CQ.SocialAuth.sociallogin.doOauth(
                   '<%= xssAPI.encodeForJSString(divID) %>',
                   '<%= xssAPI.encodeForJSString(configPage.getPath()) %>',
                   '<%= xssAPI.encodeForJSString(configid) %>',
                   '<%= loginSuffix %>',
                   '<%= xssAPI.encodeForJSString(contextPath) %>'
                   );return false;"/>

    <% }
        if (twitterConfiguration != null) {
            Resource configResource = twitterConfiguration.getResource();
            Page configPage = configResource.adaptTo(Page.class);
            final String configid = configPage.getProperties().get("oauth.config.id", String.class);
    %>

    <input type="submit" tabindex="9994" class="form_button_submit login_twitter" name="create"
           value=""
           onclick="$CQ.SocialAuth.sociallogin.doOauth(
                   '<%= xssAPI.encodeForJSString(divID) %>',
                   '<%= xssAPI.encodeForJSString(configPage.getPath()) %>',
                   '<%= xssAPI.encodeForJSString(configid) %>',
                   '<%= loginSuffix %>',
                   '<%= xssAPI.encodeForJSString(contextPath) %>'
                   );return false;"/>

    <%
        }
    if (gmailConfiguration != null) {
        Resource parentResource = gmailConfiguration.getResource().getChild("jcr:content");
        Iterator<Resource> res = null;
        if(parentResource.hasChildren()) {
            res = gmailConfiguration.getResource().getChild("jcr:content").getChildren().iterator();
        }
        while(res.hasNext()) {
            Resource resID = res.next();
            ValueMap resProperties = resID.getValueMap();
            if(resProperties.containsKey("oauth.client.id")) {
                gmail_client_id =  resProperties.get("oauth.client.id", String.class);
            }
            if(resProperties.containsKey("oauth.create.users.groups")) {
                gmail_user_group = resProperties.get("oauth.create.users.groups", String.class);
            }
        }

    %>
    <input type="submit" class="form_button_submit login_gmail" name="create" value="" id="gmailBtn"/>
    </div>
   <%}
   }%>



<script type="text/javascript">

    $CQ(document).ready(function () {
        //listening for this global event - triggered from SocialAuth.js - $CQ.SocialAuth.oauthCallbackComplete
        //any element can subscribe to this event to perform custom functionality post-oauth-callback completion
        //the social login component will listen for it here to perform the appropriate redirect as configured
        $CQ(document).bind('oauthCallbackComplete', function(ev, userId) {
            //oauthCallbackComplete happened
            CQ_Analytics.ProfileDataMgr.loadProfile(userId);
            <% if (redirectTo != null && redirectTo.length() > 0) { %>
            CQ.shared.Util.reload(null, '<%= xssAPI.encodeForJSString(contextPath + redirectTo) %>');
            <% } else { %>
            CQ.shared.Util.reload();
            <% } %>
        });
    });

    function signinCallback(authResult) {
        if (authResult['status']['signed_in']) {
            gapi.client.load('plus', 'v1', apiClientLoaded);
        }

    }

    /**
     * Sets up an API call after the Google API client loads.
     */
    function apiClientLoaded() {
        gapi.client.plus.people.get({userId: 'me'}).execute(handleResponse);
    }

    function handleResponse(resp) {
        var userId = 'gmail-'+ resp['id'];
        var token = "";
        $.ajax({
            type: 'POST',
            url: "/bin/servlets/gmailLoginServlet",
            data:{
                json: JSON.stringify(resp),
                userGroup:'<%=gmail_user_group%>'
            },
            success: function(response){
                token = $.parseJSON(response).token;
                $.ajax({
                    type: 'POST',
                    url: "<%=currentPage.getPath()%>/j_security_check",
                    data:{
                        j_username : userId,
                        j_password : token,
                        j_validate:true,
                        _charset_:"UTF-8",
                        resource:"<%= xssAPI.encodeForHTMLAttr(redirectTo) %>"
                    },
                    success: function(response){

                        CQ_Analytics.ProfileDataMgr.loadProfile(userId);
                        <% if (redirectTo != null && redirectTo.length() > 0) { %>
                        CQ.shared.Util.load('<%= xssAPI.encodeForJSString(contextPath + redirectTo) %>');
                        <% } else { %>
                        CQ.shared.Util.reload();
                        <% } %>
                    },
                    error: function(jqXHR, textStatus, errorThrown){
                        console.log();
                    }
                });
            },
            error: function(jqXHR, textStatus, errorThrown){
                console.log();
            }
        });

    }

    $('#gmailBtn').on('click', function(){
        gapi.auth.signIn({
            'callback': 'signinCallback',
            'clientid': '<%=gmail_client_id%>',
            'cookiepolicy': 'single_host_origin',
            'requestvisibleactions': 'http://schema.org/AddAction',
            'scope': 'https://www.googleapis.com/auth/plus.profile.emails.read'
        });
    });
</script>