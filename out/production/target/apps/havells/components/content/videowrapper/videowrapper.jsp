<%@include file="/apps/havells/global.jsp" %>
<%@ page import="com.havells.core.model.Carousel,java.util.UUID" %>

<% Resource container = resource.getChild("carouselContainer"); %>
<%
    String result = properties.get("displayType", "D");
    boolean dots = false;
    boolean arrows = false;
    if (result.equals("D")) {
        dots = true;
    } else {
        arrows = true;
    }
%>
<input type="hidden" name="dots" value="<%=dots%>" />
<input type="hidden" name="arrows" value="<%=arrows%>" />
<c:set var="carousel" value="<%= new Carousel(container) %>"/>
<div class="videoWrapper" style="float:none;">
    <div class="divHeading" style="float:none;">${properties.heading}</div>
    <div class="videoSlider" style="float:none;">
        <div class="slick-slider">
            <c:choose>
                <c:when test="${wcmMode == 'EDIT' || wcmMode == 'DESIGN'}">
                    Add slides below
                    <cq:include path="carouselContainer" resourceType="foundation/components/parsys"/>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${carousel.childList}" var="slide" varStatus="status">
                        <sling:include path="${slide.path}"/>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>



