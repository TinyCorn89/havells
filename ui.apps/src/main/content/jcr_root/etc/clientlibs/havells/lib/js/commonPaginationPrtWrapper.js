;(function($) {
    "use strict";
    $.fn.doPaginationPrtCoverage = function(opts){
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
                getJsonData(ajaxUrl, "json", function(jsonData){
                    if(typeof jsonData !== "undefined" &&  jsonData !== null ){
                        if(jsonData.totalItems > 0 && typeof(jsonData.items) !=='undefined'){
                            finalJsonData = prepareYtDataArray(jsonData.items);
                            $search.find("#error-msg").hide();
                            pagePagination(finalJsonData.length);
                        }else{
                            $search.find("#searchResults").empty();
                            $search.find("#Pagination").hide();
                            $search.find("#error-msg").show();
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
                        'description': list[index].description,
                        'publicationdate': list[index].publicationdate,
                        'pubsubtitle': list[index].pubtitle,
                        'color':list[index].color,
                        'pubtitle': list[index].pubtitle,
                        'newWindow': list[index].newWindow,
                        'textOverlay': list[index].textOverlay,
                        'linkType': list[index].linkType,
                        'url':list[index].url
                    };
                }
                return data;
            }

            /**
             *
             * @param yt_url
             * @param dataType
             */
            function getJsonData(url, dataType, callback){
                $.ajax({
                    type: "GET",
                    url: url,
                    dataType: dataType,
                    success: function(response){
                        callback(response);
                    },
                    error : function(jqXHR, textStatus, e) {
                        console.log("error in getting json from youtube" +e);
                        return;
                    }
                });
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
                $("#Pagination").show();
                $(".mediaVideo span.playIcon").html("<em></em>");
                /* if all videos are displayed on single page,
                 disable pagination else keep it enable.
                 */
                return false;
            }
        });
    }
})(jQuery);
