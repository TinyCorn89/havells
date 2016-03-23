<%@ page import="com.day.text.Text,
                 com.day.cq.commons.Doctype,
                 com.day.cq.wcm.api.PageFilter,
                 com.day.cq.wcm.foundation.Navigation" %>
<%@include file="/apps/havells/global.jsp" %>

<c:set var="searchPerPageCounter" value="${properties.searchPerPageCounter eq null ? 6 : properties.searchPerPageCounter}" scope="request"/>
<c:set var="etcCommercePath" value="${properties.commercePath eq null ? '/etc/commerce/products/havells' : properties.commercePath}" scope="request"/>

<c:if test="${not empty properties.filterHeading}">
    <div class="refineWrapper">
        <div class="refineHeading active">${properties.filterHeading}<span></span> </div>
        <cq:include path="priceRange" resourceType="havells/components/common/filterSearch/priceRange"/>
        <cq:include path="searchPar" resourceType="foundation/components/parsys"/>
    </div>
</c:if>
<cq:include script="/apps/havells/components/page/base/dust-templates/search/productSearchTemplate.jsp"/>
<cq:include script="/apps/havells/components/page/base/dust-templates/search/compareTemplate.jsp"/>
<div class="clearBoth"></div>
