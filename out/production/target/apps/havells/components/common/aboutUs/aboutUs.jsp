<%@ page import="com.day.text.Text,
                 com.day.cq.commons.Doctype,
                 com.day.cq.wcm.api.PageFilter,
                 com.day.cq.wcm.foundation.Navigation" %>
<%@include file="/apps/havells/global.jsp" %>
<% String fileRef = properties.get("image/fileReference", ""); %>
<c:set var="fileRef" value="<%= fileRef %>"/>
<c:choose>
    <c:when test="${editMode==true}">
        Name = ${properties.name} <br/>
        Designation = ${properties.designation}
    </c:when>
    <c:otherwise>
        <c:if test="${fileRef!=''}">
            <div class="img">
                <img src="${fileRef}" alt="">
            </div>
        </c:if>
        <div class="label">
            <div class="name">${properties.name} <span>${properties.designation}</span></div>
            <div class="viewProfile"><a href="${properties.link}.html">View Profile <i class="fa  fa-angle-right"></i></a></div>
        </div>
    </c:otherwise>
</c:choose>