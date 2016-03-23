function sendEmail(){
    var notExist = 'User does not exist.',
        internalError = 'Some internal error. Please retry later!',
        sEmail = $('.forgotPwdWrap .input').val(),
        subject = $("#subject").val(),
        mailSent = $("#mailSent").val(),
        selector = $('.forgotPwdWrap span.forgotPwdMail'),
        errorSel = $('span.internal-mail-error');
        if ($.trim(sEmail).length == 0) {
            errorSel.text('');
            selector.text(notValid);
        } else {
            var form = $('.forget_pwd_form');
            $.ajax({
                async: false,
                type: "POST",
                url: "/bin/servlets/ForgotPasswordServlet",
                data: form.serialize(),
                complete: function (jqr, code) {
                    if(jqr.status == responseStatusCodes.OK) {
                        $('.forgotPwdMailSentMsg').show();
                        $('.forgotPwdMailNotSentMsg').hide();
                        errorSel.text('');
                        selector.text('');
                    }
                    else if (jqr.status == responseStatusCodes.FORBIDDEN){
                        selector.text('');
                        errorSel.text(notExist);
                        $('.forgotPwdMailSentMsg').hide();
                        $('.forgotPwdMailNotSentMsg').show();
                    }else if(jqr.status == responseStatusCodes.INTERNAL_SERVER_ERROR){
                        selector.text('');
                        errorSel.text(internalError);
                        $('.forgotPwdMailSentMsg').hide();
                        $('.forgotPwdMailNotSentMsg').show();
                    }
                }
            });
            return false;
        }
}
