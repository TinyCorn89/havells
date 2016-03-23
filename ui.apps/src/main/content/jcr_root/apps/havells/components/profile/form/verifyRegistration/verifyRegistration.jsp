<%@page session="false"%>
<%@include file="/apps/havells/global.jsp"%>
<%
    String token = request.getParameter("c");
    String path = request.getParameter("l");
 %>
<script type="text/javascript">
    function verifyRegistration(token){

        var path=$("#path").val();
        var userCreated =  $("#userCreated").val();
        var userExist =  $("#userExist").val();
        var tokenExpired =  $("#tokenExpired").val();
        var tokennotExist =  $("#tokennotExist").val();
        var tokennotValid = $("#tokennotValid").val();
        var intermediatePath = $("#intermediatePath").val();
        $.ajax({
            url: '/bin/servlets/VerifyRegistrationServlet',
            dataType:"json",
            data: {   "c":token,
                "l":path,
                "intermediatePath":intermediatePath
            },
            success:function(item){

                if(item.message == 1){
                    $("#message").html(userCreated);
                    $("#verifyRegistrationForm").hide();
                }
                else if(item.message==2){
                    $("#message").html(userExist);
                    $("#verifyRegistrationForm").hide();
                }
                else if(item.message == 3){
                    $("#message").html(tokenExpired);
                    $("#verifyRegistrationForm").hide();
                }
                else {
                    $("#message").html(tokennotExist);
                    $("#verifyRegistrationForm").show();
                }
            }
        });
    }

    $(document).ready( function(){
        var token = $("#token").val();
        if(token !== undefined) {
            $("#verifyRegistrationForm").hide();
            verifyRegistration(token);
        }else{
            $("#verifyRegistrationForm").show();
        }
        $('.buttonGlbl').click(function(){
            token = $("#user-token").val();
            verifyRegistration(token);
        });
    });
</script>
 <input type="hidden" id="token"  name="token" value="<%=token%>" />
 <input type="hidden" id="path"  name="path" value="<%=path%>" />
 <input type="hidden" id="userExist"  value="${properties.userExist}" />
 <input type="hidden" id="userCreated"  value="${properties.UserCreatedMsg}" />
 <input type="hidden" id="tokenExpired"  value="${properties.tokenExpired}" />
 <input type="hidden" id="tokennotExist"  value="${properties.tokenNotExist}" />
 <input type="hidden" id="intermediatePath"  value="${properties.intermediatePath}" />
<b><p id="message"></p></b>
<form class="forget_pwd_form" id="verifyRegistrationForm" style="display: none" action="${properties.action}.html">
    <div class="forgotPwdWrap" >
        <p>${properties.label}</p>
        <input id="user-token" type="text" class="input"/>
        <span class="forgotPwdMail" style="color:red;"></span>
        <span style="cursor:pointer;">
           <cq:include path="buttonWrapper" resourceType="havells/components/common/cta"/>
        </span>
    </div>
</form>
