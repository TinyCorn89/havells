<%@ page import="java.util.List" %>
<%
    List<HeadingDescModel> detailFeature = null;
    boolean flag = false;
    if(productDetails != null){
         detailFeature = productDetails.getDetailFeature();
         if(detailFeature != null )flag = detailFeature.isEmpty();
    }
%>
<c:set var="list" value="<%=detailFeature%>"/>
<c:set var="isTechnicalSpec" value="<%=flag%>"/>
<c:set var="cssClass" value="fullWidth"/>

        <div class="featuresAccWrapper" id="detailTab" style="display: none">
             <div class="featureClicker">
                 <a href="javascript:;" ${style}>${featureHeading}</a>
             </div>

            <div class="featureContent ${display}">
                <div class="detailFeatures" id="detailTab1">
                </div>
            </div>
        </div>
<c:choose>
    <c:when test="${list != null && isTechnicalSpec != true}">
        <div class="featuresAccWrapper" id="originalDetailsFeatures">
            <div class="${featureClass}">
                <a href="javascript:;" ${style}>${featureHeading}</a>
            </div>
            <div class="featureContent ${display}">
                <div class="detailFeatures" id="detailFeature">
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
    </c:when>
</c:choose>
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
