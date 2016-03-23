<%@page session="false"%>
<%@ include file="/libs/foundation/global.jsp" %>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="com.havells.core.model.product.ProductDetails" %>
<%@ page import="com.adobe.cq.commerce.api.Product" %>

<%
    ProductDetails productDetails = (ProductDetails) request.getAttribute("cq.commerce.product.details");
    String mainImages[] = productDetails.getProductImages();
    Product actualProduct = (Product) request.getAttribute("cq.commerce.product");
    String imageSrc = "";
    if(actualProduct != null && actualProduct.getImage() != null){
        imageSrc = actualProduct.getImage().getSrc();
    }
%>
<c:set var="mainImages" value="<%=mainImages%>"/>
<div class="zoomWrapper">
    <div class="productMainDiv" id="productMainDiv">
        <div class="slick-slider">
            <div class="productMain mainImg">
                   <img src="<%= imageSrc%>">
            </div>
            <c:forEach items="${mainImages}" var="image">
                <div class="productMain"><img src="${image}"></div>
            </c:forEach>
        </div>
    </div>
    <div class="three360Inner noDisplay">
        <div class="threeSixtyNew"></div>
        <div class="sliderBarWrapper">
            <div class="sliderBar"></div>
        </div>
    </div>
</div>

<cq:include script="productVariantColor.jsp"/>


<script type="application/javascript">
    var three360Wrapper = $(".three360Wrapper");
    if(three360Wrapper != undefined){
        three360Wrapper.click(function(){
            $(".productMainDiv").hide();
            $(".three360Inner").show();
        });
    }

    $(function(){
        var frames = SpriteSpin.sourceArray('/content/dam/havells/default_img.png',{frame: [0, 2], digits: 1 });
        var spin = $('.threeSixtyNew');
        var slide = $('.sliderBar');
        if(slide != undefined) {
            spin.spritespin({
                source: frames,
                width: 400,
                height: 300,
                sense: 0,
                frame:1,
                animate : false,
                onLoad: function(){
                    slide.slider({
                        min: 0,
                        max: frames.length - 1,
                        slide: function(e, ui){
                            var api = spin.spritespin('api');
                            api.stopAnimation();
                            api.updateFrame(ui.value);
                        }
                    });
                },
                onFrame: function(e, data){
                    slide.slider('value', data.frame);
                }
            });
        }
    });


</script>
