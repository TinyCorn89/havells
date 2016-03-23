<%@include file="/apps/havells/global.jsp"%>
<%@ page import="com.adobe.cq.commerce.api.CommerceException" %>
<%@ page import="com.adobe.cq.commerce.api.Product" %>
<%@ page import="com.havells.core.model.FetchTestimonials" %>
<%
    Product product = (Product)request.getAttribute("cq.commerce.product");
    String pPath = "";
    try{
        if(product != null && product.getPIMProduct() != null){
            pPath = product.getPIMProduct().getPath();
        }
    }catch (CommerceException ex){}
%>
<c:set var="filter" value="<%=request.getParameter("filter")%>"/>
<c:set var="productPath" value="<%=product != null ? pPath : request.getParameter("productPath")%>"/>
<c:set var="fetchTestimonials" value="<%=new FetchTestimonials(resource, pageContext.getAttribute("productPath") != null ? pageContext.getAttribute("productPath").toString() : "",request.getParameter("filter"))%>"/>
<c:set var="comments" value="${fetchTestimonials.comments}"/>
<div id="${currentNode.identifier}" data-ajax-path="${currentNode.path}" class="testimonialsIdentifier" product-path="${productPath}">
    <div class="testominalWrapper">
        <div class="divHeading">${properties.heading ? properties.heading : "TESTIMONIALS"}</div>
        <div class="testimonialSelect">
            <select autocomplete="off" id="filterBy">
                <c:choose>
                    <c:when test="${filter == 'Recent'}">
                        <option value="Top Rated">Top Rated</option>
                        <option value="Recent" selected='selected'>Recent</option>
                    </c:when>
                    <c:otherwise>
                        <option value="Top Rated" selected='selected'>Top Rated</option>
                        <option value="Recent">Recent</option>
                    </c:otherwise>
                </c:choose>
            </select>
        </div>
        <div class="testimonialsDiv">
            <div class="msg">
            </div>
            <ul>
                <c:forEach var="comment" items="${comments}">
                    <li>
                        <cq:include path="${comment.path}" resourceType="havells/components/commerce/productDetails/productTestiMonials/comment" />
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="writeTestimonialWrapper">
            <form class="anonymous_hidden" id="writeTestimonial" method="get" action="">
                <div class="productratingWrapper">
                    <span>Your rating </span>
                    <div class="productrating" data-average="0" style="height: 20px; width: 115px; overflow: hidden; z-index: 1; position: relative; cursor: default;">
                    </div>
                </div>
                <textarea autocomplete="off" id="commentTestimonial"  name="writeTestimonial" cols="" rows="" class="txtArea" onfocus="if(this.value == this.defaultValue) { this.value=''; }" onblur="if (this.value == '') { this.value=this.defaultValue;}" required placeholder="Write Testimonial"></textarea>
                <input name="" type="submit" class="submit" value="Submit">
            </form>
        </div>
    </div>
    <c:if test="${editMode}">
        <div style="clear:both;"></div>
    </c:if>
    <c:if test="${not empty productPath}">
        <script>
            jQuery(document).ready(function() {
                if ($('.testimonialsDiv .comment').length == 0) {
                    $('.msg').text(" Be the first one to Comment on this product");
                    $('.featuresLeft .testominalWrapper .testimonialsDiv').css("height", "auto");
                }
                jQuery('.testominalWrapper').addTestimonial({"productPath" : "${productPath}"});
            });
        </script>
    </c:if>
</div>
