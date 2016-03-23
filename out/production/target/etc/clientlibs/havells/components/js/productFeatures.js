$.fn.checkImg = function () {
    $('div.detailFeatures .havellsBringLeft img').each(function (index) {
        if ($(this).attr("src") == "") {
            $('div.detailFeatures #havellsBringLeft' + index).hide();
            $(this).parent().next().addClass("fullWidth")
        }
    })
};
