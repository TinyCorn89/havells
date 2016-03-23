<%@page session="false" %>
<%@ include file="/libs/foundation/global.jsp" %>
<%@ page contentType="text/html; charset=utf-8"
         import="com.havells.core.model.product.*,
                 java.util.List"
 %>
<%
    ProductDetails productDetails = (ProductDetails) request.getAttribute("cq.commerce.product.details");
    List<HeadingDescModel> quickFeature = productDetails.getQuickFeature();
%>
<c:set var="list" value="<%=quickFeature%>"/>
<c:set var="productDetails" value="<%= productDetails %>"/>
<div class="productWrapper" id = "productWrapper">
    <c:choose>
        <c:when test="${not empty list}">
            <ul>
                <c:forEach items="${list}" var="imagePath">
                    <li>
                        <h4>${imagePath.heading}</h4>
                        <p>${imagePath.description}</p>
                    </li>
                </c:forEach>
            </ul>
            <c:if test="${productDetails != null && productDetails.price ne 0.0}">
            <div class="mrpWrapper">
                <div id="onlyMrp"> MRP: Rs. ${productDetails.absolutePrice} /-</div>
     <%--           <div class="offerDiv">
                        &lt;%&ndash;<ul>
                        <li>MRP: Rs. ${productDetails.absolutePrice} /-</li>
                           <li style="display: none"><span>Offer Price</span>: Rs. 0.0 /-</li>

                    </ul>&ndash;%&gt;
                </div>>
                <div class="buyNowBtn buyNowBtnMarginTop" style="display: none">
                    <a href="javascript:;">BUY NOW</a>
                </div>--%>
            </div>
           </c:if>
        </c:when>
    </c:choose>
</div>
