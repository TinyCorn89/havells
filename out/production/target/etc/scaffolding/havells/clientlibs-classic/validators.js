jQuery(function ($) {

    /*
     * Prices
     */
    CQ.Ext.form.VTypes.price = function(value, field) {
        return value.length == 0 || !isNaN(parseFloat(value));
    };

    CQ.Ext.form.VTypes.priceText = CQ.I18n.getMessage("Must be a valid price.");
    CQ.Ext.form.VTypes.priceMask = /[\d\.]/;

    /*
     * Havells SKUs
     */
    CQ.Ext.form.VTypes.sku = function(value, field) {
        return value.length == 0 || value.length >= 6;
    };

    CQ.Ext.form.VTypes.skuText = CQ.I18n.getMessage("Havells Outdoors SKUs must be at least 6 characters.");

})(document, Granite.$, Granite);
