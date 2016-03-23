<%@ page import="com.havells.core.model.QuickLinks" %>
<%@include file="/apps/havells/global.jsp"%>
<c:set var="links" value="<%=new QuickLinks(resource)%>"/>
<c:set var="list" value="${links.links}"/>
<div class="quickLinkWrapper" style="height: 70px; right: -70px;">
    <ul>
        <li class="trigger" style="margin-left: 150px;">
            <div class="icon"><i class="fa fa-plus"></i></div>
            <div class="label">${properties.quickLinkTitle}</div>
        </li>
        <c:choose>
            <c:when test="${not empty list}">
                <c:forEach items="${list}" var="imagePath">
                    <li style="margin-left: 150px;">
                        <a href='${imagePath.path}'>
                            <div class="icon"><i class="fa ${imagePath.icon}"></i></div>
                            <div class="label">${imagePath.link}</div>
                        </a>
                    </li>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <li><c:out value="Add Links." /></li>
            </c:otherwise>
        </c:choose>
    </ul>
</div>