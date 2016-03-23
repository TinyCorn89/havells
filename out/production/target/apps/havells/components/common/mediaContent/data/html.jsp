<%@include file="/apps/havells/global.jsp" %>
<%@ page contentType="text/html; charset=utf-8" import="com.havells.core.model.MediaContentWrapper" %>

<c:set var="mediaContentWrapper" value="<%=new MediaContentWrapper(slingRequest, resource)%>"/>

<c:choose>
    <c:when test="${not empty mediaContentWrapper.links}">
        <c:set var="count" value="0"/>
        <c:forEach items="${mediaContentWrapper.links}" var="link">
            <c:if test="${count ge mediaContentWrapper.startIndex && count lt mediaContentWrapper.lastIndex}">
                <sling:include path="${link}"/>
            </c:if>
            <c:set var="count" value="${count + 1}"/>
        </c:forEach>
        <c:if test="${mediaContentWrapper.linksSize gt mediaContentWrapper.maxItemsPerPage}">
            <%@include file="pagenumber.jsp" %>
        </c:if>
    </c:when>
    <c:otherwise>
        <b style='color:red'>no data found</b>
    </c:otherwise>
</c:choose>
