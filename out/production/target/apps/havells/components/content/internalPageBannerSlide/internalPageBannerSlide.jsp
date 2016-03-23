<%@include file="/apps/havells/global.jsp" %>
<%@page session="false" %>
<c:choose>
    <c:when test="${!editMode}">
        <c:choose>
            <c:when test="${properties.linkUrl==''}">
                <c:set var="imageUrl" value="#"/>
            </c:when>
            <c:otherwise>
                <c:set var="imageUrl" value="${properties.linkUrl}.html"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${not empty properties.titleCustomColor}">
                <c:set var="titleColorValue" value="${properties.titleCustomColor}"/>
            </c:when>
            <c:when test="${not empty properties.titleColor}">
                <c:set var="titleColorValue" value="${properties.titleColor}"/>
            </c:when>
            <c:otherwise>
                <c:set var="titleColorValue" value="fff"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${not empty properties.descCustomColor}">
                <c:set var="descColor" value="${properties.descCustomColor}"/>
            </c:when>
            <c:when test="${not empty properties.descColor}">
                <c:set var="descColor" value="${properties.descColor}"/>
            </c:when>
            <c:otherwise>
                <c:set var="descColor" value="fff"/>
            </c:otherwise>
        </c:choose>
        <a href="${imageUrl}.html" class="mediaVideo">
            <img src="${properties['image/fileReference']}" alt="">
        </a>
    </c:when>
    <c:otherwise>
        <c:if test="${(properties.title==null)||(properties.title=='')}">
            Enter Slide Information..
        </c:if>
        <img src="${properties['image/fileReference']}" alt="" style="width:100%;" >
    </c:otherwise>
</c:choose>
<div class="internalSpotlightContent">
    <div style="color:#${titleColorValue};display:block;">${properties.title}</div>
    <div style="color:#${descColor};">${properties.description}</div>
    <div class="buttonWrapper">
        <c:forEach begin="1" end="${properties.noOfButtons}" varStatus="loop">
            <cq:include path="cta_${loop.index}" resourceType="/apps/havells/components/common/cta"/>
        </c:forEach>
    </div>
</div>
