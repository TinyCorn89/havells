<%@include file="/apps/havells/global.jsp" %>
<%@page session="false"%>
<cq:include path="${headerPath}/breadCrumb" resourceType="havells/components/common/breadCrumb"/>
<cq:include path="par" resourceType="foundation/components/parsys"/>
<div class="featuresWrapper">
    <div class="container">
        <div class="master">
            <div class="featuresDiv">
                <div class="featuresLeft">
                    <cq:include path="leftPar" resourceType="foundation/components/parsys"/>
                 </div>
                 <div class="featuresRight">
                     <cq:include path="rightPar" resourceType="foundation/components/parsys"/>
                 </div>
            </div>
        </div>
    </div>
</div>
