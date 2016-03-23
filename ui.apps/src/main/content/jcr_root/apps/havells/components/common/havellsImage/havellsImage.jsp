<%@ page import="com.havells.util.RenditionUtil,
                 com.havells.core.model.product.ProductConstant" %>
<%@page session="false"%>
<%@include file="/apps/havells/global.jsp"%>
<c:set var="isClickable" value="${properties.clickable == 'true'}"/>
<c:set var="linkURL" value=""/>
<c:set var="openAs" value=""/>
<c:choose>
    <c:when test="${properties.fileReference ne null}">
        <c:set var="fileReference"
               value="<%= properties.get("fileReference","")%>"/>
    </c:when>
    <c:when test="<%=resourceResolver.getResource(resource.getPath()+"/file") != null%>">
        <c:set var="fileReference" value="<%=resource.getPath()+"/file"%>"/>
    </c:when>
    <c:otherwise>
        <c:set var="fileReference" value=""/>
    </c:otherwise>
</c:choose>

<c:if test="${isClickable}">
    <c:set var="linkURL" value="${properties.linkURL}"/>
    <c:set var="openAs" value="${properties.openAs}"/>
    <a href="${linkURL}.html" ${openAs}>
</c:if>
<div class="chairmanPic">
    <img src="${fileReference}" alt="Provide Image Path"
         style="height: ${properties.height}px ; width: ${properties.width}px"/>
    <c:if test="${properties.overlay ne null}">
        <div class="charimanProfileTxt">
                ${properties.overlay}
        </div>
    </c:if>
</div>
<c:if test="${isClickable}">
    </a>
</c:if>
