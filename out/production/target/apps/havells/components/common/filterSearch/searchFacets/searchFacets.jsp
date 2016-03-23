<%@include file="/apps/havells/global.jsp" %>
<c:set var="searchFacets"
       value="${sling:adaptTo(slingRequest,'com.havells.components.models.SearchFacetsModel')}" scope="request" />
<c:set var="path" value="${resource.path}"/>

<div id="searchFacets"></div>
<div class="priceWrapperDiv">
    <div class="refineDiv">
        <div class="priceHeading active" data-facetType="${searchFacets.facetTypeTitle}">${searchFacets.facetTypeName}<span></span></div>
        <div class="priceContent">
            <ul>
                <c:forEach items="${searchFacets.facetValuesMap}" var="childTagInfo" varStatus="loop">
                    <li>
                        <label>
                            <input type="checkbox" id="${searchFacets.facetTypeTitle}_${searchFacets.facetTypeName}-${loop.index}" onclick='addFacetToSearch(${requestScope.searchPerPageCounter},"${childTagInfo.value}","${searchFacets.facetTypeTitle}",$(this),"${path}.search", "${requestScope.etcCommercePath}")'>
                            <span>${childTagInfo.key}</span>
                        </label>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </div>
    <c:if test="${!editMode}">
        <div class="clearBoth"></div>
    </c:if>
</div>
<div class="clearBoth"></div>


