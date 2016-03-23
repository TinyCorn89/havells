<%@include file="/apps/havells/global.jsp" %>
<%@ page import="com.havells.services.SearchQuery,
    			com.havells.core.model.product.ProductFilterResultModel,
    			java.net.URLDecoder,
				org.apache.sling.commons.json.JSONObject" %>
<%
    try{
        String filters = request.getParameter("filters");
        String commercePath = request.getParameter("cpath");
        response.setContentType("application/json");
        SearchQuery productSearch = sling.getService(SearchQuery.class);
        JSONObject jsonObject;

        if(filters != null) {
            jsonObject = ProductFilterResultModel.getAllProducts(URLDecoder.decode(filters,"UTF-8"), productSearch, commercePath, resourceResolver);
            response.getWriter().write(jsonObject.toString());
        }

    }catch(Exception ex){

    }
%>

