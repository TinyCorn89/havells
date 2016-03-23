<%@include file="/apps/havells/global.jsp" %>
<%@ page import="com.havells.core.model.PopulateHavellsJob" %>
<%@page session="false" %>

<cq:text tagName="h4" placeholder="Job Application Form"/>
<sling:defineObjects />
<c:set var = "selector" scope = "page" value = "<%= slingRequest.getRequestPathInfo().getSelectors()%>"/>
<c:choose>
    <c:when test="${selector[0] == 'apply'}">
        <div class="currentOpporWrap">
            <div class="designationWrap">
                <c:set var="error" scope="page" value="<%=request.getParameter("error")%>"/>
                <c:if test="${error == 500}">
                    <span><b>${properties.error}</b></span>
                </c:if>
                <h4>${properties.heading}</h4>
                <p>${properties.subHeading}</p>
                <form id="applyForm" action="/bin/servlets/submitJob" method="POST" novalidate="novalidate"
                      enctype="multipart/form-data">
                    <div class="applyFormContainer">
                        <label>Applying For</label>
                        <c:set var="populateHavellsJob"
                               value="<%=new PopulateHavellsJob(resourceResolver,currentPage.getPath())%>"/>

                        <input id="JobsToBeApplied" name="JobsToBeApplied" type="text" class="fieldInner" value="${populateHavellsJob.jobsDesignation}" readonly required/>
                        <label for="name">Name </label><input name="userName" type="text" class="fieldInner"
                                                              id="name" required>

                        <label for="number">Mobile Number</label><input name="mobile" type="text" class="fieldInner"
                                                                        minlength="10"     id="number" required>
                        <label for="location">Current Location</label><input name="location" type="text"
                                                                             class="fieldInner" id="location" required>
                        <label>Total Experience </label>

                        <div class="monthContainer">
                            <select name="month" class="month" required>
                                <option value="Months">Months</option>
                                <c:forEach var="i" begin="0" end="12">
                                    <option value="<c:out value="${i}"/>"><c:out value="${i}"/></option>
                                </c:forEach>
                            </select>
                            <select name="year" class="month noMarginRight" required>
                                <option value="Year">Year</option>
                                <c:forEach var="i" begin="0" end="40">
                                    <option value="<c:out value="${i}"/>"><c:out value="${i}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        <label for="company">Current Company</label><input name="currentCompany" type="text" class="fieldInner" id="company" required>
                        <label for="designation">Current Designation</label><input name="currentDesignation" type="text" class="fieldInner" id="designation" required>
                        <label>Attached Resume</label>

                        <div class="chosseFile">
                            <input name="file" value="Choose File" type="file" class="chooseFileInner" required/>
                        </div>
                        <input name="departmentMailId" type="hidden" class="fieldInner" value="${properties.departmentMailId}" >

                        <input name="thankYouPageUrl" id="thankYouPageUrl" type="hidden" class="input" value="${properties.thankYouPageUrl}"/>
                        <input name="thisPage" id="thisPage" type="hidden" class="input"
                               value="<%=currentPage.getPath()%>.apply"/>
                        <input name="postedJobs" id="postedJobs" type="hidden" class="input"
                               value="${properties.postedJobs}"/>
                    </div>
                    <c:if test="${error == 101}">
                        <strong><span>${properties.userExist}</span></strong>
                    </c:if>
                    <div class="applyNowWrapBottom">
                        <div class="apply"><input type="submit" id="appliedJob" value="Apply"/></div>
                        <div class="disclaimer"><a href="javascript:;">Disclaimer</a></div>
                    </div>
                </form>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="currentOpporWrap">
            <div class="designationWrap">
                <h4>${properties.designation}</h4>
                <div class="currentOpporWrap">
                    <div class="applyNowWrap">
                        <ul>
                            <li>${properties.department}</li>
                            <li>${properties.posting}</li>
                            <li>${properties.location}</li>
                        </ul>
                        <input name="departmentMailId" type="hidden" class="fieldInner" value="${properties.departmentMailId}" >
                        <input name="branch" type="hidden" class="fieldInner" value="${properties.branch}" >
                        <input name="product" type="hidden" class="fieldInner" value="${properties.product}" >
                        <input name="experience" type="hidden" class="fieldInner" value="${properties.experience}" >
                        <input name="hashOfPosition" type="hidden" class="fieldInner" value="${properties.hashOfPosition}" >
                        <input name="industryPrefrences" type="hidden" class="fieldInner" value="${properties.industryPrefrences}" >
                    </div>
                    <cq:include path="right" resourceType = "foundation/components/parsys" />
                    <div class="applyNowWrapBottom">
                        <div class="apply"><a href="<%= currentPage.getPath()%>.apply.html">Apply</a></div>
                        <div class="disclaimer"><a href="javascript:;">Disclaimer</a></div>
                    </div>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<div class="clearBoth"></div>
