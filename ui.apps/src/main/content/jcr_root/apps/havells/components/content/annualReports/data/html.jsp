<%@ page import="com.havells.core.model.AnnualReports" %>
<%@include file="/apps/havells/global.jsp" %>
<%@ page contentType="text/html; charset=utf-8" %>
<c:set var="annualReports" value="<%=new AnnualReports(slingRequest,resource)%>"/>
<c:choose>
    <c:when test="${not empty annualReports.list}">
        <c:set var="count" value="0"/>
        <c:forEach items="${annualReports.list}" var="link">
            <c:if test="${count ge annualReports.startIndex && count lt annualReports.lastIndex}">
                <div class="quarterlyReportLeft">
                    <div class="quarterlyContent">
                        <div class="annualReportInput">${link.yearName}</div>
                        <ul>
                            <li>
                                <a target="_blank" href="${link.pdfPath}">
                                    <div class="annualReportPdf"><img
                                            src="/etc/clientlibs/havells/image/pdf-icon-big.png">
                                    </div>
                                    <div class="annualViewPdf"> ${link.pdfName}
                                        <span>${properties.viewPDF}</span>
                                    </div>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </c:if>
            <c:set var="count" value="${count + 1}"/>
        </c:forEach>
        <c:if test="${annualReports.linksSize gt annualReports.maxItemsPerPage}">
            <%@include file="pagenumber.jsp" %>
        </c:if>
    </c:when>
    <c:otherwise>
        <b style='color:red'>no data found</b>
    </c:otherwise>
</c:choose>
