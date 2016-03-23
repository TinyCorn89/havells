;(function($){
    "use strict";

    $.fn.displayRating = function(opts) {
        var options = $.extend({ }, opts);
        return this.each(function() {
            var $item = $(this);
            var uri = "/bin/productRating.html";
            $.ajax({
                type: "GET",
                url: uri,
                data: {
                    productPath: encodeURIComponent(options.productPath)
                },
                success: function(data) {
                    var averageRatingWidth = data*20+"%";
                    $item.find('.jRatingColor').css("width",averageRatingWidth);
                }
            });
        });
    }
})(jQuery);
