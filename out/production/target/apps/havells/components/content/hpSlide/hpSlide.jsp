<%@include file="/apps/havells/global.jsp" %>
<%@ page import="com.havells.core.model.SpotLightChild,java.util.ArrayList,java.util.Iterator" %>
<c:set var="hpcarousel" value="<%= new SpotLightChild(resource) %>"/>
<c:choose>
    <c:when test="${properties.title=='' || properties.title==null}">
        <p>Enter Slide Details</p>
    </c:when>
</c:choose>
<c:if test="${!wcmMode}">
    <c:set var="background" value="url(${properties.iconFileReference}) #e02626 50% no-repeat;" />
</c:if>
<div class="hpCarouselBg">
    <div class="col"><img src="${hpcarousel.imageUrl}" id="prdThumb" alt="" style="max-width: 100%;">
        <div class="hpCarouselBgIcon icn1" style="background: ${background};max-width: 100%;"></div>
    </div>
    <div class="col">
        <div class="sliderWrapper">
            <h2>${properties.title}</h2>
            <div class="subHeadWrap">
                <div class="subHead">${properties.subTitle}</div>
            </div>
            ${properties.description }
            <div class="buttonWrapper">
                <c:forEach begin="1" end="${properties.noOfButtons}" varStatus="loop">
                    <cq:include path="cta_${loop.index}" resourceType="/apps/havells/components/common/cta"/>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
<c:if test="${editMode}">
    <div style="clear:both;height:10px;">&nbsp;</div>
</c:if>
