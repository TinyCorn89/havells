<%@ page import="com.havells.core.model.Multifield,com.day.cq.wcm.api.WCMMode,
                    com.day.cq.wcm.api.components.IncludeOptions" %>
<%@include file="/apps/havells/global.jsp"%>
<c:set var="multifield" value="<%=new Multifield(resource)%>"/>
<c:set var="oLinks" value="${multifield.list}"/>
<%
    //This code will only add the surrounding DIVs for the editbars when in EDIT mode only
    if (WCMMode.fromRequest(request) != WCMMode.EDIT && WCMMode.fromRequest(request) != WCMMode.DESIGN) {
            IncludeOptions.getOptions(request, true).setDecorationTagName("");
    }
%>
<ul>
    <c:choose>
          <c:when test="${oLinks != null}">
              <c:forEach var="linkObj" items="${oLinks}">
                   <li><a href="${linkObj.path}">${linkObj.link}</a></li>
              </c:forEach>
          </c:when>
   </c:choose>
</ul>