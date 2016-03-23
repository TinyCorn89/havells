<%@ page import="com.havells.services.PrtCoverageNodesJsonService" %>
<%@ page import="org.apache.sling.commons.json.JSONObject" %>
<%@include file="/apps/havells/global.jsp" %>

<%
    try {
        PrtCoverageNodesJsonService nodesJsonService = sling.getService(PrtCoverageNodesJsonService.class);
        String suffix = slingRequest.getRequestPathInfo().getSuffix();
        response.setContentType("application/json");
        JSONObject jsonObject = nodesJsonService.getNodesInfo(resource, suffix, "havells/components/content/print-coveragre");
        response.getWriter().write(jsonObject.toString());
    }catch(Exception ex){

    }
%>