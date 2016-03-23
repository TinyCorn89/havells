<%@ page import="java.util.UUID" %>
<%@include file="/apps/havells/global.jsp"%>
<%@ page import="com.day.cq.wcm.api.WCMMode"%>
<%@page session="false" %>
<c:set var="filters" value="${properties.filter}"/>


    <h1>${properties['title']}</h1>
    <div class="managementWrapper printCoverage">
        <select class="coverageFilter">
            <c:forEach items="${filters}" var="imagePath" varStatus="status">
                <option>${imagePath}</option>
            </c:forEach>
        </select>
        <div class="managementProfile">
            <c:choose>
                <c:when test="${editMode}">
                    <ul>
                        <cq:include path = "prtcoverage" resourceType = "foundation/components/parsys" />
                    </ul>
                </c:when>
                <c:otherwise>
                    <ul>
                        <div id="searchResults"></div>
                        <div style="display: none;color: #ff0000" id="error-msg">${properties.errmsg}</div>
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
            <script type="text/html" id ="publisherTemplate">
                <ul>
                    <li>
                        <h3>{pubtitle}</h3>
                        <p>Publication: <strong>{pubsubtitle}</strong></p>
                        <p>Date: <strong>{publicationdate}</strong></p>
                        {description|s}
                        <div class="buttonGlbl {color}">
                            <a target="{newWindow}" href="{url}" class="  ">
                                <span><i class="{linkType}"></i> {textOverlay}</span>
                            </a>
                        </div>
                        <div class="clearBoth"></div>
                    </li>
                </ul>
            </script>
            <script>
                jQuery(document).ready(function () {
                    initPagination();
                    function initPagination(){
                        console.log("inside pagination");
                        jQuery('#${uuid}').closest('.printCoverage').doPaginationPrtCoverage({
                            url:"${path}.data.json/"+$(".coverageFilter").val(),
                            maxResults : "${properties['pagination']}",
                            templateId:"publisherTemplate",
                            searchResultId:"searchResults"
                        });
                    }
                    jQuery('#${uuid}').closest('.printCoverage').find(".coverageFilter").change(function(){
                        initPagination();
                    });
                });

            </script>
        </c:if>
    </div>

<div style="clear: both"/>
