<%@include file="/apps/havells/global.jsp"%>
<%@ page import="com.adobe.cq.commerce.api.Product, com.adobe.cq.commerce.api.CommerceException" %>
<%
   Product product = (Product)request.getAttribute("cq.commerce.product");
   String pPath = "";
   try{
       if(product != null && product.getPIMProduct() != null){
           pPath = product.getPIMProduct().getPath();
       }
   }catch (CommerceException ex){}
%>
<c:set var="productPath" value="<%=pPath%>"/>
<div class="readOnlyrating" style="height: 20px; width: 115px; overflow: hidden; z-index: 1; position: relative;">
    <div class="jRatingColor" style="width: 0%;"></div>
    <div class="jRatingAverage" style="width: 0px; top: -20px;"></div>
    <div class="jStar" style="width: 115px; height: 20px; top: -40px; background: url(/etc/clientlibs/havells/image/stars.png) repeat-x;"></div>
</div>
<c:if test="${not empty productPath}">
    <script>
        jQuery(document).ready(function() {
            jQuery('.rateProductWrapper').displayRating({"productPath" : "${productPath}"});
        });
    </script>
</c:if>
