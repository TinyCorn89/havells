<%@ page import="com.day.text.Text,
                 com.day.cq.commons.Doctype,
                 com.day.cq.wcm.api.PageFilter,
                 com.day.cq.wcm.foundation.Navigation" %>
<%@include file="/apps/havells/global.jsp" %>

<c:set var="searchPerPageCounter" value="${properties.searchPerPageCounter eq null ? 6 : properties.searchPerPageCounter}" scope="request"/>
<c:set var="etcCommercePath" value="${properties.commercePath eq null ? '/etc/commerce/products/havells' : properties.commercePath}" scope="request"/>

<input type="hidden" id="noSearchResultMsg" name="noSearchResultMsg" value="${not empty properties.noSearchResultMsg ? properties.noSearchResultMsg : "No Search Results found"}"/>

<c:if test="${not empty properties.filterHeading}">
    <div class="refineWrapper">
        <div class="refineHeading active">${properties.filterHeading}<span></span> </div>
        <cq:include path="priceRange" resourceType="havells/components/common/filterSearch/priceRange"/>
        <cq:include path="searchPar" resourceType="foundation/components/parsys"/>
    </div>
</c:if>
<div class="clearBoth"></div>
