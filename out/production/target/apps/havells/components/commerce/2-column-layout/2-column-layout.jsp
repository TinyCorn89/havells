<%@include file="/libs/foundation/global.jsp" %><%
%><%@ page import="com.day.cq.wcm.api.WCMMode" %>

<%
    String columnLayout = properties.get("columnlayout", "");
    String firstParsysClass = "cq-colctrl-lt0-c0";
    String secondParsysClass = "cq-colctrl-lt0-c1";
//    if(columnLayout.equals("50-50"))
//       firstParsysClass = secondParsysClass = "col-sm-6";
%>
<div class="row cq-colctrl-lt0">
    <div class="<%=firstParsysClass%>">
        <cq:include path="parl" resourceType="foundation/components/parsys" />
    </div>
    <div class="<%=secondParsysClass%>">
        <cq:include path="parr" resourceType="foundation/components/parsys" />
    </div>
</div>
