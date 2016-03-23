<%@ page import="com.havells.core.model.product.ProductDetails" %>
<%@ page import="java.util.List" %>
<%@ page import="com.adobe.cq.commerce.api.Product" %>
<%@ page import="com.havells.core.model.product.ProductRecommendationModal" %>
<%@ page import="com.havells.util.RenditionUtil" %>
<%@ page import="com.havells.core.model.product.ProductConstant" %>
<%@page session="false" %>
<%@include file="/apps/havells/global.jsp"%>
<%
	ProductDetails productDetails = (ProductDetails)request.getAttribute("cq.commerce.product.details");
	List<ProductRecommendationModal> list = null;
	if(productDetails != null){
		list =  productDetails.getRecommendedProducts();
	}
%>
<c:set var="productList" value="<%=list%>"/>
<c:if test="${not empty productList && productList != null}">
	<div class="likeWrapper">
		<div class="divHeading"> ${not empty properties.heading ? properties.heading : "You may also like"}</div>
		<div class="applicationProducts">
			<ul>
				<c:forEach items="${productList}" var="product">
					<li>
						<a href="${product.productPagePath}">
							<div class="productName">${product.productTitle}</div>
							<div class="mrpDiv">MRP Rs. ${product.price}/-</div>
							<div class="productDiv">
								<c:set var="image" value="${product.imageSrc}"/>
								<c:set var="mainImg"
									   value="<%= RenditionUtil.getRendition((String)pageContext.getAttribute("image"),resourceResolver, ProductConstant.THUMBNAIL_RENDITION_342X325)%>"/>
								<img src="${mainImg}"></div>
						</a>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</c:if>
