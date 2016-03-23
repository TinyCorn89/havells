function resetPassword(token){
    var redirectTo404 = $("#redirectTo404").val();
    var redirectToAccessDenied = $("#redirectToAccessDenied").val();
    if(token) {
        $(".resetPasswordMessage").hide();
        $(".resetPassword").show();
        var user_id =  $("#mailid").attr('value');
        $.ajax({
            url: '/bin/havells/checkUserEmail?token='+token,
            dataType:"json",
            complete: function(xhr, textStatus) {
                if(xhr.status == responseStatusCodes.OK){
                    $('input[name="userid"]').val($.parseJSON(xhr.responseText).mailIds);
                }
                else if(xhr.status == responseStatusCodes.UNAUTHORIZED) {
                    var url = redirectToAccessDenied+".html";
                    $(location).attr('href', url);
                }
                else {
                    var url = redirectTo404+".html";
                    $(location).attr('href', url);
                }
            }
        });
    }else{
        $(".resetPassword").hide();
        $(".resetPasswordMessage").show();
    }
}

