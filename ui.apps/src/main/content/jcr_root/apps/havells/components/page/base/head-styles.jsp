<%@include file="/apps/havells/global.jsp"%>
<link rel="shortcut icon" href="/etc/clientlibs/havells/image/favicon.ico">
<%-- CQ pull in dynamic CSS as needed --%>
<cq:includeClientLib css="havells.parent"/>
<c:if test="${editMode}">
    <cq:includeClientLib css="havells.components.author"/>
</c:if>


