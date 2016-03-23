<%@ page import="com.havells.core.model.Multifield" %>
<%@include file="/apps/havells/global.jsp"%>
<c:set var="links" value="<%=new Multifield (resource)%>"/>
<c:set var="list" value="${links.list}"/>
<c:set var ="display" value ="${(properties.display=='V')? 'display:block;' :'display:inline; margin-right:5px;'}" />
<c:if test="${properties.newWindow == 'true'}">
    <c:set var="target" value="target='_blank'"></c:set>
</c:if>

<c:choose>
    <c:when test="${not empty list}">
        <c:forEach items="${list}" var="imagePath">
            <c:choose>
                <c:when test="${imagePath.icon != 'none'}">
                    <a ${target} style="${display}" href='<c:out value="${imagePath.path}" />'>
                        <i class="fa fa-${imagePath.icon} ${imagePath.iconsize}"></i>
                        <c:out value="${imagePath.link}" />
                    </a>
                </c:when>
                <c:otherwise>
                    <a ${target} style="${display}" href='<c:out value="${imagePath.path}" />'>
                        <c:out value="${imagePath.link}" />
                    </a>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </c:when>

    <c:otherwise>
        <c:out value="Add Links." />
    </c:otherwise>
</c:choose>

