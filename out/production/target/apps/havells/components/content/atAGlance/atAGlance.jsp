<%@include file="/apps/havells/global.jsp" %>
<%@page session="false" %>
<c:choose>
    <c:when test="${properties.title=='' || properties.title==null}">
        <p>Enter Slide Details</p>
    </c:when>
</c:choose>
<div class="havellsAtGlance">
    <h2>${properties.title}</h2>
    <div class="subHead">${properties.subTitle}</div>
    ${properties.description}
</div>
<c:if test="${editMode}" >
    <div style="clear:both;height:10px;">&nbsp;</div>
</c:if>
