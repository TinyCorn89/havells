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

    var ALL_FILTERS_SEPARATOR = ';';
    var FILTER_KEY_VALUE_SEPARATOR = '||';
    var checkboxValues = {}, selectedFilterFlag = false;
    var map = {};

    function initFiltersMap(facetType, tagValue, isChecked){
        if (Object.keys(map).length > 0) {
            if (map[facetType]) {
                var existingTagValArr = map[facetType];
                if (isChecked) {
                    existingTagValArr.push(tagValue);
                } else {
                    existingTagValArr.splice(existingTagValArr.indexOf(tagValue), 1);
                    if (existingTagValArr.length == 0) {
                        delete map[facetType];
                    } else {
                        map[facetType] = existingTagValArr;
                    }
                }
            } else {
                map[facetType] = new Array(tagValue);
            }
        } else {
            map[facetType] = new Array(tagValue);
        }

    }
    function addFacetToSearch(perPageCounter, tagValue, facetType, item, path, etcCommercePath){

        var isChecked = item.is(':checked');
        initFiltersMap(facetType, tagValue, isChecked);

        var searchFacet = "", pageUrl = location.href;

        $.each(map,function (key, value) {
            searchFacet = searchFacet + key + FILTER_KEY_VALUE_SEPARATOR + value.toString() + ALL_FILTERS_SEPARATOR;
        });

        var cookie = jQuery.cookie('filtersInfo-'+getCurrentPageTitle());
        if(cookie){
            checkboxValues = JSON.parse(cookie).checkBoxValues;
        }

        checkboxValues[ $(item).attr("id")] = $(item).is(":checked");

        if(searchFacet !== "" && searchFacet !== undefined) {
            addFilterInUrl(pageUrl);
            callPagination(path + "?filters=" + searchFacet + "&cpath=" + etcCommercePath, perPageCounter, "productListTemplate", "productResults", true);
        }else{
            //call function which loads default content.
            checkboxValues = {};
            removeFilterUrl(pageUrl);
            reloadNonProductListPage();
            productListDefaultCall( callPagination );
        }
        persistCheckbox(path,etcCommercePath, perPageCounter, searchFacet);
    }

    function addFilterInUrl(pageUrl){
        if (pageUrl.indexOf("filter") == -1) {
            pageUrl = pageUrl.replace("html", "filter.html");
            window.history.pushState({}, getCurrentPageTitle(), pageUrl);
        }
    }
    function removeFilterUrl(pageUrl){
        pageUrl = pageUrl.replace(".filter", "");
        window.history.pushState({}, getCurrentPageTitle(), pageUrl);
    }

    function reloadNonProductListPage(){
          if ($("#isProductListTemplate").val() === "false") {
               location.reload();
          }
    }

    function getCurrentPageTitle(){
        var url = location.href.replace(location.origin, "").replace("/cf#/","");
        return url.substring(url.lastIndexOf('/')+1).replace(".html", "").replace(".filter","");
    }

    function persistCheckbox(path,etcCommercePath, perPageCounter, searchFacet) {
        var cookie = {
            checkBoxValues : checkboxValues,
            path: path + "?filters=" + searchFacet + "&cpath=" + etcCommercePath,
            perPageCounter: perPageCounter,
            pageTitle: getCurrentPageTitle(),
            map: JSON.stringify(map)
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
            console.log("populate check boxes on refresh on page..")
            checkForCookie(cookieJSON, productListDefaultCall );
    }

    function performCheckBoxOperation( cookieJSON, callback ){
        var checkboxValues = cookieJSON.checkBoxValues, isFilterFound = false;
            Object.keys(checkboxValues).forEach(function (element) {
                var checked = checkboxValues[element];
                $('.searchFacets input[id="'+element+'"]').prop('checked', checked);
                if(checked) isFilterFound = true;
            });
            console.log("filters found " + isFilterFound);
            //load results if searchFilter found in cookie.
            if(isFilterFound) {
                addFilterInUrl(location.href);
                callback(cookieJSON.path, cookieJSON.perPageCounter, "productListTemplate", "productResults", true);
            }
    }

    function productListDefaultCall( callback ){
        //call function which loads default content.
        if($("#resPath").val() !== "" && $("#resPath").val() !== undefined){
            callback($("#resPath").val() + ".json" , $("#productListPageCounter").val(), "productListTemplate", "productResults", true);
        }
    }


    function checkForCookie(cookieJSON, callback ){
        if (cookieJSON.map && Object.keys(cookieJSON.checkBoxValues).length !== 0){
            map = JSON.parse(cookieJSON.map);
            performCheckBoxOperation( cookieJSON, callPagination );
        }else{
            callback( callPagination );
        }
    }

