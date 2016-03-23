<%@include file="/apps/havells/global.jsp" %>
<%@page session="false"
        contentType="text/html; charset=utf-8"
        import="com.havells.commons.sling.Resources" %><%
%>
<c:set var="redirectTarget" value="${properties.redirectTarget}"/>
<c:set var="redirect" value="<%=Resources.getValidURL(resourceResolver, String.valueOf(pageContext.getAttribute("redirectTarget")))%>"/>
<c:if test="${not empty redirectTarget}">
    <c:if test="${!editMode}">
        <c:choose>
            <c:when test='${redirect ne "#"}'>
                <c:redirect url="${redirect}"/>
            </c:when>
            <c:otherwise>
                <c:redirect url="${redirectTarget}"/>
            </c:otherwise>
        </c:choose>
    </c:if>
</c:if>
<c:if test="${editMode and redirectTarget ne null}">
    This page redirects to <a href="${redirectTarget}">${redirectTarget}</a>
</c:if>
