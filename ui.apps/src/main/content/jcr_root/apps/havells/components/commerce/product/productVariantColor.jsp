<%@page session="false" %>
<%@ include file="/apps/havells/global.jsp" %>
<%@ page import="com.adobe.cq.commerce.api.Product, com.havells.core.model.VariantsUtil, java.util.List" %>

<%
    Product baseProduct = (Product) request.getAttribute("cq.commerce.product");
    Resource rr = baseProduct.adaptTo(Resource.class);
    VariantsUtil variantsUtil=null;
    if (baseProduct != null){
        variantsUtil = new VariantsUtil(baseProduct, resourceResolver);
        variantsUtil.setVariantList();
    }
    List<Product> colorVariantsList =  variantsUtil.getColorVariantList();
    List<Product> genericVariantsList =  variantsUtil.getGenericVariantList();
%>

<c:set var="colorVariantsList" value="<%= colorVariantsList %>"/>
<c:set var="genericVariantsList" value="<%= genericVariantsList %>" scope="request"/>

<c:if test="${editMode}">
    Variants of this products.
</c:if>
<c:if test="${not empty colorVariantsList}">
    <div class="colorWrapper" id="variantsColor"> <span>
    <c:set var="title" value="${properties.variationTitle}"/>
            ${ not empty title ? title : "Color Options"}</span>

        <%
            int length = colorVariantsList.size();
            for(int index = 0;index < length; index++){%>
        <div class="colorDiv" index="<%=index%>" id="<%=colorVariantsList.get(index).getPath()%>" data-iscolor="color">
            <a href="javascript:;">
                <%
                    String colorImg = colorVariantsList.get(index).getProperty("colorImg", String.class);
                    String finalColorImg = "";
                    try {
                        if (colorImg != null) {
                            Resource colorImageResource = resourceResolver.getResource(colorImg);
                            if (colorImageResource == null) {
                                colorImageResource = resourceResolver.getResource(colorImg.indexOf("color") > 0 ? colorImg.replace("/color.", "/colour.").intern() : colorImg.replace("/colour.", "/color.").intern());
                                finalColorImg = colorImageResource != null ? colorImageResource.getPath() : "";
                            } else {
                                finalColorImg = colorImg;
                            }
                        }
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                %>
                <img src="<%=finalColorImg%>" title="<%=colorVariantsList.get(index).getProperty("color",String.class)%>"
                     alt="<%=colorVariantsList.get(index).getProperty("color",String.class)%>">
            </a>
        </div>
        <% }%>
    </div>
</c:if>
<div style="clear:both;"></div>
<script type="application/javascript">

    $(document).ready(function(){
        jQuery(".colorDiv").on("click" , function(){
            setAllVariantsData($(this));
        });
    });
</script>
