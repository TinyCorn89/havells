<%@include file="/apps/havells/global.jsp" %>
<%@ page import="java.util.UUID" %>
<div class="formElementsWrapper">
    <div class="fieldWrap">
        <select name="" class="select state">
            <option value="">Select State</option>
        </select>
    </div>
    <div class="fieldWrap">
        <select name="" class="select city">
            <option value="">Select City</option>
        </select>
    </div>
    <div class="fieldWrap">
        <input name="" type="button" class="submit locateDealer" value="Submit">
    </div>
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
<c:set var="uuid" value="<%= UUID.randomUUID().toString() %>" />
<div id="${uuid}" style="clear:both;"></div>
<script type="text/javascript">
    $(document).ready(function() {
        $('#${uuid}').closest('.dealerLocatorForm').dealerLocator($('.path').val());
    });
</script>
