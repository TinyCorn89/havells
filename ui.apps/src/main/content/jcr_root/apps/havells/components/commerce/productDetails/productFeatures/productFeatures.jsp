<%@ page import="com.havells.core.model.product.ProductDetails" %>
<%@page session="false" %>
<%@include file="/apps/havells/global.jsp" %>
<%
    ProductDetails productDetails = (ProductDetails) request.getAttribute("cq.commerce.product.details");
%>
<c:set var="featureClass" value=""/>
<c:set var="style" value='style="background: #f7f7f7; display:block;"'/>
<c:set var="display" value=''/>
<c:set var="featureType" value="<%=properties.get("featureType","")%>"/>
<c:set var="featureHeading" value="<%=properties.get("heading", "[HEADER]")%>"/>
<c:choose>
    <c:when test="${editMode != true}">
        <c:set var="featureClass" value="featureClicker"/>
        <c:set var="style" value=''/>
        <c:set var="display" value='noDisplay'/>
    </c:when>
</c:choose>
<c:choose>
    <c:when test="${featureType eq 'detailFeatures'}">
        <%@include file="detailsFeatures.jsp" %>
    </c:when>
    <c:when test="${featureType eq 'faq'}">
            <%@include file="faqs.jsp" %>
    </c:when>
    <c:when test="${featureType eq 'techspecs'}">
            <%@include file="techspecs.jsp" %>
    </c:when>
    <c:when test="${featureType eq 'techImages'}">
            <%@include file="technicalDrawing.jsp" %>
    </c:when>
    <c:when test="${featureType eq 'accessories'}">
        <%@include file="accesories.jsp" %>
    </c:when>
</c:choose>

