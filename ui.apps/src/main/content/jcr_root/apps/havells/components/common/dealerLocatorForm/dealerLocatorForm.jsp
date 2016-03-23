<%@include file="/apps/havells/global.jsp" %>
<%@ page import="com.havells.core.model.FetchDealerDetails" %>
<%@ page import="java.util.UUID" %>

<c:if test="${not empty pageContext.request.queryString}">
    <c:set var="state" value="${param.state}"/>
    <c:set var="city" value="${param.city}"/>
</c:if>
<div class="formElementsWrapper">
    <c:set var="path" value="${not empty properties.contentPath ? properties.contentPath : \"/etc/havells/dealersdata\"}"/>
    <c:set var="dealerProductPath" value="${not empty properties.dealerProductPath ? properties.dealerProductPath : \"/etc/havells/dealerproducts\"}"/>
    <c:set var="dealerDetails"
           value='<%=new FetchDealerDetails((String)pageContext.getAttribute("path"), (String)pageContext.getAttribute("dealerProductPath"), resourceResolver)%>'/>
    <c:set var="stateInfo" value="${dealerDetails.sortedMap}"/>
    <div class="fieldWrap">
        <select name="" class="select state">
            <option>Select State</option>
            <c:forEach var="list" items="${stateInfo}">
                <c:choose>
                    <c:when test="${not empty state && state==list.value}">
                        <option class="formParameter" selected="selected" value="${list.key}">${list.value}</option>
                    </c:when>
                    <c:otherwise>
                        <option value="${list.key}">${list.value}</option>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </select>
    </div>
    <div class="fieldWrap">
        <select name="" class="select city">
            <option value="">Select City</option>
        </select>
    </div>
    <div class="fieldWrap noMarginRight">
        <select name="products" class="select products">
            <option value="Select Product">Select Product</option>
            <c:forEach var="product" items="${dealerDetails.dealerProducts}">
                 <option value="${product}">${product}</option>
            </c:forEach>
        </select>
    </div>
    <br/>
    <div class="fieldWrap submitButton">
        <input name="" type="button" class="submit locateDealer" value="Submit">
    </div>
    <br/>
    <div class="dealerResult"></div>
    <div class="fieldWrap">
        <c:choose>
            <c:when test="${editMode && empty properties.contentPath}">
                Please Enter the Root Content Path
            </c:when>
            <c:otherwise>
                <input type="hidden" value="${properties.contentPath}" class="path"/>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<div class="tableWrapper hideDealer">
    <div class="tableHolder">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" class="dealerLocator dealerTable">
            <thead>
            <tr>
                <th>S.No.</th>
                <th>Store Name</th>
                <th>Address</th>
                <th>Contact No.</th>
                <th>Email</th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
    </div>
</div>
<c:set var="uuid" value="<%= UUID.randomUUID().toString() %>"/>
<div id="${uuid}" style="clear:both;"></div>
<script type="text/javascript">
    $(document).ready(function () {
        $('#${uuid}').closest('.dealerLocatorForm').dealerLocator($('.path').val());
        if ($("div#googleMap").length !== 0) {
            googleMapUtil.dealerLocator.initialize();
        }
    });
</script>
