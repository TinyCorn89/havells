$(document).ready(function () {
    $("#checkPinAvailability").click(function (event) {
        event.preventDefault();
        var pin = $("#pin").val();
        var pinloc = $("#pinloc").val();
        var codAvailable = $("#codAvailable").val();
        var codUnavailable = $("#codUnavailable").val();
        var pinNotAvailable = $("#pinNotAvailable").val();

        $.ajax({
            type: "Get",
            url: "/bin/pinChecker?pin=" + pin + "&pinloc=" + pinloc,

            success: function (data) {
                var jsonData = JSON.parse(data);
                if (jsonData.message == "available" && jsonData.cod == true)
                    $('.result').html(codAvailable);
                else if (jsonData.message == "available" && jsonData.cod == false)
                    $('.result').html(codUnavailable);
                else
                    $('.result').html(pinNotAvailable);
            },
            error: function () {
                console.log("There is some error.");
            }
        });

    });
});

