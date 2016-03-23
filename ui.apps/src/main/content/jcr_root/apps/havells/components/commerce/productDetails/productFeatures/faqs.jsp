<%
    String faqs = "";
    if(productDetails != null){
        faqs = productDetails.getFaqs();
    }
%>
<c:set var="faqs" value="<%=faqs%>"/>

<div class="featuresAccWrapper" id="faqsTab" style="display: none">
    <div class="featureClicker">
        <a href="javascript:;" ${style}>${featureHeading}</a>
    </div>

    <div class="featureContent ${display}">
        <div id="faqsTab1">

        </div>
    </div>
</div>

<c:choose>
    <c:when test="${not empty faqs}">
        <div class="featuresAccWrapper" id="originalFaqs">
            <div class="${featureClass}">
                <a href="javascript:;" ${style}>${featureHeading}</a>
            </div>
            <div class="featureContent ${display}">
                <div id="faqsContent"><%=faqs%></div>
            </div>
        </div>
    </c:when>
</c:choose>
<script>
    $(document).ready(function(){
        accordionCheck.incrementAccordionCount(${not empty faqs});
    });
</script>
