<%@ page import="com.havells.core.model.AnalystCoverageComponent" %>
<%@include file="/apps/havells/global.jsp" %>
<%@page session="false" %>
<c:set var="links" value="<%=new AnalystCoverageComponent(resource)%>"/>
<c:set var="path" value="${resource.path}"/>
<c:if test="${not empty properties.title}">
    <div id="searchFacets"></div>
    <div class="priceWrapperDiv">
        <div class="refineDiv">
            <div class="priceHeading active" data-facetType="price">${properties.title}<span></span></div>
            <div class="priceContent">
                <ul>
                    <c:forEach items="${links.fields}" var="item" varStatus="loop">
                        <li>
                            <label>
                                <input type="checkbox" id="price-${loop.index}" name="price" onclick='addFacetToSearch(${requestScope.searchPerPageCounter},"${item.fieldName}-${item.fieldWidth}","${properties.title}" ,$(this),"${path}.search", "${requestScope.etcCommercePath}")'>
                                <c:choose>
                                    <c:when test="${not empty item.priceDescription}">
                                        ${item.priceDescription}
                                    </c:when>
                                    <c:otherwise>
                                        ${item.fieldName}-${item.fieldWidth}
                                    </c:otherwise>
                                </c:choose>
                                <br>
                            </label>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
</c:if>
<div class="clearBoth"></div>
