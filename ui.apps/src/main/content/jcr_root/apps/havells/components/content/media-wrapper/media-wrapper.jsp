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

<c:set var="defaultCategoryMessage" value="Select Category"/>
<c:set var="defaultTypeMessage" value="Select Type"/>
<c:set var="defaultYearMessage" value="Select Year"/>

<c:set var ="margin" value ="${properties.margin}" />
<c:set var ="width" value ="${((100-(colcount*margin))/colcount)}" />
<c:set var ="categoryFilterMessage" value ="${properties.categoryFilterMessage}" />
<c:set var ="typeFilterMessage" value ="${properties.typeFilterMessage}" />
<c:set var ="yearFilterMessage" value ="${properties.yearFilterMessage}" />
<c:set var ="categoryMessage" value ="${categoryFilterMessage eq null or empty categoryFilterMessage ? defaultCategoryMessage : categoryFilterMessage }"/>
<c:set var ="typeMessage" value ="${typeFilterMessage eq null or empty typeFilterMessage ? defaultTypeMessage : typeFilterMessage }"/>
<c:set var ="yearMessage" value ="${yearFilterMessage eq null or empty yearFilterMessage ? defaultYearMessage : yearFilterMessage }"/>

<div class="mediaVideoWrapper">
    <div class="dropDownWrapper">
        <c:if test="${not empty types}">
            <select class="typeFilter">
                <option>${typeMessage}</option>
                <c:forEach items="${types}" var="type" varStatus="status">
                    <option>${type}</option>
                </c:forEach>
            </select>
        </c:if>
        <c:if test="${not empty categories}">
            <select class="categoryFilter">
                <option>${categoryMessage}</option>
                <c:forEach items="${categories}" var="category" varStatus="status">
                    <option>${category}</option>
                </c:forEach>
            </select>
        </c:if>
        <c:if test="${not empty filters}">
            <select class="mediaFilter">
                <option>${yearMessage}</option>
                <c:forEach items="${filters}" var="imagePath" varStatus="status">
                    <option>${imagePath}</option>
                </c:forEach>
            </select>
        </c:if>
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

                if( typeFilter.val() != undefined && typeFilter.val() != "${typeMessage}"){
                    query += "/" +typeFilter.val();
                }
                if( categoryFilter.val() != undefined && categoryFilter.val() != "${categoryMessage}"){
                    query += "/" + categoryFilter.val();
                }
                if( mediaFilter.val() != undefined && mediaFilter.val() != "${yearMessage}"){
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