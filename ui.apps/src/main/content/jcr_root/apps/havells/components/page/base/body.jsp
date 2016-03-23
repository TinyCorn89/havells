<%@include file="/apps/havells/global.jsp" %><%
%><%@page session="false" %>
<body>
    <!--[if lt IE 7]>
    <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
    <![endif]-->
    <!-- Add your site or application content here -->

    <cq:include path="clientcontext" resourceType="cq/personalization/components/clientcontext"/>

    <cq:include script="header.jsp" />

    <cq:include script="content.jsp" />

    <cq:include script="footer.jsp" />
    <cq:include script="footer-scripts.jsp" />

    <cq:include path="cloudservices" resourceType="cq/cloudserviceconfigs/components/servicecomponents"/>
</body>
