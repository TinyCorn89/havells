;
var CommonUtil = CommonUtil || {
        getJsonData: function (url, dataType, callback) {
            $('#loading-image').show();
            jQuery.ajax({
                type: "GET",
                url: url,
                dataType: dataType,
                success: function (response) {
                    callback(response);
                },
                error: function (jqXHR, textStatus, e) {
                    console.log("error in getting json from youtube" + e);
                    return;
                },
                complete: function(){
                    $('#loading-image').hide();
                }
            });
        },
        loadTemplate: function (dataJson, template) {
            var content = "";
            try {
                dust.loadSource(dust.compile(template, 'info'));
                dust.render('info', dataJson, function (err, out) {
                    if (out !== undefined) {
                        content = out;
                    }
                });
            } catch (ex) {
            }
            return content;
        },
        /**
         * shows the testimonials on product details pages
         * @param filterType provides a filter for testimonial comments : can have two values either 'Recent' or 'Top Rated'(case sensitive)
         */
        showTestimonials: function(filterType){
            var $testimonialWrapper = $('.testimonialsIdentifier');

            var data_ajax_path = $testimonialWrapper.attr('data-ajax-path');
            var productPath = $testimonialWrapper.attr('product-path');

            /**
             * NOTE: this request shouldn't be cached on dispatcher
             * because of no extension in the end of suffix, won't be cached either.
             */
            var path = filterType != null ? data_ajax_path+".comments.html?filter="+filterType+"&productPath="+productPath : data_ajax_path+".comments.html?productPath="+productPath;

            $.get(path,function(data){
                $testimonialWrapper.find(".testominalWrapper").find(".testimonialsDiv").html(data);
                var niceScroll = $('.nicescroll-rails');
                $(niceScroll).each(function(counter, element){
                    $(element).remove();
                });
                $(".testimonialsDiv ").niceScroll({touchbehavior:false,autohidemode:false,cursorborder:"", cursorcolor:"#7f7f7f"});
            });
        }
    };
