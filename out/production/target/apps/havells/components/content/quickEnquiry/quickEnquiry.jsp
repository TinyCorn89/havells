<%@include file="/apps/havells/global.jsp" %>
<%@page session="false" %>
<div class="queryFormWrapper noDisplay" id="queryFormWrapper">
    Have a query? Please submit you details and
    someone from our team will contact you shortly
    <form id="quickEnquiry" method="POST" action="#">
        <input type="text" id="userName" placeholder="NAME*" name="userName" required>
        <input type="text" id="email" placeholder="EMAIL ID*" name="email" required>
        <input type="text" id="phoneNo" placeholder="PHONE NUMBER*" name="phoneNo" required>
        <input type="text" id="location" placeholder="LOCATION*" name="location" required>
        <input type="text" id="description" placeholder="DESCRIPTION*" name="description" required>
        <input type="submit" id="quickEnquire" value="Submit">
    </form>
    <div id="userMsg" class="errorMsg">${properties.userError}</div>
    <div id="serverMsg" class="errorMsg">${properties.serverError}</div>
    <div id="successMsg" class="errorMsg">Thankyou for your query. We will revert to you soon!!</div>
</div>
<script type="text/javascript">
    jQuery(document).ready(function () {
        quickEnquiry();
    });
</script>
