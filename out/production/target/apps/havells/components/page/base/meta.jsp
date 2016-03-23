<%-- @ include is static (precompile) include, so needs full path to file (can't use sling relative pathing/searching/inheritance --%>
<%@include file="/apps/havells/global.jsp"%>
<%@ page import="com.day.cq.commons.Doctype" %><%
    String xs = Doctype.isXHTML(request) ? "/" : "";
%>
  <!-- X-UA-Compatible must be the first tag to successfully disable IE Compatibility Mode. -->
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta charset="utf-8">
  <%String dialogpageTitle = properties.get("pageTitle","");%>
  <%-- currentPage.getTitle() == null ? currentPage.getName() : currentPage.getTitle() --%>
  <title><%=dialogpageTitle==""?currentPage.getTitle() == null ? currentPage.getName() : currentPage.getTitle():dialogpageTitle %></title>
  <%String pagedescription = properties.get("jcr:description","");%>
  <meta name="description" content="<%=pagedescription%>">
  <meta name="author" content="">

  <meta http-equiv="content-type" content="text/html; charset=UTF-8"<%=xs%>>
  <meta http-equiv="keywords" content="<%= WCMUtils.getKeywords(currentPage) %>"<%=xs%>>

  <!--Disable Pinch to Zoom meta tag script for Android OS -->
  <meta name="viewport" content="user-scalable=0, width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0">

