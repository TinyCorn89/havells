<%@ page import="com.havells.core.model.CategoryList" %>
<%@include file="/apps/havells/global.jsp" %>
<c:set var="categoryList"
       value="<%=new CategoryList(resourceResolver, properties.get("categoryListPath",currentPage.getPath()))%>"/>
<c:set var="list" value="${categoryList.categoryList}"/>
<c:set var="categoryExplore" value="${fn:trim(properties.categoryExplore)}"/>
<div class="category">${properties.categoryHeading}</div>
<div class="appliancesContainer appliancesContainer2">
    <c:set var="len" value="${fn:length(list)}" />
    <c:set var="i" value="0" scope="page"/>
    <c:if test="${fn:length(list) gt 0}">
        <c:forEach items="${list}" var="categories">
            <c:set var="lastClass" value="${len eq (i+1) ? 'newClass' :''}"/>
            <c:choose>
                <c:when test="${i%2 != 0}">
                    <div class="appliancesRight ${lastClass}">
                        <a href="${categories.link}.html">
                            <div class="applincesExplore exploreRight">
                                <div class="prep">${categories.category}</div>
                                <div class="applincesImage"><img src="${categories.imgPath}"></div>
                                <div class="exploreTxt">${empty categoryExplore ? "Explore ": properties.categoryExplore}</div>
                                <div class="appliancesCircleRight">
                                    <span class="appliancesCircle2Right"></span>
                                </div>
                            </div>
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="appliancesLeft ${lastClass}">
                        <a href="${categories.link}.html">
                            <div class="applincesExplore exploreLeft">
                                <div class="prep">${categories.category}</div>
                                <div class="applincesImage"><img src="${categories.imgPath}"></div>
                                <div class="exploreTxt">${empty categoryExplore  ? "Explore ": properties.categoryExplore}</div>
                                <div class="appliancesCircle">
                                    <span class="appliancesCircle2"></span>
                                </div>
                            </div>
                        </a>
                    </div>
                </c:otherwise>
            </c:choose>
            <c:set var="i" value="${i+1}" scope="page"/>
        </c:forEach>
    </c:if>
</div>
