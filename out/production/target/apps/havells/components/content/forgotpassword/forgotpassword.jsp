<%@include file="/apps/havells/global.jsp" %>
<form class="forget_pwd_form" action="${properties.action}.html">
    <div class="forgotPwdWrap">

        <div class="forgotPwdMailNotSentMsg">
            <span class="internal-mail-error" style="color:red;"></span>

            <p>${properties.label}</p>

            <input name="user-id" type="text" class="input"/>
            <input name="token" type="hidden" class="input"/>
            <input type="hidden" id="subject" name="subject" value="${properties.subject}"/>
            <span class="forgotPwdMail" style="color:red;"></span>
        <span style="cursor:pointer;">
           <cq:include path="buttonWrapper" resourceType="havells/components/common/cta"/>
        </span>
        </div>
        <div class="forgotPwdMailSentMsg" style="display: none">
            <label id="mailSent" name="mailSent">${properties.mailSent}</label>
        </div>
    </div>

</form>
<script>
    $(document).ready(function () {
        var anchortag = $('.forgotPwdWrap .buttonGlbl a');
        $('.forgotPwdMailSentMsg').hide();
        anchortag.click(function () {
            sendEmail();
            return false;
        });
    });
</script>
<c:if test="${editMode}">
    <div style="clear:both;"></div>
</c:if>