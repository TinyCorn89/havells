<%@include file="/apps/havells/global.jsp"%>
<%@ page import="com.havells.core.model.Links" %>
<c:set var="tabs" value="<%=new Links(resource)%>"/>
<div id="currentNodePath" class="${currentNode.path}"></div>
<div class="tabWrapperGlbl">
    ${properties.title}
    <div id="parentHorizontalTab">
        <div class="topTabs">
            <ul class="resp-tabs-list hor_1">
                <c:choose>
                    <c:when test="${not empty tabs}">
                        <c:forEach var="tab" items="${tabs.list}" varStatus="counter">
                            <li id="tabber${counter.index}" class="horizontalTabs">${tab.link}</li>
                        </c:forEach>
                    </c:when>
                </c:choose>
            </ul>
        </div>
        <div class="resp-tabs-container hor_1">
            <c:choose>
                <c:when test="${not empty tabs}">
                    <c:forEach var="tab" items="${tabs.list}" varStatus="counter">
                        <div  id="content${counter.index}" class="genericTabContent">
                            <cq:include path="tabContent${counter.index}" resourceType="foundation/components/parsys" />
                        </div>
                    </c:forEach>
                </c:when>
            </c:choose>
        </div>
    </div>
</div>
<div style="clear:both;"></div>
<script>
    jQuery(document).ready(function() {
        if(${editMode}){
            jQuery('.tabsComponent').toggleAndHideParsys({"noOfParsys" : "${fn:length(tabs.list)}"});
        } else{
            jQuery('.tabsComponent').toggleComponentTabs();
        }
    });
</script>


