<%@ page import="java.util.UUID" %>
<%@ page import="com.havells.commons.sling.Resources" %>
<%@include file="/apps/havells/global.jsp" %>
<c:set var="currentPath" value="<%=currentPage.getPath()%>"/>
<c:set var="path" value="${resource.path}"/>

<c:set var="uuid" value="<%= UUID.randomUUID().toString()%>"/>
<c:set var="perPageCounter" value="${properties.perPageCounter eq null ? 6 : properties.perPageCounter}"/>
<c:set var="paginationAtTop" value="${properties.paginationAtTop}"/>
<c:set var="paginationAlignment" value="${properties.paginationAlign}"/>
<c:set var="margin45" value="margin-left:45%"/>
<c:set var="alignLeftRight" value="float:${paginationAlignment}"/>

<div class="garmentWrapper">
    <ul id="productResults" class="garmentDiv"></ul>
</div>
<input type="hidden" id="resPath" name="resPath" value="${path}"/>
<input type="hidden" id="productListPageCounter" name="productListPageCounter" value="${perPageCounter}"/>
<input type="hidden" id="previous" name="previous" value="Previous"/>
<input type="hidden" id="next" name="next" value="Next"/>

<div id="${uuid}"></div>

<script type="text/javascript">
    jQuery(document).ready(function () {
        var currentPageTitle = getCurrentPageTitle();
        var cookieJSON = getJSONCookie(currentPageTitle);
        if(cookieJSON !== "" && cookieJSON.pageTitle === currentPageTitle){
             repopulateCheckboxes(cookieJSON);
        }else{
             initProductList();
        }
        function initProductList(){
            jQuery('#${uuid}').categoryListPagination({
                    url: $("#resPath").val()+".json",
                    maxResults: '${perPageCounter}',
                    templateId: "productListTemplate",
                    searchResultId: "productResults",
                    isProductList: true
             });
             showCompareAlerts();
        }
    });
</script>

<div style="clear:both;"></div>
<br/>

<div class="paginationWrapper">
    <ul class="Pagination" style=" ${paginationAlignment eq 'center' ? margin45 : alignLeftRight}"></ul>
</div>
