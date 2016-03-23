<%@ page import="org.apache.sling.commons.json.JSONObject" %>
<%@ page import="com.havells.services.CategoryListService" %>
<%@include file="/libs/foundation/global.jsp" %>

<%
    try {
        CategoryListService categoryListService = sling.getService(CategoryListService.class);
        response.setContentType("application/json");
        JSONObject jsonObject = categoryListService.getCategoriesInfo(resource, currentPage);
        response.getWriter().write(jsonObject.toString());
    }catch(Exception ex){
        response.getWriter().write("failed");
    }
%>