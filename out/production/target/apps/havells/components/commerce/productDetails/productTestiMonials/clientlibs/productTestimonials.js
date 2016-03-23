;(function($){
    "use strict";

    $.fn.addTestimonial = function(opts) {
        var options = $.extend({ }, opts);
        return this.each(function() {
            var $item = $(this);
            var $form = $item.find('#writeTestimonial');
            var profilestore = CQ_Analytics.ClientContextMgr.getRegisteredStore("profile");
            var ratingSubmitted = "0";
            var $dropDown = $item.find('#filterBy');
            var filterBy = $dropDown.val();
            var productPath = options.productPath;
            var name = getClientContextData("username");
            var fullName = getClientContextData("formattedName");
            var user = CQ_Analytics.ProfileDataMgr.data;
            var avatar=user.path+"/photos/primary/image" ;

            if(fullName && fullName!=="anonymous"){
                $form.removeClass('anonymous_hidden');
            }

            $dropDown.on('change',function(){
                filterBy = $dropDown.val();
                reloadComponent();
            });

            $form.submit(function() {
                var comment = $item.find('#commentTestimonial').val().replace(/\s+/g," ").trim();
                var uri = "/bin/addTestimonialComment.html";
                $.ajax({
                    type: "POST",
                    url: uri,
                    data: {
                        comment: encodeURIComponent(comment),
                        rating: ratingSubmitted,
                        fullName: encodeURIComponent(fullName),
                        avatar: encodeURIComponent(avatar),
                        productPath: encodeURIComponent(productPath)
                    },
                    complete: function() {
                        reloadComponent();
                    }
                });
                return false;
            });

            $item.find('.productrating').jRating({
                bigStarsPath: "/etc/clientlibs/havells/image/stars.png",
                sendRequest: false,
                onClick:function(element,rate){
                    ratingSubmitted = rate;
                }
            });

            function getClientContextData(property){
                if(profilestore){
                    return profilestore.getProperty(property, false);
                } else {
                    return null;
                }
            }

            function  reloadComponent(){
                var $testimonialWrapper = $('.testimonialsIdentifier');
                var data_ajax_path = $testimonialWrapper.attr('data-ajax-path');
                $.get(data_ajax_path+".html?filter="+filterBy+"&productPath="+productPath,function(data){
                    $testimonialWrapper.parent().html(data);
                    var niceScroll = $('.nicescroll-rails');
                    $(niceScroll).each(function(counter, element){
                        $(element).remove();
                    });
                    $(".testimonialsDiv ").niceScroll({touchbehavior:false,autohidemode:false,cursorborder:"", cursorcolor:"#7f7f7f"});
                });
            }
        });
    }
})(jQuery);
