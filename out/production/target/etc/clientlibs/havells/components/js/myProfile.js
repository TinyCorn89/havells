$(document).ready(function () {
    if (CQ_Analytics) {
        var user = CQ_Analytics.ProfileDataMgr.data;
        var path = user.path;
        if (!user || user.authorizableId == 'anonymous') {
            $(".master .myProfileWrap").hide();
            $(".master .anonymous").html("Please login to view your profile");
        } else if (user) {
            $(".myProfileWrap .fieldWrap .details.email").html(user.email);
            $(".myProfileWrap .firstname").html(user.givenName);
            $(".myProfileWrap .fieldWrap .details.name").html(user.givenName);
            $(".myProfileWrap .fieldWrap .details.surname").html(user.familyName);
            $(".myProfileWrap .fieldWrap .details.mobile").html(user.mobile);
            $(".myProfileWrap .fieldWrap .details.dateOfBirth").html(user.dateOfBirth);
            $(".myProfileWrap .fieldWrap .details.shippingAddress").html(user.shippingAddress);
            $(".myProfileWrap .fieldWrap .details.billingAddress").html(user.billingAddress);
            $(".myProfileWrap .fieldWrap.posRel .details").html(user.givenName);
            $.ajax({
                type: "GET",
                url: "/bin/profile",
                dataType: "json",
                data: { 'path': user.path + "/photos/primary/image"},
                success: function (data) {
                 $(".myProfileWrap .fieldWrap.posRel img").attr("src", data.path);
                },
                  error: function () {
                    console.log("error");
                }
            });
            var editPagePath = $(".myProfileWrap .buttonGlbl.edit a").attr("href");
            editPagePath = path + ".form.html" + editPagePath;
            $(".myProfileWrap .buttonGlbl.edit a").attr("href", editPagePath);
            CQ_Analytics.ProfileDataMgr.loadProfile(CQ_Analytics.ProfileDataMgr.data.authorizableId);
        }
    }
});
