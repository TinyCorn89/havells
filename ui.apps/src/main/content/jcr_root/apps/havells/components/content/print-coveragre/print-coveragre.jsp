<%@include file="/apps/havells/global.jsp"%>
<%@page session="false"%>
<%@page import="com.havells.core.model.CustomDateFormatter" %>

<li>
    <h3>${properties['pubtitle']}</h3>
    <p>Publication: <strong>${properties['pubsubtitle']}</strong></p>
    <c:set var = "pubdate" value = "${properties['publicationdate']}" />
    <p>Date: <c:if test = "${not empty pubdate}"><strong>
        <%= new CustomDateFormatter().convertToPublicationDate((String)pageContext.getAttribute("pubdate")) %></strong>
       </c:if>
    </p>
    <p>${properties['description']}</p>
    <cq:include path = "cta" resourceType = "havells/components/common/cta" />
</li>

<div style="clear: both"/>
