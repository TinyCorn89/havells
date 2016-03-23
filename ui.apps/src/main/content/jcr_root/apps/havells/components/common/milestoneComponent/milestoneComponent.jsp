<%@include file="/apps/havells/global.jsp" %>
<%@ page import="com.havells.core.model.Links" %>
<c:set var="tabs" value="<%=new Links(resource)%>"/>
<div id="currentNodePath" class="${currentNode.path}"></div>
${properties.title}
<div class="milestoneWrapper">
    <div class="milestoneYears">${properties.expandText}</div>
    <div class="milestoneTab">
        <ul>
            <c:choose>
                <c:when test="${not empty tabs}">
                    <c:forEach var="tab" items="${tabs.list}" varStatus="counter">
                        <li><a id="tabber${counter.index}" href="javascript:;">${tab.link}</a></li>
                    </c:forEach>
                </c:when>
            </c:choose>
        </ul>
    </div>
    <div class="Wrapper100Pre">
        <c:choose>
            <c:when test="${not empty tabs}">
                <c:forEach var="tab" items="${tabs.list}" varStatus="counter">
                    <div  id="content${counter.index}" class="milestoneContent noDisplay">
                        <cq:include path="tabContent${counter.index}" resourceType="foundation/components/parsys" />
                    </div>
                </c:forEach>
            </c:when>
        </c:choose>
    </div>
</div>
<div style="clear:both;"></div>
<script>
    jQuery(document).ready(function () {
        jQuery('.milestoneComponent').toggleMilestoneTabs({"noOfParsys" : "${fn:length(tabs.list)}"});
    });
</script>


