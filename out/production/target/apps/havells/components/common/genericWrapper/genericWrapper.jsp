<%@include file="/apps/havells/global.jsp" %>
<%@ page import="com.havells.core.model.Carousel" %>
<%@ page import="java.util.UUID" %>

<c:set var="carousel" value="<%= new Carousel(resource.getChild("carouselContainer")) %>"/>
<c:set var="slideNo" value="${properties.noOfSlides ne null ? properties.noOfSlides : 4}"/>
<c:set var="uuid" value="<%= UUID.randomUUID().toString()%>"/>

<div class="caseStudySlider" id="${uuid}">
    <h3>${properties.heading}</h3>
    <div class="caseStudySliderSec">
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

<div class="clearBoth"></div>
<script>
    $(document).ready(function(){
        var slider = $("#${uuid} .caseStudySliderSec .slick-slider");
        console.log(slider);
        genericSlider(slider, ${slideNo});
    });
</script>
