;(function($) {
    "use strict";
    $.fn.jobListPagination = function(opts){
        var options = $.extend({
            url :'',
            maxResults : '',
            templateId : '',
            searchResultId : '',
            filter : ''
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

                        }else{
                            //showMsgPopUp();
                            console.log("No data found");
                            return;
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
                            'count': list[index].count,
                            'branch': list[index].branch,
                            'product':list[index].product,
                            'experience':list[index].experience,
                            'location':list[index].location,
                            'position':list[index].position,
                            'hashOfPosition':list[index].hashOfPosition,
                            'industryPrefrences':list[index].industryPrefrences,
                            'path':list[index].path
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
                    $("#jobPagination").pagination(size, optInit);
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
                if(i>=items_per_page) {
                   $("#jobPagination").show();
                    if($('ul[id="jobPagination"] li:nth-child(2) a').attr("class") === "active ") {
                        $(".prev").hide();
                          }
                    if($('ul[id="jobPagination"] li:nth-last-child(2) a').attr("class") === "active ") {
                        $(".next").hide();
                       }
                } else {
                    $("#jobPagination").hide();
                }
                /* if all videos are displayed on single page,
                 disable pagination else keep it enable.
                 */
                return false;
            }
        });
    }
})(jQuery);

