    $(".refineHeading span , .priceHeading span").click(function(e) {
        $(this).parent().toggleClass("active");
        var elem = $(this).parent().next(".searchFacets").children(".priceWrapperDiv");
        if(elem !== undefined)
            elem.slideToggle("slow");

        var elem1 = $(this).parent().siblings(".searchPar").children(".searchFacets").children(".priceWrapperDiv");
        if(elem1 != undefined)
            elem1.slideToggle("slow");

        var elem2 = $(this).parent().next(".priceContent");

        if(elem2 != undefined)
            elem2.slideToggle("slow");
    });

    function callPagination(path, perPageCounter, templateId, searchResultsId, isProductList){
        //After search results rendered, clear the right par content
        jQuery("#searchFacets").categoryListPagination({
            url: path,
            maxResults: perPageCounter,
            templateId: templateId,
            searchResultId: searchResultsId,
            isProductList: isProductList
        });
        //show the product compare alert
        showCompareAlerts();
    }
    function showCompareAlerts(){
        Havells.CompareProductObj.showProducts();
        Havells.CompareProductObj.initProductInfo("get", " ");
    }
    function clearRightParContent(){
        var rightPar = $(".twoColRight").find(".rightPar");
        if(rightPar.find(".productList").length < 1){
            rightPar.html("");
        }
    }
    var searchFacet = "";
    var filterCounter = 0;
    var ALL_FILTERS_SEPARATOR = ';';
    var FILTER_KEY_VALUE_SEPARATOR = '||';
    var checkboxValues = {};

    function addFacetToSearch(perPageCounter, tagValue, facetType, item, path, etcCommercePath){

        var filterType = facetType;
        var filterKeyValue = ( filterType.replace(" ", "") + FILTER_KEY_VALUE_SEPARATOR + tagValue.replace(" ", "") ).toLowerCase();

        var cookie = jQuery.cookie('filtersInfo-'+getCurrentPageTitle());
        if(cookie){
            checkboxValues = JSON.parse(cookie).checkBoxValues;
        }
        if(item.is(":checked")){
            if(searchFacet.indexOf(filterKeyValue) < 0)
                searchFacet = searchFacet + (filterCounter != 0? ALL_FILTERS_SEPARATOR :'') + filterKeyValue;
            else{
                if(filterCounter == 0){
                    searchFacet = searchFacet + filterKeyValue;
                }else{
                    searchFacet = searchFacet + ALL_FILTERS_SEPARATOR + filterKeyValue;
                }
            }
            filterCounter++;
        }else{
            if(searchFacet.indexOf(ALL_FILTERS_SEPARATOR + filterKeyValue) >= 0){
                searchFacet = searchFacet.replace(ALL_FILTERS_SEPARATOR + filterKeyValue, '');
            } else if(searchFacet.indexOf(filterKeyValue + ALL_FILTERS_SEPARATOR) >= 0) {
                searchFacet = searchFacet.replace(filterKeyValue + ALL_FILTERS_SEPARATOR, '');
            }else{
                searchFacet = searchFacet.replace(filterKeyValue, '');
            }
            filterCounter--;
        }
        checkboxValues[ $(item).attr("id")] = $(item).is(":checked");
        if(searchFacet !== "" && searchFacet !== undefined) {
              callPagination(path + "?filters=" + searchFacet + "&cpath=" + etcCommercePath, perPageCounter, "productListTemplate", "productResults", true);
        }else{
            //call function which loads default content.
            if($("#resPath").val() !== "" && $("#resPath").val() !== undefined){
                callPagination($("#resPath").val() + ".json" , perPageCounter, "productListTemplate", "productResults", true);
            }
        }
        persistCheckbox(path,etcCommercePath, perPageCounter);
    }

    function getCurrentPageTitle(){
         var url = location.href.replace(location.origin, "").replace("/cf#/","");
         return url.substring(url.lastIndexOf('/')+1).replace(".html", "");
    }

    function persistCheckbox(path,etcCommercePath, perPageCounter) {
        var cookie = {
            checkBoxValues : checkboxValues,
            path: path + "?filters=" + searchFacet + "&cpath=" + etcCommercePath,
            perPageCounter: perPageCounter,
            pageTitle: getCurrentPageTitle(),
            searchFacet: searchFacet
        };
        jQuery.cookie('filtersInfo-'+getCurrentPageTitle(), JSON.stringify(cookie) , { expires: 1, path: '/' });
    }
    function getJSONCookie(title){
        var cookie = jQuery.cookie('filtersInfo-'+title);
        if(cookie) {
            return JSON.parse(cookie);
        }
        return "";
    }
    function repopulateCheckboxes(cookieJSON) {
        if (cookieJSON.searchFacet){
            searchFacet = cookieJSON.searchFacet + ";";
            var checkboxValues = cookieJSON.checkBoxValues;
            if (checkboxValues) {
                Object.keys(checkboxValues).forEach(function (element) {
                    var checked = checkboxValues[element];
                    $("#" + element).prop('checked', checked);
                });
                callPagination(cookieJSON.path, cookieJSON.perPageCounter, "productListTemplate", "productResults", true);
            }
        }else{
            //call function which loads default content.
            if($("#resPath").val() !== "" && $("#resPath").val() !== undefined){
                callPagination($("#resPath").val() + ".json" , $("#productListPageCounter").val(), "productListTemplate", "productResults", true);
            }
        }

    }

