<%@include file="/apps/havells/global.jsp" %>

<cq:include path="${headerPath}/breadCrumb" resourceType="havells/components/common/breadCrumb"/>

<div class="spotlightWrapper">
    <div class="slick-slider slick-initialized">
        <cq:include path="par" resourceType="foundation/components/parsys"/>
    </div>
</div>
<div class="categoryListingWrapper">
    <div class="container">
        <div class="master">
            <div class="expandAcc">Expand</div>
                <div class="twoColLeft">
                    <cq:include path="leftNav" resourceType="havells/components/common/leftNav"/>
                    <cq:include path="leftPar" resourceType="foundation/components/parsys"/>
                </div>
                <div class="twoColRight">
                    <cq:include path="rightPar" resourceType="foundation/components/parsys"/>
                </div>
                <div style="clear:both"></div>
                <cq:include path="lowerPar" resourceType="foundation/components/parsys"/>
            </div>
      </div>
</div>
