<%@include file="/apps/havells/global.jsp"%>
 <c:set var="richTextValue"   value="<%=properties.get("richtext","")%>" />
<c:choose>
    <c:when test = "${richTextValue eq ''}">
        <cq:text  tagName="h4" placeholder="<%=(currentPage.getTitle() == null) ? currentPage.getName() : currentPage.getTitle()%>" />
    </c:when>
    <c:otherwise>
        ${richTextValue}
    </c:otherwise>
</c:choose>
