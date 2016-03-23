<%@page session="false" %>
<%@ include file="/apps/havells/global.jsp" %>

<c:set var="variantsList" value="${genericVariantsList}"/>
        <c:if test="${editMode}">
            Variants of this products.
        </c:if>
        <c:if test="${variantsList != null && not empty variantsList && fn:length(variantsList) gt 1}">
            <div class="variantsWrap">
                <div class="divHeading">VARIANTS</div>
                <div class="variantSliderWrap">
                    <div class="variantSlider">
                        <c:forEach items="${variantsList}" var="product" varStatus="status">
                            <div class="variantsDiv" style="width: 144px;" id="${product.path}">
                                <div class="variantsDivInner"  name="${status.index}">
                                    <a href="javascript:;">
                                        <img src="${product.image.src}" width="70" height="50" title="text">
                                    </a>
                                </div>
                                <div class="oyster">${product.title} </div>
                                <%--<span>320 ml water tank</span>--%>
                            </div>
                        </c:forEach>
                     </div>
                </div>
            </div>
            <div style="clear:both;"></div>
        </c:if>
        <script>
            $(function(){
                var variantSlider = $('.variantSlider');
                bindSlider(variantSlider);
            });
            jQuery(document).ready( function(){
                var initialIndex = 0;
                $('.variantsDiv[index="'+initialIndex+'"]').hide();
                jQuery(".variantsDiv").on("click" , function(){
                    accordionCheck.resetAccordionCounter();
                    $('.variantsDiv[index="'+$(this).attr("index")+'"]').hide();
                    $('.variantsDiv[index!="'+$(this).attr("index")+'"]').show();
                    setAllVariantsData($(this));
                });
            });
        </script>