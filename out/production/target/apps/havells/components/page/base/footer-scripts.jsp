<%@include file="/apps/havells/global.jsp"%>
<%-- Conditional include of edit-mode javascript --%>
<cq:includeClientLib js="havells.parent"/>
<c:if test="${editMode}">
    <cq:includeClientLib js="havells.components.author"/>
</c:if>
