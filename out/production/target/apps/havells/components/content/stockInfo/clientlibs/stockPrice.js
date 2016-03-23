$(function () {

    var stockCookie = $.cookie("stockInfo");
    if(stockCookie != undefined){
        updateStockInfo(stockCookie);
    }else {
        var defaultStock = $(".stockClient").data("stock-id");
        if (defaultStock == "") {
            defaultStock = "NSE:Havells";
        }
        $.ajax({
            type: 'GET',
            url: window.location.protocol + "//finance.google.com/finance/info?client=ig&q=" + defaultStock,
            dataType: 'jsonp',
            success: function (data, textStatus, jqXHR) {
                try {
                    var obj = jQuery.parseJSON(JSON.stringify(data).replace("// ", ""));
                    var info = obj[0].l_fix;
                    updateStockInfo(info);
                    var todayDate = new Date();
                    todayDate.setTime(todayDate.getTime() + 10*60 * 1000);
                    $.cookie("stockInfo", info, {expires: todayDate, path : '/'});
                } catch (e) {
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                updateStockInfo("8484.0")
            }
        });
    }
    function updateStockInfo(info){
        $('.stockPrice').text(info);
        var upDownPrice = parseInt(info);
        if(upDownPrice < 0){
            $('.fa').first().removeClass('fa-arrow-up').addClass('fa-arrow-down');
            $('.nseValue').removeClass('up');
        }else{
            $('.fa').first().removeClass('fa-arrow-down').addClass('fa-arrow-up');
            $('.nseValue').addClass('up');
        }
    }
});
