<%@ page import="com.havells.core.model.Carousel" %>
<%@include file="/apps/havells/global.jsp" %>
<% Resource container = resource.getChild("values"); %>
<c:set var="valuesContainer" value="<%= new Carousel(container) %>"/>
<c:choose>
    <c:when test="${editMode==true}">
        <cq:include path="values" resourceType="foundation/components/parsys" />
    </c:when>
    <c:otherwise>
        <h2>${properties.title}</h2>
        <div class="valuesWrapper">
            <c:forEach items="${valuesContainer.childList}" var="slide" varStatus="status">
                <c:choose>
                    <c:when test="${((status.index+1) % 2)==0}">
                        <c:set var="secondClass" value="noMarginRight" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="secondClass" value="" />
                    </c:otherwise>
                </c:choose>
                <div class="values ${secondClass}">
                    <sling:include path="${slide.path}"/>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>