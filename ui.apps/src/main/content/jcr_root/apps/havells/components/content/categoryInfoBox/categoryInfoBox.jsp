<%@include file="/apps/havells/global.jsp" %>
<%@page session="false" %>
<div class="productImage">
    <img src="${properties.fileRefer}" class="categoryImg"/>
</div>
<div class="productHover">
    <div class="productHoverCont">
        ${properties.heading}
        ${properties.description}
        <cq:include path="buttonWrapper" resourceType="havells/components/common/cta"/>
    </div>
</div>
