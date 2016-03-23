<%@ page import="com.havells.core.model.QuarterReport" %>
<%@include file="/apps/havells/global.jsp" %>

<c:set var="path" value="${properties.path}"/>
<c:if test="${not empty path}">
    <c:set var="report" value="<%=new QuarterReport(resource,(String)pageContext.getAttribute("path"))%>"/>
</c:if>
<div>
    <h1>${properties.title}</h1>
    <div class="quaterlySelectSection">
        <div class="quaterlySelectBox">
            <select>
                <c:forEach items="${report.yearList}" var="item">
                    <option>${item}</option>
                </c:forEach>
            </select>
        </div>

    </div>
<cq:include path="richText" resourceType="foundation/components/text" />
<c:choose>

    <c:when test='${wcmMode == "EDIT"}'>
        Go to Preview Mode to View the Rendered Component
    </c:when>

    <c:otherwise>
            <script type="text/html" id="reportTemplate">
                {#.}
                <div class="quarterlyReportLeft noBorderRight{$idx}">
                    <div class="quarterlyContent noBorder{$idx} noBorderBottom{$idx}">
                        <h2>{quarter}</h2>
                        <ul>
                            {#documents}
                            <cq:include path="{href}" resourceType="havells/components/commerce/report/pdf"/>
                            {/documents}
                        </ul>
                    </div>
                </div>
                {/.}
            </script>
            <div id="reportOutput" class="clearBoth"></div>
    </c:otherwise>
</c:choose>
</div>
<div style="clear:both"></div>

<script>
    $(document).ready(function () {
        jQuery().renderReport({
            "templateId": "reportTemplate",
            "resultId": "reportOutput",
            "data": ${report.pdfResources},
            "year": $('.quaterlySelectBox select').val()
        });

        $('.quaterlySelectSection .quaterlySelectBox select').on('change', function () {
            jQuery().renderReportWithoutLoad({
                "templateId": "reportTemplate",
                "resultId": "reportOutput",
                "data": ${report.pdfResources},
                "year": $(this).val()
            });
        })

    });
</script>

