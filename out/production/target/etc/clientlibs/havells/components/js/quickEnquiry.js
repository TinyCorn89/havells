var quickEnquiry = function () {
    $(".errorMsg").hide();
    $("#quickEnquiry").validate({
        rules: {
            userName: {
                required: true
            },
            phoneNo: {
                required: true,
                minlength: 10,
                number: true
            },
            email: {
                required: true,
                email: true
            },
            location: {
                required: true
            },
            description: "required"
        },
        messages: {
            userName: {
                required: "Please enter a username",
                minlength: "Your username must consist of at least 2 characters"
            },
            phoneNo: {
                required: "Please provide a phoneNO",
                minlength: "Your phoneNo must be at least 10 characters long",
                number: "Please enter correct Phone number"
            },
            location: {
                required: "Please provide a Location"

            },
            email: {
                email: "Please enter a valid email address",
                required: "Please enter a email Id"
            },
            description: "Please enter description"
        },
        submitHandler: function (form) {
            var userName = $("#userName").val();
            var email = $('#email').val();
            var phone = $('#phoneNo').val();
            var location = $('#location').val();
            var description = $('#description').val();
            $.ajax({
                async: false,
                type: "POST",
                data: {name: userName, emailId: email, phoneNumber: phone, location: location, description: description},
                url: "/bin/servlets/quickEnquiry",
                dataType: 'text',
                success: function (text) {
                    var lines = text;
                    if (lines == 'error=101') {
                        $("#userMsg").show();
                    }
                    else if (lines == 'error=500') {
                        $("#serverMsg").show();
                    }
                    else if (lines == 'success=1') {
                        $("#successMsg").show();

                    }
                }
            });
        }
    });
};
