<%@include file="/apps/havells/global.jsp"%><%
%><%@page session="false" %><%
%>

<%
    String bgcolor = "";
    String color = properties.get("bgcolor", "#E0E0E0");
    if (color.equals("#0000FF")) {
        bgcolor = "bluebgcolor";
    }else if (color.equals("#000000")) {
        bgcolor = "blackbgcolor";
    }else {
        bgcolor = "greybgcolor";
    }
%>

<div class=<%= bgcolor%>>
    Edit Component.
    <cq:include path="content-par" resourceType="foundation/components/parsys" />

</div>
