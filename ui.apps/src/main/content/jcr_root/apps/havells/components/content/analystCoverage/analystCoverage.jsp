<%@ page import="com.havells.core.model.AnalystCoverageComponent" %>
<%@include file="/apps/havells/global.jsp" %>
<c:set var="links" value="<%=new AnalystCoverageComponent(resource)%>"/>
<c:set var="width" value="${properties.width ne null ? properties.width : 20}"/>

<div class="analystWrapper">
    <c:forEach items="${links.fields}" var="item">
        <c:set var="fieldName" value="${item.fieldName}"/>
        <c:set var="width" value="${item.fieldWidth}"/>
        <div class="fieldWrapper" style="width:${width};">
            <div class="fieldName">
                ${fieldName}
            </div>
            <cq:include path="${fieldName}" resourceType="havells/components/content/multifield" />
        </div>
    </c:forEach>
</div>
<style>
    .analystWrapper .fieldWrapper .multifield a{
        margin-bottom: ${properties.margin}px;
    }
</style>
<div class="clearBoth"></div>
