<%@page import="javax.jcr.*,
        org.apache.sling.api.resource.Resource,
        org.apache.sling.api.resource.ValueMap,
        com.day.cq.commons.inherit.InheritanceValueMap,
        com.day.cq.wcm.commons.WCMUtils,
        com.day.cq.wcm.api.Page,
        com.day.cq.wcm.api.NameConstants,
        com.day.cq.wcm.api.PageManager,
        com.day.cq.wcm.api.designer.Designer,
        com.day.cq.wcm.api.designer.Design,
        com.day.cq.wcm.api.designer.Style,
        com.day.cq.wcm.api.components.ComponentContext,
        com.day.cq.wcm.api.WCMMode,
        com.day.cq.wcm.api.components.EditContext"
%>
<%@ page import="com.day.cq.i18n.I18n" %>
<%@ page import="com.havells.util.GlobalPageWrapper" %>
<%@taglib prefix="sling" uri="http://sling.apache.org/taglibs/sling" %><%
%><%@taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %><%
%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%
%><%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %><%
%><%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%
%><cq:defineObjects />

<% GlobalPageWrapper globalPageWrapper = new GlobalPageWrapper(currentPage); %>
 <%
    String globalPage = globalPageWrapper.getGlobalPage();
    String footerPath = globalPage + "/_jcr_content/fpar/footer";
    String headerPath = globalPage + "/_jcr_content";
%>

<c:set var="wcmMode" value="<%= WCMMode.fromRequest(request).toString() %>"/>
<c:set var="editMode" value="<%= WCMMode.fromRequest(request) == WCMMode.EDIT || WCMMode.fromRequest(request) == WCMMode.DESIGN %>"/>
<c:set var="disabledMode" value="<%= WCMMode.fromRequest(request) == WCMMode.DISABLED %>"/>
<c:set var="footerPath" value="<%=footerPath%>"/>
<c:set var="headerPath" value="<%=headerPath%>"/>
<% final I18n i18n = new I18n(slingRequest.getResourceBundle(currentPage.getLanguage(false))); %>
