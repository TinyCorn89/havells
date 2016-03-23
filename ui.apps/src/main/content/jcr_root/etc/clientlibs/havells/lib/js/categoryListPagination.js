;(function($) {
    "use strict";
    $.fn.categoryListPagination = function(opts){
        var options = $.extend({
            url :'',
            maxResults : '',
            templateId : '',
            searchResultId : '',
            isProductList : false
        }, opts);
        return this.each(function() {
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
                      if (options.isProductList) {
                                Havells.CompareProductObj.setProductList(jsonData);
                            }
                            finalJsonData = prepareYtDataArray(jsonData.items);
                            pagePagination(finalJsonData.length);
                        }else{
                            clearRightParContent();
                            if ($("#noSearchResultMsg")) {
                                $("#" + options.searchResultId).html($("#noSearchResultMsg").val());
                            }
                            $(".Pagination").hide();
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
                for(var index = 0; index < list.length; index = index +1){
                    if(options.isProductList){
                        data[index] = {
                            'pagePath': list[index].pagePath,
                            'imagePath': list[index].imagePath,
                            'title':list[index].title,
                            'subTitle':list[index].subTitle,
                            'price':list[index].price,
                            'pageId':list[index].pageId,
                            'originalPagePath' : list[index].originalPagePath ,
                            'imageWithRendition':list[index].imageWithRendition,
                            'skuId':list[index].skuId
                        };
                    } else{
                        data[index] = {
                            'pagePath': list[index].pagePath,
                            'imagePath': list[index].imagePath,
                            'pageName':list[index].pageName,
                            'originalPagePath' : list[index].originalPagePath,
                           'imageWithRendition':list[index].imageWithRendition
                        };
                    }
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
                    $(".Pagination").pagination(size, optInit);
                }catch (ex){
                }
            }
            /**
             *
             * @param dataJson
             * @return {null}
             */

            function  pageSelectCallback(page_index, jq){
                var content = '';
                var max_elem = Math.min((page_index+1) * items_per_page, finalJsonData.length);
                // Iterate through a selection of the content and build an HTML string
                for(var i = page_index*items_per_page; i < max_elem; i++){
                    content =  content + CommonUtil.loadTemplate(finalJsonData[i], template);
                }
                clearRightParContent();
                $("#"+options.searchResultId).html(content);

                adjustProductListingBoxes();

                if(i >= items_per_page) {
                    if(finalJsonData.length == items_per_page){
                        $(".Pagination").hide();$(".prev").hide();$(".next").hide();
                    }else{
                        $(".Pagination").show();
                        if($('ul[class="Pagination"] li:nth-child(2) a').attr("class") === "active ") {
                            $(".prev").hide();
                        }
                        if($('ul[class="Pagination"] li:nth-last-child(2) a').attr("class") === "active ") {
                            $(".next").hide();
                        }
                    }
                } else {
                    $(".Pagination").hide();
                }
                return false;
            }
        });
    }
})(jQuery);

/**
 * Adjust productlisting each product box height.
 * This also does same thing in search product listing.
 */
function adjustProductListingBoxes() {
    var prodDetailsDivHeight = 0, prodFullNameDivHeight = 0;
    $(".garmentWrapper .garmentDiv li").each(function (index, element) {
        var detailDivHeight = $(this).find(".detailsOfProd").height();
        if (detailDivHeight > prodDetailsDivHeight) {
            prodDetailsDivHeight = detailDivHeight;
        }
        var nameDivHeight = $(this).find(".productFullName").height();
        if (nameDivHeight > prodFullNameDivHeight) {
            prodFullNameDivHeight = nameDivHeight;
        }
    });
    $(".garmentDiv li").each(function (index, element) {
        $(this).find(".detailsOfProd").height(prodDetailsDivHeight);
        $(this).find(".productFullName").height(prodFullNameDivHeight);
    });
}
