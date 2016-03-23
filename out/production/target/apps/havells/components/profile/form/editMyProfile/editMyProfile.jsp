<%@include file="/apps/havells/global.jsp"%>
<c:set var="redirectUrl" value="${properties.redirect}"/>
<div class="signUpWrap noBg">
    <h1>${properties.formtitle}</h1>
<cq:include path="par" resourceType="foundation/components/parsys" />
</div>
<script>
$( "#editProfileImage" ).hide();
</script>
<c:if test="${!editMode}">
    <script>
        $(document).ready(function(){
            if($(location).attr('pathname').indexOf("/"+CQ_Analytics.ProfileDataMgr.data.authorizableId+"/")==-1){
                var url = "${redirectUrl}.html";
                $(location).attr('href',url);
            }
            $("#profile_dateOfBirth").datepicker({
                changeMonth: true,
                changeYear:true,
                yearRange: "1940:new Date().getFullYear()"
            });
        });
    </script>
</c:if>
