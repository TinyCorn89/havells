;(function($) {
    "use strict";
    $.fn.doPagination = function(opts){
        var options = $.extend({
            url :'',
            maxResults : '',
            templateId : '',
            searchResultId : ''
        }, opts);
        return this.each(function() {
            var $search = $(this);
            var template = $("#"+options.templateId).html();
            initSearch();
            var  finalJsonData = [], items_per_page = options.maxResults;
            function initSearch(){
                //searching default text at the time of loading pages.
                var ajaxUrl = options.url;
                if (!ajaxUrl) return;
                CommonUtil.getJsonData(ajaxUrl, "json", function(jsonData){
                    if(typeof jsonData !== "undefined" &&  jsonData !== null ){
                        if(jsonData.totalItems > 0 && typeof(jsonData.items) !=='undefined'){
                            finalJsonData = prepareYtDataArray(jsonData.items);
                            /** init pagination of you tube videos on
                             * the page after formating data
                             */
                            pagePagination(finalJsonData.length);
                            if(finalJsonData.length/items_per_page <= 1){
                            $search.find('#Pagination').css("display","none");
                            } else{
                                $search.find('#Pagination').css("display","block");
                            }
                        }else{
                            $search.find('#searchResults').html("<b style='color:red'>no data found</b>");
                            $search.find('#Pagination').css("display","none");
                        }
                    }
                });
            }
              /**
             *
             * @param list
             * @return {Array}
             */
            function prepareYtDataArray(list){
                var data = [];
                console.log("list.length "+list.length);
                for(var index = 0; index < list.length; index = index +1){
                    data[index] = {
                        'label': list[index].label,
                        'imagePath': list[index].fileReference,
                        'linkUrl': list[index].anchorLink,
                        'mediaClass':list[index].mediaClass
                    };
                }
                return data;
            }

            /**
             *  This method is to invoke
             *  pagination plugin.
             * @param size
             */
            function pagePagination(size){
                try{
                    var optInit = {
                        items_per_page: items_per_page,
                        num_display_entries : items_per_page,
                        prev_text: $("#previous").val(),
                        next_text: $("#next").val(),
                        callback: pageSelectCallback
                    };
                    $("#Pagination").pagination(size, optInit);
                }catch (ex){
                }
            }

            function  pageSelectCallback(page_index, jq){
                var content = '';
                var max_elem = Math.min((page_index+1) * items_per_page, finalJsonData.length);
                // Iterate through a selection of the content and build an HTML string
                //pagingCount(page_index, max_elem);
                for(var i = page_index*items_per_page; i < max_elem; i++){
                    content =  content + CommonUtil.loadTemplate(finalJsonData[i], template);
                }
                $("#"+options.searchResultId).html(content);
                $(".mediaVideo span.playIcon").html("<em></em>");
                /* if all videos are displayed on single page,
                 disable pagination else keep it enable.
                 */
                return false;
            }
        });
    }
})(jQuery);
