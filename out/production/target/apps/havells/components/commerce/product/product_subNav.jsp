<%@ include file="/apps/havells/global.jsp" %>
<%@page session="false" %>
<%@ page import="com.havells.core.model.product.ProductSiblings" %>
<%@ page import="com.havells.util.RenditionUtil" %>
<%@ page import="com.havells.core.model.product.ProductConstant" %>
<c:set var="siblings" value="<%= new ProductSiblings(currentPage,resourceResolver) %>"/>

<div class="subNavImageWrapper">
    <div class="container">
        <div class="master">
            <div class="recTopSliderWrap">
                <div class="recTopSlider">
                    <div class="recTopSliderInner">
                        <c:forEach var="product" items="${siblings.productDataLinks}">
                            <div class="div100By95"><a href="${product.pageUrl}">
                                <c:set var="image" value="${product.imageUrl}"/>
                                <c:set var="productImg"
                                       value="<%= RenditionUtil.getRendition((String)pageContext.getAttribute("image"),resourceResolver,ProductConstant.THUMBNAIL_RENDITION_75X71)%>"/>
                                <img src="${productImg}" style="width: 106px;" title="${product.title}">
                               </a></div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="application/javascript">
    $(function(){
        var slideInner = $('.recTopSliderInner');
        subNavBindingSlider(slideInner);
    });

</script>
