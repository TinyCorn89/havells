var VariantsDataUtil = {

    DOM_INFO : {
        PRODUCT_INDEX : -1,
        VARIANT_DIV : "",
        DEFAULT_DIV : "",
        DEFAULT_DIV_FOR_HTML : "",
        CONTENT : "",
        VARIANT_PRODUCT_TITLE_INDEX : 0,
        DEFAULT_PRODUCT_TITLE_INDEX : 1
    },
    setTechSpecs: function(productJsonData){
        this.DOM_INFO.PRODUCT_INDEX = 1;
        var techSpecTextContent = "";
        if (productJsonData.techSpecsKeys.length) {
            var content = "";
            for (var index = 0; index < productJsonData.techSpecsKeys.length; index++) {
                var key = productJsonData.techSpecsKeys[index];
                var feature = productJsonData.technicalSpecs["heading-" + index];
                var json = {
                    "key": key,
                    "features": feature
                };
                content = content + this.compileTemplate(json, $("#techSpecsTemplate").html());
            }
            techSpecTextContent = content;
        }
        var techSpecImageJson = {"techSpecsImages": productJsonData.techSpecsImages};
        var techSpecImageContent = this.compileTemplate(techSpecImageJson, $("#techSpecsImagesTemplate").html());

        var isProdFeatureJsonEmpty = false;
        if(techSpecTextContent == "" && techSpecImageContent == ""){
            isProdFeatureJsonEmpty = true;
        }
        
        if(this.isDefaultProdFeaturePresent(this.DOM_INFO.PRODUCT_INDEX)){
                if(isProdFeatureJsonEmpty){
                    this.hideProductFeatTitle(this.DOM_INFO.PRODUCT_INDEX);
                }else{
                    this.showProductFeatTitle(this.DOM_INFO.DEFAULT_PRODUCT_TITLE_INDEX);
                }
                $("#techSpecsDetails").html(techSpecTextContent);
                $("#techSpecsImages").html(techSpecImageContent);
        }else{
                if(isProdFeatureJsonEmpty){
                    this.hideProductFeatTitle(this.DOM_INFO.PRODUCT_INDEX);
                    $("#technicalSpecsTab").css('display','none');
                }else{
                    this.showProductFeatTitle(this.DOM_INFO.VARIANT_PRODUCT_TITLE_INDEX);
                    $("#technicalSpecsTab").css('display','block');
                }
                $("#technicalSpecsTabText").html(techSpecTextContent);
                $("#technicalSpecsTabImage").html(techSpecImageContent);
        }
    },
    setAccessories: function(productJsonData){
        var json = {"accessoryFeature" : productJsonData.accessoryFeature};


        this.DOM_INFO.DEFAULT_DIV = "originalAccessoriesFeatures";
        this.DOM_INFO.VARIANT_DIV = "accessoriesTab";
        this.DOM_INFO.DEFAULT_DIV_FOR_HTML = "accesoriesRender";
        this.DOM_INFO.CONTENT = this.compileTemplate(json, $("#accessoryTemplate").html());
        this.DOM_INFO.PRODUCT_INDEX = 3;

        if(productJsonData.accessoryFeature.length){
            this.setProductFeatureIfPresent();
        }else{
            this.hideCorrectDiv();
        }
    },
    setTechnicalDrawing: function (productJsonData) {
        var json = {"technicalDrawing": productJsonData.technicalDrawing};

        this.DOM_INFO.DEFAULT_DIV = "originalTechnicalDrawing";
        this.DOM_INFO.VARIANT_DIV = "technicalDrawingTab";
        this.DOM_INFO.DEFAULT_DIV_FOR_HTML = "technicalDrawing";
        this.DOM_INFO.CONTENT = this.compileTemplate(json, $("#techImagesTemplate").html());
        this.DOM_INFO.PRODUCT_INDEX = 2;

        /*checking the size of array*/
        if (productJsonData.technicalDrawing.length) {
            this.setProductFeatureIfPresent();
        } else {
            this.hideCorrectDiv();
        }
    },
    setFAQContent: function (productJsonData) {

        this.DOM_INFO.DEFAULT_DIV = "originalFaqs";
        this.DOM_INFO.VARIANT_DIV = "faqsTab";
        this.DOM_INFO.DEFAULT_DIV_FOR_HTML = "faqsContent";
        this.DOM_INFO.CONTENT = productJsonData.faqs;
        this.DOM_INFO.PRODUCT_INDEX = 4;

        if (this.DOM_INFO.CONTENT.length != 0) {
            this.setProductFeatureIfPresent();
        } else {
            this.hideCorrectDiv();
        }
    },
    setQuickFeature: function (productJsonData) {
        var json = {
            "price": productJsonData.productInfo.price,
            "offerPrice": productJsonData.productInfo.offerPrice,
            "quickFeature": productJsonData.quickFeature
        };
        $("#productManual").find("a").attr("href", productJsonData.productInfo.productManual);
        $("#productBrochure").find("a").attr("href", productJsonData.productInfo.productBrochure);
        $("#productTitle").html(productJsonData.productInfo.title);
        $("#productWrapper").html(this.compileTemplate(json, $("#quickFeatureTemplate").html()));
    },
    setSliderImages: function (productJsonData) {
        var json = {
            "productImage": productJsonData.productImage,
            "productImages": productJsonData.productImages
        };
        $("#threeSliderImages").html(this.compileTemplate(json, $("#sliderTemplate").html()));
        $("#productMainDiv").html(this.compileTemplate(json, $("#zoomSliderTemplate").html()));

        var $threeSlider = $('.threeSlider');
        bindThreeSlider($threeSlider);

        var threeSlider = $(".threeSlider .threeSliderInner a");
        bindThreeSliderInner(threeSlider);

        var variantSlider = $('.variantSlider');
        bindSlider(variantSlider);
        $('.productMainDiv .slick-slider').slick({
            dots: false,
            arrows: false,
            slidesToShow: 1,
            infinite: false,
            draggable: false
        });
        $('.productMain').zoom();

    },
    setDetailsFeature: function (productJsonData) {

        var json = {"detailsFeatures": productJsonData.detailsFeatures};
        this.DOM_INFO.DEFAULT_DIV = "originalDetailsFeatures";
        this.DOM_INFO.VARIANT_DIV = "detailTab";
        this.DOM_INFO.DEFAULT_DIV_FOR_HTML = "detailFeature";
        this.DOM_INFO.CONTENT = this.compileTemplate(json, $("#detailFeatureTemplate").html());
        this.DOM_INFO.PRODUCT_INDEX = 0;

        if (productJsonData.detailsFeatures.length) {
            this.setProductFeatureIfPresent();
        } else {
            this.hideCorrectDiv();
        }
    },
    setColorVariants: function (productJsonData) {
        var colorVariants = productJsonData.colorVariants;
        var json = {"colorVariants": colorVariants};
        $('div#variantsColor').html(this.compileTemplate(json, $("#colorWrapperTemplate").html()));
    },
    compileTemplate: function (jsonData, template) {
        var content = "";
        try {
            dust.loadSource(dust.compile(template, 'info'));
            dust.render('info', jsonData, function (err, out) {
                if (out !== undefined) {
                    content = out;
                }
            });
        } catch (ex) {
            console.log("error occurs in compiling " + ex);
        }
        return content;
    },
    isDefaultProdFeaturePresent: function (childNumber) {

        if ($('div.productFeatures.page.section:eq(' + childNumber + ') .featuresAccWrapper').length == 1) {
            return false;
        } else {
            return true;
        }
    },
    hideProductFeatTitle: function(childNumber){
        $('div.productFeatures.page.section:eq(' + childNumber + ') .featuresAccWrapper .featureClicker').css('display','none');
    },
    showProductFeatTitle: function(featureIndex){
        $('div.productFeatures.page.section:eq(' + featureIndex + ') .featuresAccWrapper .featureClicker').css('display','block');
    },
    hideThisId: function (idToHide) {
        $('div#' + idToHide).hide();
    },
    setProductFeatureIfPresent: function () {
        if (this.isDefaultProdFeaturePresent(this.DOM_INFO.PRODUCT_INDEX)) {
            if ($('div#' + this.DOM_INFO.DEFAULT_DIV).css('display') == 'none') {
                $('div#' + this.DOM_INFO.DEFAULT_DIV).show();
            }
            $("#" + this.DOM_INFO.DEFAULT_DIV_FOR_HTML).html(this.DOM_INFO.CONTENT);
        } else {
            $('div#' + this.DOM_INFO.VARIANT_DIV).show();
            $("#" + this.DOM_INFO.VARIANT_DIV + "1").html(this.DOM_INFO.CONTENT);
        }
    },
    hideCorrectDiv: function () {
        if (this.isDefaultProdFeaturePresent(this.DOM_INFO.PRODUCT_INDEX)) {
            this.hideThisId(this.DOM_INFO.DEFAULT_DIV);
        } else {
            this.hideThisId(this.DOM_INFO.VARIANT_DIV);
        }

    }
};
function bindSlider(variantSlider) {
    if (variantSlider != undefined) {
        variantSlider.slick({
            dots: false,
            arrows: true,
            slidesToShow: 4,
            infinite: false,
            responsive: [
                {
                    breakpoint: 480,
                    settings: {
                        slidesToShow: 3,
                        slidesToScroll: 1
                    }
                },
                {
                    breakpoint: 768,
                    settings: {
                        slidesToShow: 2,
                        slidesToScroll: 1
                    }
                }
            ]
        });
    }
}
function subNavBindingSlider(slideInner) {
    if (slideInner != undefined) {
        slideInner.slick({
            dots: false,
            arrows: true,
            slidesToShow: 7,
            infinite: true,
            responsive: [
                {
                    breakpoint: 1024,
                    settings: {
                        slidesToShow: 5,
                        slidesToScroll: 1,
                        infinite: true,
                        dots: false
                    }
                },
                {
                    breakpoint: 600,
                    settings: {
                        dots: false,
                        slidesToShow: 4,
                        slidesToScroll: 1
                    }
                },
                {
                    breakpoint: 480,
                    settings: {
                        slidesToShow: 2,
                        slidesToScroll: 1
                    }
                }
            ]
        });
    }
}

