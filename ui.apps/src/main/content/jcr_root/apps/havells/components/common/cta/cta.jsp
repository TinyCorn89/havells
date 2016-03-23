<%@ page import="com.havells.commons.sling.Resources" %>
<%@include file="/apps/havells/global.jsp" %>

<c:set var="target" value=" "></c:set>
<c:set var="bold" value=" "/>
<c:set var="arrowPosition" value="${properties.arrowposition}"/>
<c:set var="fontSize" value="${properties.fontsize}"/>

<c:if test="${not empty properties.openOption}">
    <c:set var="target" value="${properties.openOption}"></c:set>
</c:if>

<c:if test="${properties.boldText == 'true'}">
    <c:set var="bold" value="text-bold"></c:set>
</c:if>

<c:if test="${properties.linkhover == 'true'}">
    <c:set var="colorChange" value="noColorChange"></c:set>
</c:if>

<c:set var="url" value="<%=Resources.getValidURL(resourceResolver, properties.get("url",""))%>"/>

<div class="buttonGlbl ${properties.color} ${bold} ${colorChange}">
    <a ${target} href="${url}">
        <span style="font-size: ${fontSize};">
            <c:choose>
                <c:when test="${arrowPosition =='left'}">
                    <i class="${properties.linkType}"></i> ${properties.textOverlay}
                </c:when>
                <c:otherwise>
                    ${properties.textOverlay} <i class="${properties.linkType} arrow-right"></i>
                </c:otherwise>
            </c:choose>
        </span>
    </a>
</div>
<div class="clearBoth"></div>
<script>
    $(document).ready(function () {
        if ($('.medialist').length) {
            $('.cta').addClass("cta-border");
        }
    });
</script>


