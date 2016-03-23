<%@include file="/apps/havells/global.jsp" %>
<head>
    <meta charset="utf-8">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="user-scalable=no, minimum-scale=1.0, maximum-scale=1.0, width=device-width, initial-scale=1.0" />
    <title><%= xssAPI.encodeForHTML( currentPage.getTitle() == null ? currentPage.getName() : currentPage.getTitle() ) %></title>
    <% currentDesign.writeCssIncludes(pageContext); %>
    <%String pagedescription = properties.get("jcr:description","");%>
    <meta name="description" content="<%=pagedescription%>">
    <meta name="author" content="">
    <meta http-equiv="keywords" content="<%= WCMUtils.getKeywords(currentPage) %>">

    <cq:include script="/libs/wcm/core/components/init/init.jsp"/>
    <cq:include script="/libs/cq/cloudserviceconfigs/components/servicelibs/servicelibs.jsp"/>

    <!-- Adobe Edge Web Fonts in use by Geo-media -->
    <cq:include script="head-styles.jsp" />
    <cq:include script="head-scripts.jsp" />

</head>
