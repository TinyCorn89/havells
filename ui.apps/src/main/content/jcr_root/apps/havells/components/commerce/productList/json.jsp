<%@page session="false"%>
<%@ page import="org.apache.sling.commons.json.JSONObject" %>
<%@ page import="com.havells.services.ProductListService" %>
<%@ page import="com.adobe.cq.commerce.api.CommerceSession" %>
<%@ page import="com.adobe.cq.commerce.api.CommerceService" %>
<%@include file="/libs/foundation/global.jsp" %>

<%
    try {
        CommerceService commerceService = resource.adaptTo(CommerceService.class);
        ProductListService productListService = sling.getService(ProductListService.class);
        response.setContentType("application/json");
        CommerceSession session = commerceService.login(slingRequest, slingResponse);
        JSONObject jsonObject = productListService.getProductsInfo(resource, currentPage, session);
        response.getWriter().write(jsonObject.toString());
    }catch(Exception ex){
       ex.printStackTrace();
    }
%>