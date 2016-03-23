<%@page import="com.day.cq.personalization.UserPropertiesUtil,
                 com.day.cq.wcm.api.WCMMode"%>
<%@ page import="com.day.cq.wcm.api.components.IncludeOptions" %>
<%@ page import="com.havells.core.model.Multifield" %>
<%@include file="/apps/havells/global.jsp"%>
<%
    //This code will only add the surrounding DIVs for the editbars when in EDIT mode only
    if (WCMMode.fromRequest(request) != WCMMode.EDIT && WCMMode.fromRequest(request) != WCMMode.DESIGN) {
        IncludeOptions.getOptions(request, true).setDecorationTagName("");
    }
%>
<c:set var="multifield" value="<%=new Multifield(resource)%>"/>
<c:set var="toplinks" value="${multifield.list}"/>
    <ul>
        <c:choose>
            <c:when test="${toplinks ne null and not empty toplinks}">
                <c:forEach var="linkObj" items="${toplinks}" varStatus="counter">
                    <li><a href="${linkObj.path}">${linkObj.link}</a>
                        <cq:include path="${linkObj.encodedName}" resourceType="havells/components/common/links"/>
                     </li>
                </c:forEach>
                <li>
                    <cq:include path="signin" resourceType="havells/components/profile/form/signinWrapper"/>
                </li>
            </c:when>
            <c:otherwise>
                  edit links here.
            </c:otherwise>
        </c:choose>
    </ul>