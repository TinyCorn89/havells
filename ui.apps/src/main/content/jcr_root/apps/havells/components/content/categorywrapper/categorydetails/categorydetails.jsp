<%@include file="/apps/havells/global.jsp"%>
<c:set var ="position" value ="${(properties.textPosition=='right')? 'floatRight' :''}" />
<li>
    <div class="col ${position}">
        <div class="txtWrap">  ${properties.heading}  ${properties.description}
            <cq:include path="buttonWrapper" resourceType="havells/components/common/cta"/>
        </div>
    </div>
    <div class="col">
        <c:choose>
            <c:when test="${properties.linkURL !=null}">
                <a href='${properties.linkURL}'>
                    <img alt="${properties.alt}" src="${properties.fileReference}">
                </a>
            </c:when>
            <c:otherwise>
                <img alt="${properties.alt}" src="${properties.fileReference}">
            </c:otherwise>
        </c:choose>
    </div>
</li>

<c:if test="${editMode}">
    <div style="clear:both;"></div>
</c:if>

