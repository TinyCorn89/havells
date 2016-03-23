var Havells = Havells || {};


// TODO : if compare more than 3, then don't let checkbox turn red
// TODO : category check as well
Havells.CompareProductObj = Havells.CompareProductObj || {
        productList: {},
        compareProductList: {},
        rangeTag: {},
        oneProduct: {}, existsProduct: {}, threeProducts: {},
        setWarningMessages: function(oneProduct, existsProduct, threeProducts){
            this.oneProduct = oneProduct;
            this.existsProduct = existsProduct;
            this.threeProducts = threeProducts;
        },
        setRangeTag: function(tag){
            this.rangeTag = tag;
        },
        setProductList: function (list) {
            this.productList = list;
        },
        getProductList: function () {
            return this.productList;
        },
        setCompareProductList: function (product) {
            this.compareProductList = product;
        },
        getCompareProductList: function () {
            return this.compareProductList;
        },
        addToCompareProductList: function (product) {
            this.compareProductList[product.skuId] = product;
        },
        isProductInCompareList: function (product) {
            console.log("product sku id :",product.skuId);
            for (var product1 in this.compareProductList) {
                if (!this.compareProductList.hasOwnProperty(product1))
                    continue;
                if (product1 === product.skuId) {
                    return true;
                }
            }

            return false;
        },
        compareWrapperClick: function () {
            $(".compareCross").click(function () {
                $(this).parents(':eq(1)').hide();
                $('.checkBoxClass').attr('checked', false);
                $('.errorMessage').css("display", "none");
            });
        },
        getProduct: function (skuId) {
            var list = this.productList.items;
            for (var product in list) {
                if (list[product].skuId == skuId) {
                    return list[product];
                }
            }
        },
        showChildDiv: function(){
            var childDiv = $('.tableDiv').children('.tableCellDiv');
            var counter = this.getLength();

            if (counter > 1) {
                $(".buttonGlbl").show();
            }
            if (counter >= 2)
                childDiv.hide();
        },
        addItemFromCompare: function(productId, context) {
            this.showCompareBlock();
            this.showChildDiv();
            var product = this.getProduct(productId);


            if (this.isProductInCompareList(product)) {
                // product already in list
                this.showErrorMessage(this.existsProduct);
            } else
            if ( this.getLength() >= 3) {
                // add to compareProductList
                this.showErrorMessage(this.threeProducts);
                this.unselectCheckBox(context);
            } else {
                this.addToCompareProductList(product);
            }
        },
        unselectCheckBox: function(context){
            $(context).prop('checked',false);
        },
        removeItemFromCompare: function(productId,context) {
            var productList = Havells.CompareProductObj.getCompareProductList();
            delete productList[productId];

            this.unselectCheckBox(context);
        },
        renderCompareStructure: function(compareProductList) {
            var compareProductList = this.getCompareProductList();
            var content = "";
            var counter = this.getLength();
            if (counter > 0) {
                $(".compareWrapperOuter").css("display", "block");
                $(".mainCompare").show();
            }else{
                $(".compareWrapperOuter").css("display", "none");
                $(".mainCompare").hide();
            }
            if ( counter > 1) {
                $(".buttonGlbl").show();
                $(".compareBtnDiv").removeClass('disable_a_href');
                $(".compareBtnDiv a").attr("href", $("#comparePageRedirect").val());
            } else if(counter == 1){
                $(".compareBtnDiv").addClass('disable_a_href');
                this.showErrorMessage($('#warningMessage').val());
            } else {
                $(".compareBtnDiv").removeClass('disable_a_href');
                $(".buttonGlbl").hide();
                $(".compareBtnDiv a").attr("href", "#");
            }

            for (var index in compareProductList) {
                if (!compareProductList.hasOwnProperty(index))
                    continue;
                var jsonData = {
                    "imagePath": compareProductList[index].imageWithRendition75_71,
                    "productTitle": compareProductList[index].title,
                    "originalPagePath": compareProductList[index].pagePath,
                    "price": compareProductList[index].price,
                    "skuId": compareProductList[index].skuId
                };
                content = content.concat( this.loadCookieData($("#compareTemplate").html(), jsonData));
            }
            $(".mainCompare .productInfo").html(content);
        },
        getLength: function(){
            return Object.keys(this.getCompareProductList()).length;
        },
        showErrorMessage: function(message) {
            $(".errorMessage").text(message).fadeIn(1500).fadeOut(1500);;
        },
        showCompareBlock: function() {
            $('.compareCross').parents(':eq(1)').show();
        },
        showProducts: function () {
            $(".addAnotherProduct").click(function (e) {
                $('body').animate({scrollTop: $(".garmentWrapper").offset().top}, 1000)
            });
        },
        loadCookieData: function(template, dataJson) {
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
        initProductInfo: function(selector, skuId,context) {
            console.log("product list",this.productList);
            $('#warningMessage').attr('value',this.oneProduct);
            var range = this.rangeTag;

            var compareListString = $.cookie("compareProductList");
            if (compareListString != undefined ) {
                var jsonString = JSON.parse(compareListString);

                if(jsonString.range === range) {
                    this.setCompareProductList(jsonString.items);
                }else{
                    this.setCompareProductList({});
                }
                if (this.getLength() > 0)
                    this.showCompareBlock();

                this.showChildDiv();
            }

            if (selector == "add") {
                this.addItemFromCompare(skuId, context);
            } else if (selector == "remove") {
                this.removeItemFromCompare(skuId,context);
            }
            this.renderCompareStructure();

            var childDiv = $('.tableDiv').children('.tableCellDiv');
            var counter = this.getLength();

            if (counter > 2)
                childDiv.hide();
            else
                childDiv.show();
            var obj = {
                range : range,
                items: this.getCompareProductList(),
                length: this.getLength()
            };
            try {

                $.cookie("compareProductList", JSON.stringify( obj ) , {path:'/'} );
            } catch (ex) {
                console.log("ex", ex);
            }
        }
    };