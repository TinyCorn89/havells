<%@include file="/apps/havells/global.jsp"%>
<%@page session="false" import="com.day.cq.personalization.UserPropertiesUtil,
                                com.day.cq.wcm.api.components.IncludeOptions"%>
<%@taglib prefix="personalization" uri="http://www.day.com/taglibs/cq/personalization/1.0" %>
<%
    //This code will only add the surrounding DIVs for the editbars when in EDIT mode only
    if (WCMMode.fromRequest(request) != WCMMode.EDIT && WCMMode.fromRequest(request) != WCMMode.DESIGN) {
        IncludeOptions.getOptions(request, true).setDecorationTagName("");
    }
    final boolean isAnonymous = UserPropertiesUtil.isAnonymous(slingRequest);
    if (isAnonymous) {%>
        <a href="javascript:;" class="signIn"><%=properties.get("signInLabel","Sign In")%></a>
        <ul class="noBorder">
            <li>
                <cq:include path="login" resourceType="havells/components/profile/form/login"/>
            </li>
        </ul>
    <% } else {%>
            <cq:include path="userinfo" resourceType="havells/components/profile/form/userinfo"/>
    <% } %>