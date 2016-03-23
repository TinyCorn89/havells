<%@include file="/apps/havells/global.jsp" %>
<cq:include path="${headerPath}/breadCrumb" resourceType="havells/components/common/breadCrumb"/>
<div class="spotlightWrapper">
    <div class="slick-slider slick-initialized">
        <cq:include path="page-par" resourceType="foundation/components/parsys"/>
    </div>
</div>

<c:set var="filter" value="<%=slingRequest.getRequestPathInfo().getSelectors()%>"/>

    <div class="categoryListingWrapper">
        <div class="container">
            <div class="master">
                <div class="expandAcc">Expand</div>
                <div class="twoColLeft">
                    <cq:include path="leftNav" resourceType="havells/components/common/leftNav"/>
                    <cq:include path="leftPar" resourceType="foundation/components/iparsys"/>
                </div>
                <div class="twoColRight">
                    <div id="loading-image"></div>
                    <cq:include path="productCompare" resourceType="foundation/components/iparsys"/>
                    <cq:include path="searchHolder" resourceType="havells/components/common/filterSearch/productSearchHolder"/>
            <c:if test="${fn:length(filter) < 1}">
                    <cq:include path="rightPar" resourceType="foundation/components/parsys"/>
            </c:if>
                </div>
            </div>
        </div>
    </div>
<cq:include script="/apps/havells/components/page/base/dust-templates/search/productSearchTemplate.jsp"/>
<cq:include script="/apps/havells/components/page/base/dust-templates/search/compareTemplate.jsp"/>
<input type="hidden" id="isProductListTemplate" name="isProductListTemplate" value="false"/>
<script type="text/javascript">
    jQuery(document).ready(function () {
        var currentPageTitle = getCurrentPageTitle();
        var cookieJSON = getJSONCookie(currentPageTitle);
        if (cookieJSON !== "" && cookieJSON.pageTitle === currentPageTitle) {
            repopulateCheckboxes(cookieJSON);
        }
    });
</script>
