<%@page session="false" %>
<%@include file="/apps/havells/global.jsp" %>
<%@ page contentType="text/html; charset=utf-8" import="com.havells.core.model.product.ProductDetails" %>
<%
    ProductDetails productDetails = (ProductDetails) request.getAttribute("cq.commerce.product.details");
    String manualDocUrl = "#", brochureUrl = "#", title = "", subTitle = "";
    if (productDetails != null) {
        manualDocUrl = productDetails.getProductManualUrl();
        brochureUrl = productDetails.getProductBrochureUrl();
        title = productDetails.getPageTitle();
        subTitle = productDetails.getProductSubTitle();
    }
%>
<div class="productManualWrapper">
    <div class="container">
        <div class="master">
            <div class="productHeading">
                <h1 style="margin: -5px 0px 2px" id="productTitle" class="product-title" itemprop="name">
                    <%= xssAPI.encodeForHTML(title) %>
                </h1>
                <h3><%=subTitle%></h3>
            </div>
            <div class="rateProductWrapper">
                <cq:include path="rating" resourceType="havells/components/commerce/rating"/>
            </div>
            <div class="productManual">
                <ul>
              <%--      <li id="productManual">
                        <a href="<%=manualDocUrl%>">
                            <i class="fa fa-file-text fa-arrow-up"></i>
                            <span>Product<br/> manual</span>
                        </a>
                    </li>
       --%>             <li id="productBrochure">
                        <a href="<%=brochureUrl%>" download="<%=brochureUrl%>">
                            <i class="fa fa-arrow-down"></i><span>download<br> brochure</span>
                        </a>
                    </li>
                    <li class="noBorderRight">
                        <cq:include path="${headerPath}/social"
                                    resourceType="havells/components/content/social-media"/>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>

