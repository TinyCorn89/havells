<%--

  image-banner component.

  banner component with an image, text and CTA

--%><%
%><%@include file="/apps/havells/global.jsp"%><%
%><%@page session="false" %><%
%><%
	// TODO add you code here
%>
<div class="cardRow hpCsrBg" style="background-image: url(${properties.fileReference}); height: ${properties.height}px;">
    <div class="col">
        <div class="sliderWrapper">
            <div class="heading ${properties.color}">${properties.title}</div>
            <div class="subHead">${properties.subHead}</div>
            <div class="description">${properties.description}</div>
            <c:if test="${properties.includeButtonWrapper}">
                <div class="buttonWrapper">
                    <cq:include path="cta" resourceType="havells/components/common/cta"/>
                </div>
            </c:if>
        </div>
    </div>
</div>
<c:if test="${editMode}">
    <div style="clear:both;"></div>
</c:if>


