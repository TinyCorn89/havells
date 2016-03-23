<%@include file="/apps/havells/global.jsp" %>
<%@ page import="com.havells.core.model.SpotLightChild" %>
<c:set var="spotlight" value="<%= new SpotLightChild(resource) %>"/>
<c:choose>
    <c:when test="${properties.heading=='' || properties.heading==null}">
        <p>Enter Slide Details</p>
    </c:when>
</c:choose>
<c:choose>
    <c:when test="${!editMode}">
        <c:choose>
            <c:when test="${((properties.customColor != null) && (properties.customColor!=''))}">
                <c:set var="background" value="${properties.customColor}"/>
            </c:when>
            <c:when test="${((properties.color != null) && (properties.color!=''))}">
                <c:set var="background" value="${properties.color}"/>
            </c:when>
            <c:otherwise>
                <c:set var="background" value="#e02626"/>
            </c:otherwise>
        </c:choose>
    </c:when>
</c:choose>
<c:if test='${properties.noLeftRibbon}'>
    <c:set var="background" value="transparent"/>
</c:if>
<c:choose>
    <c:when test="${!editMode}">
        <c:choose>
            <c:when test="${((properties.rightRibbonCustomColor != null) && (properties.rightRibbonCustomColor!=''))}">
                <c:set var="rightRibbonBackground" value="${properties.rightRibbonCustomColor}"/>
            </c:when>
            <c:when test="${((properties.rightRibbonColor != null) && (properties.rightRibbonColor!=''))}">
                <c:set var="rightRibbonBackground" value="${properties.rightRibbonColor}"/>
            </c:when>
            <c:otherwise>
                <c:set var="rightRibbonBackground" value="#e02626"/>
            </c:otherwise>
        </c:choose>
    </c:when>
</c:choose>
<c:if test='${properties.noRightRibbon}'>
    <c:set var="rightRibbonBackground" value="transparent"/>
</c:if>

<div class="spotlightWrapper">
    <img src="${spotlight.imageUrl}" alt="" style="max-width:100%;">

    <div class="spotlightContentLeft" style="background:#${background}">
        <div class="content">
            <div class="heading">${properties.heading}</div>
            ${properties.description}
            <div class="buttonWrapper">
                <c:forEach begin="1" end="${properties.noOfButtons}" varStatus="loop">
                    <cq:include path="cta_${loop.index}" resourceType="/apps/havells/components/common/cta"/>
                </c:forEach>
            </div>
        </div>
    </div>
    <div class="spotlightContentRight" style="background:#${rightRibbonBackground}"></div>
</div>
<c:if test="${editMode}">
    <div style="clear:both;height:10px;">&nbsp;</div>
</c:if>
