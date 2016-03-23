<%@include file="/libs/foundation/global.jsp" %>
<%@ page import="com.day.cq.wcm.foundation.Download" %>
<%@page session="false" %>
<c:set var="path" value="<%=new Download(resource).getHref() %>"/>
<div class="guptaWrapper mediaWrapperBorder">
    <div class="dateLeft">
        <cq:include path="left" resourceType = "foundation/components/parsys" />
    </div>
    <div class="guptaDiv mediaDiv">
        <cq:include path="par" resourceType = "foundation/components/parsys" />
    </div>
    <div class="rightPdf">
        <cq:include path="right" resourceType = "foundation/components/parsys" />
    </div>
</div>

<div class="clearfix"></div>




