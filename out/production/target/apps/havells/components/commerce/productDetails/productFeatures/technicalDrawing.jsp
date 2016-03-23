<%@ page import="java.util.ArrayList" %>
<%
    String[] technicalDrawings = null;
    boolean flag = false;
    if(productDetails != null){
        technicalDrawings = productDetails.getTechnicalDrawings();
        if(technicalDrawings != null)
            flag = technicalDrawings.length > 0;
    }
%>
<c:set var="images" value="<%=technicalDrawings%>"/>
<c:set var="flag" value="<%=flag%>"/>

<div class="featuresAccWrapper" id="technicalDrawingTab" style="display: none">
    <div class="featureClicker">
        <a href="javascript:;" ${style}>${featureHeading}</a>
    </div>

    <div class="featureContent ${display}">
        <div class="technicalDrawing" id="technicalDrawingTab1">
        </div>
    </div>
</div>


<c:choose>
    <c:when test="${flag}">
        <div class="featuresAccWrapper" id="originalTechnicalDrawing">
            <div class="${featureClass}">
                <a href="javascript:;" ${style}>${featureHeading}</a>
            </div>
            <div class="featureContent ${display}">
                <div class="technicalDrawing" id="technicalDrawing">
                      <c:forEach items="${images}" var="imagePath">
                            <div class="specDrawing">
                                <a href="${imagePath}" class="specFancybox">
                                    <img src="${imagePath}">
                                    <div class="viewImage">VIEW IMAGE</div>
                                </a>
                            </div>
                      </c:forEach>
                </div>
            </div>
        </div>
    </c:when>
</c:choose>
<c:if test="${editMode}">
    <div style="clear:both;"></div>
</c:if>
<script>
    $(document).ready(function(){
        accordionCheck.incrementAccordionCount(${not empty images});
    });
</script>

