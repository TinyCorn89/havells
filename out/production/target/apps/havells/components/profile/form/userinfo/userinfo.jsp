<%@include file="/apps/havells/global.jsp"%><%
%><%@page session="false" import="
          com.day.cq.personalization.UserPropertiesUtil,
          com.day.cq.wcm.commons.WCMUtils, org.apache.sling.api.resource.ValueMap,org.apache.sling.api.auth.Authenticator"%>
<%@ page import="com.day.cq.wcm.api.components.IncludeOptions" %>
<%@taglib prefix="personalization" uri="http://www.day.com/taglibs/cq/personalization/1.0" %><%

    final boolean isAnonymous = UserPropertiesUtil.isAnonymous(slingRequest);
    final boolean isDisabled = WCMMode.DISABLED.equals(WCMMode.fromRequest(request));
    Resource signInDataRes = resourceResolver.getResource(globalPage+"/jcr:content/hpar/header/topLinks/signin/login");
    ValueMap myProfile = null;
    String profilePath = "#";
    if(signInDataRes != null){
        myProfile = signInDataRes.adaptTo(ValueMap.class);
        if(myProfile != null){
            profilePath = (String) myProfile.get("myprofile", "#");
            profilePath = profilePath+".html";
        }
    }
    //This code will only add the surrounding DIVs for the editbars when in EDIT mode only
    if (WCMMode.fromRequest(request) != WCMMode.EDIT && WCMMode.fromRequest(request) != WCMMode.DESIGN) {
        IncludeOptions.getOptions(request, true).setDecorationTagName("");
    }
  if (!isAnonymous) { %>
    <a href="javascript:;" class="signIn">
        Welcome, <personalization:contextProfileProperty propertyName="givenName" prefix="" suffix=""/>
    </a>
 <% }%>
    <ul class="noBorder">
        <%  if (!isAnonymous) { %>
        <li>
            <div class="signInWrapper">
                <script type="text/javascript">function logout() {
                    if (_g && _g.shared && _g.shared.ClientSidePersistence) {
                        _g.shared.ClientSidePersistence.clearAllMaps();
                    }
                    <%if( !isDisabled ) { %>
                    if( CQ_Analytics && CQ_Analytics.CCM) {
                        CQ.shared.Util.reload();
                    }
                    <%  } else { %>
                    if( CQ_Analytics && CQ_Analytics.CCM) {
                        CQ_Analytics.ProfileDataMgr.clear();
                        CQ_Analytics.CCM.reset();
                    }
                    CQ.shared.Util.load("<%= resourceResolver.map(request, "/system/sling/logout") %>.html?" +
                            "<%= Authenticator.LOGIN_RESOURCE %>="+window.location.pathname);
                    <%      } %>
                }
                </script>
                <div class="clearfix"></div>
                <a href="<%=profilePath%>">My Profile</a><div class="clearfix"></div>
                <a href="javascript:logout();"><%= i18n.get("Sign Out") %></a>
            </div>
        </li>
        <%}%>
    </ul>
