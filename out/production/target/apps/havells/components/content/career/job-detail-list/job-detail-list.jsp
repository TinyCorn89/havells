<%@ page import="java.util.UUID" %>
<%@include file="/apps/havells/global.jsp" %>

<div class="currentOpporWrap" id="jobSearchResults">

</div>
<%@include file="job-template.jsp" %>
<c:set var="noOfPage" value="${properties.noOfPage}"/>
<c:set var="path" value="<%= currentPage.getPath()%>"/>
<c:set var="uuid" value="<%= UUID.randomUUID().toString()%>"/>
<div id="${uuid}"></div>
<script>
    $(document).ready(function () {
        jQuery('#${uuid}').jobListPagination({
            url: "/bin/servlets/JobListingServlet?path="+"${path}",
            maxResults: ${noOfPage},
            templateId: "jobListTemplate",
            searchResultId: "jobSearchResults"
        });
    });
</script>

<c:if test="${editMode}">
    <div style="clear:both;"></div>
</c:if>
<div class="paginationWrapper">
    <ul id="jobPagination"></ul>
</div>
<input type="hidden" id="previous" name="previous" value="Previous"/>
<input type="hidden" id="next" name="next" value="Next"/>
