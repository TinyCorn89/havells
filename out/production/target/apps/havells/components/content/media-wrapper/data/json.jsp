<%@ page import="com.havells.services.NodesJsonService" %>
<%@ page import="org.apache.sling.commons.json.JSONObject" %>
<%@include file="/libs/foundation/global.jsp" %>
<%
    try {
        NodesJsonService nodesJsonService = sling.getService(NodesJsonService.class);
        String suffix = slingRequest.getRequestPathInfo().getSuffix();
        response.setContentType("application/json");
        if(suffix == null){
            suffix = "/all";
        }
        JSONObject jsonObject = nodesJsonService.getNodesInfo(resource, suffix, "havells/components/content/medialist");
        response.getWriter().write(jsonObject.toString());
    }catch(Exception ex){
    }
%>
