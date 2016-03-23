<%@include file="/apps/havells/global.jsp" %>
<%@ page import="com.havells.services.SearchQuery, 
    			java.util.List, 
				com.havells.core.model.product.ProductDetails,
				com.havells.core.model.product.ProductFilterResultModel" %>
<%
    String filterType = request.getParameter("filterType");
    String filterValue = request.getParameter("filterValue");
    response.setContentType("text/html");
    SearchQuery productSearch = sling.getService(SearchQuery.class);
    List<ProductDetails> productList = null;
    if(filterValue != null) {
        productList = ProductFilterResultModel.getAllProducts(filterType, filterValue, productSearch, resourceResolver);
    }
%>
<c:set var="productList" value="<%=productList%>"/>
<div class="garmentWrapper">
    <ul id="productResults" class="garmentDiv">
        <c:choose>
            <c:when test="${productList != null }">
                <c:forEach items="${productList}" var="product" varStatus="currentProduct">
                    <li>
                        <a id="item-${currentProduct.count}" href="${product.pagePath}">
                            <div class="productFullName">${product.pageTitle}</div>
                            <div class="detailsOfProd">
                                <div class="productCaptity">${product.productSubTitle}</div>
                                <div class="productMRP">${product.price}</div>
                            </div>
                            <div class="garmentProdictDiv"><img src="${product.smallCoverImage}"></div>
                            <div class="compareDiv">
                                <div class="addToCart2">
                                    <label><input type="checkbox" class="checkBoxClass compareClass">
                                        <span>Compare</span>
                                    </label>
                                </div>
                            </div>
                        </a>
                    </li>
                </c:forEach>
            </c:when>
            <c:otherwise>
                    <li> No Data found !!!</li>
            </c:otherwise>
        </c:choose>
    </ul>
</div>
