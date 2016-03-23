<%@ page import="java.util.UUID" %>
<%@include file="/apps/havells/global.jsp"%>
<c:set var="filters" value="${properties.filter}"/>
<c:set var="types" value="${properties.type}"/>
<c:set var="categories" value="${properties.category}"/>
<c:set var="rowCount" value="${properties.rowNumber}" />
<c:set var="totalItems" value="${properties.items}" />

<c:if test="${properties.column=='two-col'}">
    <c:set var="colcount" value="2" />
</c:if>
<c:if test="${properties.column=='three-col'}">
    <c:set var="colcount" value="3" />
</c:if>
<c:if test="${properties.column=='four-col'}">
    <c:set var="colcount" value="4" />
</c:if>
<c:if test="${properties.column=='five-col'}">
    <c:set var="colcount" value="5" />
</c:if>

<c:set var ="margin" value ="${properties.margin}" />
<c:set var ="width" value ="${((100-(colcount*margin))/colcount)}" />


<div class="mediaVideoWrapper">
    <div class="dropDownWrapper">
    <select class="typeFilter">
        <option>Select Type</option>
        <c:forEach items="${types}" var="type" varStatus="status">
            <option>${type}</option>
        </c:forEach>
    </select>
    <select class="categoryFilter">
        <option>Select Category</option>
        <c:forEach items="${categories}" var="category" varStatus="status">
            <option>${category}</option>
        </c:forEach>
    </select>
    <select class="mediaFilter">
        <option>Select Year</option>
        <c:forEach items="${filters}" var="imagePath" varStatus="status">
            <option>${imagePath}</option>
        </c:forEach>
	</select>
        </div>
    <c:choose>
        <c:when test="${editMode}">
            <ul>
                <c:forEach  begin="1" end="${rowCount}" varStatus="row" >
                    <c:forEach  begin="1" end="${colcount}" varStatus="column" >
                        <div class="${properties.column} " style="margin-left:${margin}%; width: ${width}%">
                            <cq:include path="list${row.index}${column.index}" resourceType="havells/components/content/medialist" />
                        </div>
                    </c:forEach>
                </c:forEach>
            </ul>
        </c:when>
        <c:otherwise>
             <ul>
                 <div id="searchResults"></div>
             </ul>
        </c:otherwise>
    </c:choose>
</div>
<div class="paginationWrapper"><ul id="Pagination"></ul></div>
<input type="hidden" id="previous" name="previous" value="Previous"/>
<input type="hidden" id="next" name="next" value="Next"/>
<c:if test="${!editMode}">
    <c:set var="path" value="${resource.path}"/>
    <c:set var="uuid" value="<%= UUID.randomUUID().toString()%>"/>
    <div id="${uuid}"></div>
    <script type="text/html" id ="mediaTemplate">
        <ul>
            <li>
                <a href="{linkUrl}" class="{mediaClass}">
                    <img src="{imagePath}" style="width:100%;">
                    <span class="playIcon"></span>
                    <span><i class="fa fa-plus-circle"></i>{label}</span>
                </a>
            </li>
        </ul>
    </script>
    <script>
        jQuery(document).ready(function () {
            var mediaWrapper = jQuery('#${uuid}').closest('.media-wrapper');

            initPagination();
            function initPagination(){

                var suffix = makeSuffix();

                mediaWrapper.doPagination({
                    url:"${path}.data.json"+suffix,
                    maxResults : '${totalItems}',
                    templateId:"mediaTemplate",
                    searchResultId:"searchResults"
                });
            }
            function makeSuffix(){
                var query = "";

                var typeFilter = mediaWrapper.find(".typeFilter");
                var categoryFilter = mediaWrapper.find(".categoryFilter");
                var mediaFilter = mediaWrapper.find(".mediaFilter");

                if( typeFilter.val() != "Select Type"){
                    query += "/" +typeFilter.val();
                }
                if( categoryFilter.val() != "Select Category"){
                    query += "/" + categoryFilter.val();
                }
                if( mediaFilter.val() != "Select Year"){
                    query += "/" + mediaFilter.val();
                }

                return query;
            }

            mediaWrapper.find("select").change(function(){
                initPagination();
            });
        });
    </script>
</c:if>
<div style="clear:both;"></div>