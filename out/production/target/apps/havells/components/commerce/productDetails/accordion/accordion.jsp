<%@ include file="/apps/havells/global.jsp" %>
<c:set var="entryTitles" value="${properties.entries}"/>
<c:set var="featureClass" value=""/>
<c:set var="style" value='style="background: #f7f7f7; display:block;"'/>
<c:set var="display" value=''/>
<c:choose>
    <c:when test="${editMode != true}">
        <c:set var="featureClass" value="featureClicker"/>
        <c:set var="style" value=''/>
        <c:set var="display" value='noDisplay'/>
    </c:when>
</c:choose>
<div class="featuresAccWrapper">
    <c:forEach items="${entryTitles}" var="imagePath" varStatus="status">
            <div class="${featureClass}">
                <a href="javascript:;" ${style}>${imagePath}</a>
            </div>
          <div class="featureContent ${display}">
                <cq:include path="entry-${status.index}" resourceType="foundation/components/parsys" />
                </div>
            <c:if test="${editMode == true}">
                <div style="clear:both;">Add Slides Below</div>
            </c:if>
    </c:forEach>
</div>
<div style="clear:both;"></div>
