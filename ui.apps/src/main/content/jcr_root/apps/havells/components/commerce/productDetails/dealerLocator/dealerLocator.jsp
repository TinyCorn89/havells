<%@ page import="com.havells.core.model.FetchDealerDetails" %>
<%@include file="/apps/havells/global.jsp" %>
<script type="text/javascript">
    function redirectLocatorPage() {
        var state = $(".state option:selected").text();
        var city = $(".city option:selected").text();
        if (state !== "Select City" && city !== "Select City") {
            window.location = "${properties.dealerFormPath}.html?state=" + state + "&city=" + city;
        }else{
            alert("choose state & city");
        }
        return false;
    }
</script>
<c:set var="path" value="${not empty properties.dealerContentPath ? properties.dealerContentPath : \"/etc/havells/dealersdata\"}"/>
<c:set var="stateInfo"
       value='<%=new FetchDealerDetails((String)pageContext.getAttribute("path"), resourceResolver).getSortedMap()%>'/>
<form action="#" onsubmit="return redirectLocatorPage();">
    <div class="locateDealerWrapper">
        <p>${not empty properties.locatorHeading ? properties.locatorHeading : "Where Can I Buy"}</p>
        <select name="state" class="select state">
            <option>Select State</option>
            <c:forEach var="list" items="${stateInfo}">
                <option value="${list.key}">${list.value}</option>
            </c:forEach>
        </select>
        <select name="city" class="select city">
            <option value="">Select City</option>
        </select>

        <div class="buttonGlbl">
            <input type="submit" name="locate" value="${not empty properties.locatorButton ? properties.locatorButton : "LOCATE"}"></div>
        <c:choose>
            <c:when test="${editMode && empty properties.dealerContentPath}">
                Please Enter the Root Content Path
            </c:when>
            <c:otherwise>
                <input type="hidden" value="${path}" class="path"/>
            </c:otherwise>
        </c:choose>
    </div>
</form>

<div class="registerProduct">
    <cq:include path="quickEnquiry" resourceType="/apps/havells/components/content/quickEnquiry"/>
</div>

<c:if test="${!editMode}">
    <div style="clear:both;"></div>
</c:if>
<script type="text/javascript">
    $(document).ready(function () {
        findDealer.getDealerData($('.path').val(),$(this));
    });
</script>
