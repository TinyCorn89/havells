<%@include file="/apps/havells/global.jsp"%>
<%@ page import="java.util.UUID" %>

<div class="label stockClient" data-stock-id="${properties.stockName}"><cq:text property="stockLabel" placeholder="NSE" default="NSE"/></div>

<div class="nseValue">
    <span class="stockPrice"></span>
    <i class="fa"></i>
</div>

<c:set var="uuid" value="<%= UUID.randomUUID().toString()%>"/>

<div id="${uuid}"></div>
