<%@ page import="com.havells.core.model.Infrastructure" %>
<%@include file="/apps/havells/global.jsp"%><%
%><%@page session="false" %>

<c:set var="list" value="<%=new Infrastructure(resource).getList()%>"/>
<div class="cardRow">
    <div class="col" >
        <img alt="${properties.alt}" src="${properties.fileReference}" style="width: 100%;height: 100%;">
    </div>
    <div class="col infraBg" >
        <div class="sliderWrapper">
            ${properties.heading}
            ${properties.description}
            <c:choose>
                <c:when test="${not empty list}">
                    <div class="infraRow">
                        <ul>
                            <c:forEach items="${list}" var="imagePath">
                                <li>
                                    <div class="numbers">${imagePath.number}</div>
                                    <div class="label">${imagePath.label}</div>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:when>
            </c:choose>
        </div>
    </div>
</div>

<c:if test="${editMode}">
    <div style="clear:both;"></div>
</c:if>


