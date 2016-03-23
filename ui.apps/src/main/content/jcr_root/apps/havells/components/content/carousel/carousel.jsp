<%@include file="/apps/havells/global.jsp" %>
<%@ page import="com.havells.core.model.Carousel,java.util.UUID" %>

<% Resource container = resource.getChild("carouselContainer"); %>
<%
    String result = properties.get("displayType", "D");
    String bannerText = properties.get("bannerText", "");
    boolean dots = false;
    boolean arrows = false;
    if (result.equals("D")) {
        dots = true;
    } else {
        arrows = true;
    }
%>
<c:set var="carousel" value="<%= new Carousel(container) %>"/>
<div class="carousel">
    <div class="slick-slider">
        <c:choose>
            <c:when test="${wcmMode == 'EDIT' || wcmMode == 'DESIGN'}">
                <p>Add slides below</p>
                <cq:include path="carouselContainer" resourceType="foundation/components/parsys"/>
            </c:when>
            <c:otherwise>
                <c:forEach items="${carousel.childList}" var="slide" varStatus="status">
                    <sling:include path="${slide.path}"/>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
    <c:if test="<%= ((bannerText!=null) && (!bannerText.equals("")) ) %>">
        <div class="container">
            <div class="master">
                <div class="bannerText">${properties.bannerText}</div>
            </div>
        </div>
    </c:if>
</div>
<c:set var="uuid" value="<%=UUID.randomUUID().toString()%>"/>
<div id="${uuid}"></div>
<script>
    $(document).ready(function () {
        $("#${uuid}").closest('.carousel').find('.slick-slider').slick({
            dots: <%= dots %>,
            arrows: <%= arrows %>
        });
    });
</script>
<script>
    $(document).ready(function () {
        try {
            $(".carousel").resize();
        } catch (e) {
            alert(e);
        }
    });
</script>
