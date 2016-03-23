<%@include file="/apps/havells/global.jsp"%>
<%@page session="false" %>
<%@ page import="com.day.cq.wcm.foundation.Download" %>
<c:set var="path" value="<%=new Download(resource).getHref() %>"/>
<div class="twoColRight">
    <h1>${properties.title}</h1>
    <h4>${properties.subTitle}</h4>
    <p>${properties.description} </p>
    <div class="quarterlyReportsWrapper">
        <div class="quarterlyReports noBorderBottom">
            <div class="quarterlyReportLeft">
                <div class="disclouserContent">
                    <ul>
                        <li>
                            <a href="javascript:;">
                                <cq:include path="pdf1" resourceType="havells/components/commerce/report/pdf"/>
                                <div class="pdfImageDisclosures"></div>
                                <div class="pdfDownloadDisclosures"><span></span></div>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="quarterlyReportLeft noBorderRight">
                <div class="disclouserContent disclouserContentRight">
                    <ul>
                        <li>
                            <a href="javascript:;">
                                <cq:include path="pdf2" resourceType="havells/components/commerce/report/pdf"/>
                                <div class="pdfImageDisclosures"></div>
                                <div class="pdfDownloadDisclosures"><span></span></div>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
