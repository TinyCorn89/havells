<%@include file="/apps/havells/global.jsp" %>
<cq:include path="${headerPath}/breadCrumb" resourceType="havells/components/common/breadCrumb"/>
<div class="spotlightWrapper">
    <div class="slick-slider slick-initialized">
        <cq:include path="page-par" resourceType="foundation/components/parsys"/>
    </div>
</div>
<div class="categoryListingWrapper">
    <div class="container">
        <div class="master">
            <div class="expandAcc">Expand</div>
            <div class="twoColLeft">
                <cq:include path="leftNav" resourceType="havells/components/common/leftNav"/>
                <cq:include path="leftPar" resourceType="foundation/components/iparsys"/>
                <cq:include path="searchContainer" resourceType="havells/components/common/filterSearch/searchContainer"/>
            </div>
            <div class="twoColRight">
                <div id="loading-image"></div>
                <cq:include path="productCompare" resourceType="foundation/components/iparsys"/>
                <cq:include path="rightPar" resourceType="foundation/components/parsys"/>
            </div>
        </div>
    </div>
</div>
<cq:include script="/apps/havells/components/page/base/dust-templates/search/productSearchTemplate.jsp"/>
<cq:include script="/apps/havells/components/page/base/dust-templates/search/compareTemplate.jsp"/>
<input type="hidden" id="isProductListTemplate" name="isProductListTemplate" value="true"/>
<div style="clear:both;"></div>
