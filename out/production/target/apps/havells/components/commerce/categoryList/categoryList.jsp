<%@ page import="java.util.UUID" %>
<%@include file="/apps/havells/global.jsp"%>
<c:set var="currentPath" value="<%=currentPage.getPath()%>"/>

<div class="applicationWrapper">
    <cq:include path="categoryHeader" resourceType="foundation/components/parsys"/>
    <div class="applicationProducts">
        <ul id="searchResults"> </ul>
    </div>
</div>
   <c:set var="path" value="${resource.path}"/>
   <c:set var="uuid" value="<%= UUID.randomUUID().toString()%>"/>
   <div id="${uuid}"></div>
   <script type="text/html" id ="categoryListTemplate">
        <li>
            <a href="{pagePath}">
                <div class="productName">{pageName}</div>
                <div class="productDiv"><img src="{imagePath}"></div>
                <div class="viewCategories">View Categories</div>
            </a>
        </li>
    </script>
    <script>
        jQuery(document).ready(function () {


            jQuery('#${uuid}').categoryListPagination({
                url:"${path}"+".json",
                maxResults : '9',
                templateId:"categoryListTemplate",
                searchResultId:"searchResults",
                isProductList:false
            });
        });
    </script>
<div style="clear:both;"></div><br/>
<div class="paginationWrapper"><ul id="Pagination"></ul></div>
<input type="hidden" id="previous" name="previous" value="Previous"/>
<input type="hidden" id="next" name="next" value="Next"/>