var globalIndex = -1;

function setAllVariantsData(clickedObject) {

    var path = clickedObject.attr('id');
    var colorAttr = clickedObject.attr('data-iscolor');
    var isColorVariant= false;
    accordionCheck.resetAccordionCounter();
    if(colorAttr){
        var clickedIndex = clickedObject.attr('index');
        if (clickedIndex != globalIndex) {
            globalIndex = clickedIndex;
            isColorVariant = true;
            ajaxCallForAllVariants(path,isColorVariant)
        }
    }else{
        globalIndex = -1;
        ajaxCallForAllVariants(path,isColorVariant);
    }
}

function ajaxCallForAllVariants(path,isColorVariant){
    $.ajax({
        url: '/bin/variantInfo.json',
        type: 'GET',
        data: {path: path},
        contentType: 'application/json; charset=utf-8',
        success: function (response) {
            var productJsonData = JSON.parse(response);
            if (productJsonData != "undefined" && productJsonData != null) {
                VariantsDataUtil.setQuickFeature(productJsonData);
                VariantsDataUtil.setSliderImages(productJsonData);
                if (!isColorVariant) {
                    VariantsDataUtil.setColorVariants(productJsonData);
                }
                VariantsDataUtil.setDetailsFeature(productJsonData);
                accordionCheck.incrementAccordionCount(productJsonData.detailsFeatures.length != 0);
                VariantsDataUtil.setTechnicalDrawing(productJsonData);
                accordionCheck.incrementAccordionCount(productJsonData.technicalDrawing.length != 0);
                VariantsDataUtil.setTechSpecs(productJsonData);
                accordionCheck.incrementAccordionCount(productJsonData.techSpecsKeys.length != 0);
                VariantsDataUtil.setFAQContent(productJsonData);
                accordionCheck.incrementAccordionCount(productJsonData.faqs.length != 0);
                VariantsDataUtil.setAccessories(productJsonData);
                accordionCheck.incrementAccordionCount(productJsonData.accessoryFeature.length != 0);
                var acc = $(".featureClicker a");
                if(acc != undefined){
                    // closes all accordions first and then check if to open them on basis of number of non empty accordions
                    accordionCheck.closeAllAccordions().isSingleAccordion();
                }
            } else {
                console.log("error in getting variants data");
            }
            $().checkImg();
        },
        error: function () {
            console.log("Error during the ajax call");
        }
    });
}

