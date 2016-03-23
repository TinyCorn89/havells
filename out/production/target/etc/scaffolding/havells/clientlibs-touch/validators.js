(function(document, $, Granite) {
    "use strict";

    /*
     * Prices
     */
    $.validator.register({
        selector: "form [data-validation='havells.price']",
        validate: function(el) {
            var v = el.val();
            if (v.length > 0 && (!v.match(/^[\d\.]+$/) || isNaN(parseFloat(v)))) {
                return Granite.I18n.get("Must be a valid price.");
            }
        }
    });

    /*
     * Havells SKUs
     */
    $.validator.register({
        selector: "form [data-validation='havells.sku']",
        validate: function(el) {
            var v = el.val();
            if (v.length < 6) {
                return Granite.I18n.get("Havells SKUs must be at least 6 characters.");
            }else{
                /*var validationPath="/bin/validateSku.json?sku="+v;
                var buttonObj = $(".foundation-wizard-control.coral-Button.js-coral-Wizard-step-control.coral-Button--primary.coral-Wizard-nextButton");
                var buttonValue = buttonObj.data("action")+"";
                $.ajax({
                    url: validationPath,
                    method: "GET",
                    success:function(msg){
                        if(buttonValue=="next"){
                            buttonObj.show();
                        }
                    },
                    error:function(msg,errorCode){
                        if(buttonValue=="next"){
                            buttonObj.hide();
                        }
                    }
                });*/
            }
        }
    });

    /*
     * Havells Currencies
     */
    $.validator.register({
        selector: "form [data-validation='havells.currencyCode']",
        validate: function(el) {
            var v = el.val();

            if (!v.match(/^(USD)|(EUR)|(GBP)|(CHF)|(JPY)$/)) {
                return Granite.I18n.get("Havells supports only USD, EUR, GBP, CHF and JPY")
            }
        }
    })

})(document, Granite.$, Granite);
