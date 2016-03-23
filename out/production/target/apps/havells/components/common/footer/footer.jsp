<%@include file="/apps/havells/global.jsp"%>
<%@ page import="com.havells.core.model.Multifield" %>
<c:set var="multifield" value="<%=new Multifield(resource)%>"/>
<c:set var="links" value="${multifield.list}"/>
<div class="container">
    <div class="master">
        <c:choose>
            <c:when test="${links != null}">
                <c:forEach var="linkObj" items="${links}" varStatus="loopCounter">
                        <div class="col">
                            <div class="heading"><a href="${linkObj.path}">${linkObj.link}</a></div>
                            <div class="linkWrapper">
                                <cq:include path="par${loopCounter.count}" resourceType="foundation/components/parsys"/>
                            </div>
                        </div>
                    </c:forEach>
            </c:when>
        </c:choose>
        <div class="copyRightWrapper">
            <div class="left">
            <cq:include path="copyrightInfo" resourceType="/apps/havells/components/common/richtexteditor"/></div>
            <div class="right">
                <cq:include path="privacyInfo" resourceType="/apps/havells/components/common/richtexteditor"/>
            </div>
        </div>
    </div>
</div>
<div style="clear:both"></div>