$(function () {
    $('.productMain').zoom();
    var productMainDivSlider = $('.productMainDiv .slick-slider');
    if (productMainDivSlider != undefined) {
        productMainDivSlider.slick({
            dots: false,
            arrows: false,
            slidesToShow: 1,
            infinite: false,
            draggable: false
        });
    }

    $(".productMainDivLeft ul li a").click(function () {
        console.log("clicked");
        var tabIndex = $(this).parent().index();
        console.log("tabIndex " + tabIndex);
        $(".productMainDiv .slick-slider").slickGoTo(tabIndex);
        $(".productMainDivLeft ul li").removeClass("active");
        $(this).parent().addClass("active");
    })
});

$(function () {
    var threeSlider = $(".threeSlider .threeSliderInner a");
    bindThreeSliderInner(threeSlider);
});

function bindThreeSliderInner(threeSlider) {
    if (threeSlider != undefined) {
        threeSlider.click(function () {
            $(".productMainDiv").show();
            $(".three360Inner").hide();
            var tabIndex = $(this).parent().index();
            $(".productMainDiv .slick-slider").slickGoTo(tabIndex);
            $(".threeSliderInner a").removeClass("active");
            $(this).addClass("active");
        });
    }
}

$(function () {
    var $threeSlider = $('.threeSlider');
    bindThreeSlider($threeSlider);
});
function bindThreeSlider($threeSlider) {
    if ($threeSlider != undefined) {
        $threeSlider.slick({
            dots: false,
            arrows: true,
            slidesToShow: 3,
            infinite: false,
            autoplay: false,
            draggable: false,
            vertical: true,
            responsive: [
                {
                    breakpoint: 1024,
                    settings: {
                        vertical: true,
                        slidesToShow: 3,
                        slidesToScroll: 1,
                        infinite: true,
                        arrows: true,
                        dots: true

                    }
                },
                {
                    breakpoint: 768,
                    settings: {
                        vertical: false,
                        slidesToShow: 3,
                        slidesToScroll: 1
                    }
                },
                {
                    breakpoint: 480,
                    settings: {
                        vertical: false,
                        slidesToShow: 1,
                        slidesToScroll: 1
                    }
                }
            ]
        });
    }
}

