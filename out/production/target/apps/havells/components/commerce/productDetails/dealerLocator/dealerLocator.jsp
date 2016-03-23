<%@include file="/apps/havells/global.jsp"%>
<script type="text/javascript">
    function redirectLocatorPage(){
        var city = $("#city").val();
        var pincode = $("#pincode").val();
        if(city !== "" && pincode !== ""){
            window.location = "${properties.dealerFormPath}.html?city="+city+"&pincode="+pincode;
        }
        return false;
    }
</script>
<form action="#" onsubmit="return redirectLocatorPage();">
    <div class="locateDealerWrapper">
        <p>${properties.locatorHeading}</p>
        <input type="text" name="city" id="city" placeholder="Enter your City" required="" class="validation-error"/>
        <span>OR</span>
        <input type="text" name="pincode" id="pincode" placeholder="Enter your Pincode" required="" class="validation-error"/>
        <div class="buttonGlbl">
         <input type="submit" name="locate" value="LOCATE"></div>
    </div>
</form>


<div class="registerProduct">
    <ul>
        <li><a href="javascript:;">REGISTER YOUR PRODUCT</a></li>
        <li><a href="#queryFormWrapper" class="queryFormWrapperFancy">QUICK ENQUIRY FORM</a></li>
    </ul>
   <cq:include path="quickEnquiry" resourceType="/apps/havells/components/content/quickEnquiry"/>
</div>

<c:if test="${!editMode}">
    <div style="clear:both;"></div>
</c:if>
