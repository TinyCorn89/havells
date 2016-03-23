<%@page session="false"%>
<%@ include file="/libs/foundation/global.jsp" %>
<%@ page contentType="text/html; charset=utf-8" import="
		com.adobe.cq.commerce.api.CommerceService,
		com.adobe.cq.commerce.api.CommerceSession,
		com.adobe.cq.commerce.api.Product,
		com.day.cq.i18n.I18n,
		com.havells.core.model.product.ProductConstant,
		com.havells.core.model.product.ProductDetails,
		com.havells.util.ProductLikeHelper,
		com.havells.util.RenditionUtil,
		org.apache.commons.lang.StringUtils" %>
<%@ page import="org.apache.sling.api.resource.Resource" %>
<%@ page import="org.apache.sling.api.resource.ValueMap" %>

<%
    final I18n i18n = new I18n(slingRequest);
    CommerceService commerceService = resource.adaptTo(CommerceService.class);
    CommerceSession session = commerceService.login(slingRequest, slingResponse);

    String productData = resource.adaptTo(ValueMap.class).get("productData",String.class);

    Product actualProduct = null;

    if(productData != null ){
        ProductLikeHelper.setProductData(productData);
        actualProduct = commerceService.getProduct(productData);
        ProductLikeHelper.setProduct(actualProduct);
    }
%>
<cq:include script="init.jsp"/>
<%

	String addToCartUrl = (String) request.getAttribute("cq.commerce.addToCartUrl");
	String redirect = (String) request.getAttribute("cq.commerce.redirect");
	String errorRedirect = (String) request.getAttribute("cq.commerce.errorRedirect");
	Product baseProduct = (Product) request.getAttribute("cq.commerce.product");
	Resource baseProductImage = baseProduct.getImage();
	try{

		final String productQuantityId = xssAPI.encodeForHTMLAttr("product-quantity-" + System.currentTimeMillis());
		String mainImages[]= new String[0];
		if(baseProduct != null){
			ProductDetails productDetails = (ProductDetails) request.getAttribute("cq.commerce.product.details");
			mainImages = productDetails.getProductImages();
		}
		String defaultImageSrc = (baseProduct != null && baseProduct.getImage() != null) ? baseProduct.getImage().getSrc() : "";
%>
<cq:include script="product_subNav.jsp"/>
<cq:include script="product_header.jsp"/>
<c:set var="showThreeSixty" value="${properties.threeSixty}"/>

<div class="productWrapperDiv productWrapperBg">
	<div class="container">
		<div class="master zoomRelative">
			<div class="productSliderWrapper">

				<div class="threeSliderWrapper">
					<div class="threeSlider" id="threeSliderImages">
						<div class="threeSliderInner">
                            <a class="active" href="javascript:;">
                         <img src="<%= RenditionUtil.getRendition(defaultImageSrc, resourceResolver, ProductConstant.THUMBNAIL_RENDITION_75X71)%>"></a>
                        </div>
						<%
							for(String imageUrl : mainImages) {
						%>
						<div class="threeSliderInner"><a href="javascript:;">
                            <img src="<%= RenditionUtil.getRendition(imageUrl, resourceResolver, ProductConstant.THUMBNAIL_RENDITION_75X71)%>"></a>
                        </div>
						<% } %>
					</div>

					<c:if test="${showThreeSixty}">
						<div class="three360Wrapper"><a href="javascript:;">
							<img src="/etc/clientlibs/havells/image/image-view-360.png">
						</a></div>
					</c:if>

				</div>

				<cq:include script="product-slider.jsp"/>
			</div>

			<form class="product-form" method="POST" action="<%= xssAPI.getValidHref(addToCartUrl) %>"
				  onsubmit="return validateProductQuantity('<%= productQuantityId %>') && trackCartAdd(this)">
				<cq:include script="product_specs.jsp"/>
			</form>
		</div>

	</div>


</div>

<%
	}catch(Exception ex){
		ex.printStackTrace();
	}
%>



<script type="text/javascript">

	$CQ(document).on("sitecatalystAfterCollect", function(event) {
		if (CQ_Analytics.Sitecatalyst) {
			CQ_Analytics.record({
				"event": ["prodView"],
				"values": {
					"product": [{
						"category": "",
						"sku": "<%= xssAPI.encodeForJSString(baseProduct.getSKU()) %>"
					}]
				},
				"componentPath": "<%= xssAPI.encodeForJSString(resource.getResourceType()) %>"
			});
		}
	});

	function validateProductQuantity(fieldId) {
		var quantity = document.getElementById(fieldId).value;
		if (quantity.length == 0 || (quantity.match(/^\d+$/) && Number(quantity) > 0)) {
			return true;
		} else {
			alert('<%= i18n.get("Quantity must be a positive number.") %>');
			document.getElementById(fieldId).value = "";
			return false;
		}
	}

	function trackCartAdd(form) {
		if (CQ_Analytics.Sitecatalyst) {
			var productQuantity = Number($("input[name='product-quantity']", form).val() || '1');
			var productPrice    = Number($("input[name='product-size']:checked", form).data('price').replace(/[^0-9\\.]/g, ''));
			var productChildSku =        $("input[name='product-size']:checked", form).data('sku');
			CQ_Analytics.record({
				"event": ["cartAdd"<%= (session.getCartEntryCount() == 0) ? ", 'cartOpen'" : "" %>],
				"values": {
					"product": [{
						"category": "",
						"sku": "<%= xssAPI.encodeForJSString(baseProduct.getSKU()) %>",
						"price": productPrice * productQuantity,
						"quantity": productQuantity,
						"evars": {
							"childSku": CQ.shared.Util.htmlEncode(productChildSku)
						}
					}]
				},
				"componentPath": "<%= xssAPI.encodeForJSString(resource.getResourceType()) %>"
			});
		}
		return true;
	}

	function trackProductViewed() {
		if (CQ_Analytics.ViewedProducts) {
			CQ_Analytics.ViewedProducts.record(
				'<%= xssAPI.encodeForJSString(baseProduct.getPagePath()) %>',
				'<%= xssAPI.encodeForJSString(baseProduct.getTitle()) %>',
				'<%= xssAPI.encodeForJSString(baseProductImage != null ? resourceResolver.map(baseProductImage.getPath()) : "") %>',
				'<%= xssAPI.encodeForJSString(session.getProductPrice(baseProduct))%>');
		}
	}

	function selectVariationAndSize() {
		if (window.location.hash) {
			var hashSku = window.location.hash.slice(1);
			var hashSize = $(".product-size input[data-sku='"+hashSku+"']");
			if (hashSize.length > 0) {
				$("article").addClass("isHidden");
				hashSize.parents("article").removeClass("isHidden");
				hashSize.click();
				return;
			} else {
				var hashVariation = $("li[data-sku='"+hashSku+"']");
				if (hashVariation.length > 0) {
					$("article").addClass("isHidden");
					hashVariation.parents("article").removeClass("isHidden");
					hashVariation.click();
					return;
				}
			}

			if (window.location.hash.length > 0) {
				window.location.hash = "";
			}
		}
		var defaultSize = $("article:visible").find(".product-size input:first");
		defaultSize.click();
	}

	$CQ(document).ready(function() {
		selectVariationAndSize();
		trackProductViewed();
	});
</script>

