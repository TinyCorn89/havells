<%@include file="/apps/havells/global.jsp" %>
<%@page session="false" %>
<c:set var="selector" scope="page" value="<%= slingRequest.getRequestPathInfo().getSelectors()%>"/>
<div class="signUpWrap noBg">
    <h1>${properties.formtitle}</h1>
    <input type="hidden" id="userExistErrorMessage" value="${properties.userExistErrorMessage}"/>

    <div class="errorWrapper" style="color: #ff0000">

        <c:if test="${selector[0] == '500'}">
            ${properties.error}
        </c:if>
        <c:if test="${selector[0] == '600'}">
             ${properties.userExistErrorMessage}
        </c:if>
    </div>
    <cq:include path="par" resourceType="foundation/components/parsys"/>
</div>
<script>
    $(document).ready(function () {
        $('#signUpform_email').change(function () {
            var email = $('#signUpform_email').val();
            var error = $("#userExistErrorMessage").val();
            $.ajax({
                url: '/bin/servlets/VerifyUserNameServlet',
                type: 'GET',
                data: {email: email},
                contentType: 'application/json; charset=utf-8',
                success: function (response) {
                    if (response == 'true')
                        $('.errorWrapper').html(error);
                    else{
                        $('.errorWrapper').html("");
                    }

                },
                error: function () {
                    console.log("Error during the ajax call");
                }
            });
        });
        $("#signUpform_dateOfBirth").datepicker({
            changeMonth: true,
            changeYear:true,
            yearRange: "1940:new Date().getFullYear()"
        });
    });
</script>


