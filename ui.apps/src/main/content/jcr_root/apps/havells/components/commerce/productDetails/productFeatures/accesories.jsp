<%@ page import="java.util.List" %>
<%
    List<HeadingDescModel> accessories = null;
    boolean flag = false;
    if(productDetails != null){
         accessories = productDetails.getAccessories();
         if(accessories != null )flag = accessories.isEmpty();
    }
%>
<c:set var="list" value="<%=accessories%>"/>
<c:set var="isAccessory" value="<%=flag%>"/>
<c:set var="cssClass" value="fullWidth"/>

        <div class="featuresAccWrapper" id="accessoriesTab" style="display: none">
             <div class="featureClicker">
                 <a href="javascript:;" ${style}>${featureHeading}</a>
             </div>

            <div class="featureContent ${display}">
                <div class="accesoriesRender" id="accessoriesTab1">
                </div>
            </div>
        </div>
<c:if test="${list != null && isAccessory != true}">
        <div class="featuresAccWrapper" id="originalAccessoriesFeatures">
            <div class="${featureClass}">
                <a href="javascript:;" ${style}>${featureHeading}</a>
            </div>
            <div class="featureContent ${display}">
                <div class="accesoriesRender" id="accesoriesRender">
                    <div class="havellsBringWrapper">
                            <c:forEach items="${list}" var="current" varStatus="currentProduct">
                                <div class="havellsBringInner">
                                    <c:if test="${not empty current.imagePath}">
                                        <div class="havellsBringLeft" id="havellsBringLeft${currentProduct.index}">
                                            <img src="${current.imagePath}">
                                        </div>
                                        <c:set var="cssClass" value=""/>
                                    </c:if>
                                    <div class="havellsBringRight ${cssClass}">
                                        <h6>${current.heading}</h6>
                                        <p>${current.description}</p>
                                    </div>
                                </div>
                            </c:forEach>
                    </div>
                </div>
            </div>
        </div>
</c:if>
<c:if test="${editMode}">
    <div style="clear:both;"></div>
</c:if>

<script>
    $(function(){
        $().checkImg();
    });
    $(document).ready(function(){
        accordionCheck.incrementAccordionCount(${not empty list});
    });
</script>
