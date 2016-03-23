<%@include file="/apps/havells/global.jsp" %>

<c:if test="${properties.column=='one-col'}">
    <c:set var="colcount" value="1"/>
</c:if>
<c:if test="${properties.column=='two-col'}">
    <c:set var="colcount" value="2"/>
</c:if>
<c:if test="${properties.column=='three-col'}">
    <c:set var="colcount" value="3"/>
</c:if>
<c:if test="${properties.column=='four-col'}">
    <c:set var="colcount" value="4"/>
</c:if>
<c:if test="${properties.column=='five-col'}">
    <c:set var="colcount" value="5"/>
</c:if>
<c:set var="margin" value="${properties.margin}"/>
<c:set var="width" value="${((100-(colcount*margin))/colcount)}"/>
<div class="cardRow">
    <c:forEach begin="1" end="${colcount}" varStatus="t">
        <div class="${properties.column} col" style="margin-left:${margin}%; width: ${width}%">
            <cq:include path="par_${t.index}" resourceType="foundation/components/parsys"/>
        </div>
    </c:forEach>
</div>
<div style="clear:both;height:0px;"></div>