<%@ page import="com.day.cq.wcm.api.WCMMode,com.day.cq.wcm.api.components.IncludeOptions" %>
<%@ page import="com.havells.core.model.OrderUnOrderList" %>
<%@include file="/apps/havells/global.jsp"%>
<c:set var="list" value="<%=new OrderUnOrderList(resource).getList()%>"/>
<%
    //This code will only add the surrounding DIVs for the editbars when in EDIT mode only
    if (WCMMode.fromRequest(request) != WCMMode.EDIT && WCMMode.fromRequest(request) != WCMMode.DESIGN) {
            IncludeOptions.getOptions(request, true).setDecorationTagName("");
    }
%>
<c:choose>
    <c:when test="${properties.ltypeCheck == 'ordered'}">
        <div class="ulWrapper">
            <ul>
                <c:choose>
                    <c:when test="${list != null}">
                        <c:forEach var="linkObj" items="${list}">
                            <li>${linkObj}</li>
                        </c:forEach>
                    </c:when>
                </c:choose>
            </ul>
        </div>
    </c:when>
    <c:otherwise>
        <div class="olWrapper">
            <ol>
                <c:choose>
                    <c:when test="${list != null}">
                        <c:forEach var="linkObj" items="${list}">
                            <li>${linkObj}</li>
                        </c:forEach>
                    </c:when>
                </c:choose>
            </ol>
        </div>
    </c:otherwise>
</c:choose>
