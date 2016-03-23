<%@ page import="java.util.ArrayList" %>
<%@ page import="com.havells.core.model.product.HeadingDescModel" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.day.cq.tagging.Tag" %>
<%@ page import="com.day.cq.tagging.TagManager,org.apache.commons.lang.ArrayUtils" %>
<%
    Map<String, List> technicalSpecMap = null;
    boolean isTechnicalSpecEmpty = true;
    boolean isImagePathsEmpty = true;
    String[] imagePaths = null;
    if(productDetails != null){
        technicalSpecMap = productDetails.getTechnicalSpec();
        if(technicalSpecMap != null && !technicalSpecMap.isEmpty()){
            isTechnicalSpecEmpty = false;
        }

        imagePaths = productDetails.getTechnicalSpecImages();

        if(imagePaths != null){
            isImagePathsEmpty = false;
        }
    }
%>
<c:set var="isTechnicalSpecEmpty" value="<%=isTechnicalSpecEmpty%>"/>
<c:set var="isImagePathsEmpty" value="<%=isImagePathsEmpty%>"/>
<c:set var="technicalSpecMap" value="<%=technicalSpecMap%>"/>
<c:set var="imagePaths" value="<%=imagePaths%>"/>


<div class="featuresAccWrapper" id="technicalSpecsTab" style="display: none">
    <div class="${featureClass}">
        <a href="javascript:;" ${style}>${featureHeading}</a>
    </div>

    <div class="featureContent ${display}">
        <div class="technicalSpecificationsDiv" id="technicalSpecsTabText"></div>
        <div id="technicalSpecsTabImage"></div>
    </div>
</div>

<c:choose>
    <c:when test="${not isTechnicalSpecEmpty || not isImagePathsEmpty}">
        <div class="featuresAccWrapper" id="originalTechnicalSpecs">
            <div class="${featureClass}">
                <a href="javascript:;" ${style}>${featureHeading}</a>
            </div>
            <div class="featureContent ${display}">
                <div class="technicalSpecificationsDiv" id="techSpecsDetails">
                    <c:if test="${not empty technicalSpecMap}">
                        <% for(Map.Entry<String, List> entry :  technicalSpecMap.entrySet()){
                        %>
                        <div class="techSpecWrapper">
                            <div class="techSpecWrapperTitle"><%=entry.getKey()%></div>
                            <c:forEach items="<%=entry.getValue()%>" var="imagePath">
                                <div class="techSpecContentWrapper">
                                    <div class="width20Pre">${imagePath.heading}</div>
                                    <div class="borderRightNone">${imagePath.description}</div>
                                </div>
                            </c:forEach>
                        </div>
                        <%  }
                        %>
                    </c:if>
                </div>
                    <%-------------------image view in tech specifications--------------%>
                <div id="techSpecsImages">
                    <c:choose>
                        <c:when test="${not isImagePathsEmpty}">
                            <c:forEach items="${imagePaths}" var="imagePath">
                                <div class="specDrawing">
                                    <a href="${imagePath}" class="specFancybox"><img src="${imagePath}">
                                        <div class="viewImage">VIEW IMAGE</div>
                                    </a>
                                </div>
                            </c:forEach>
                        </c:when>
                    </c:choose>
                </div>
                <c:if test="${editMode}">
                    <div style="clear:both;"></div>
                </c:if>
            </div>
        </div>
    </c:when>
</c:choose>
<script>
    $(document).ready(function(){
        accordionCheck.incrementAccordionCount(${not empty technicalSpecMap});
    });
</script>
