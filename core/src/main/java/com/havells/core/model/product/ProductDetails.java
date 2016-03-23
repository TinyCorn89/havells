package com.havells.core.model.product;

import com.havells.util.RenditionUtil;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import javax.jcr.RepositoryException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class ProductDetails {

    private Resource resource;
    private ValueMap properties;

    public ProductDetails() { /* EMPTY CONSTRUCTOR*/ }

    public ProductDetails(Resource resource) {
        this.resource = resource;
        if (resource != null) {
            this.properties = resource.adaptTo(ValueMap.class);
        }
    }
    public String getDefaultImage(){
        if(resource != null){
            Resource imageResource = resource.getChild("image");
            if(imageResource != null){
                return imageResource.adaptTo(ValueMap.class).get("fileReference", "");
            }
        }
        return "#";
    }
    public String getSmallCoverImage(){
        return RenditionUtil.getRendition(getDefaultImage(), resource.getResourceResolver(), ProductConstant.THUMBNAIL_RENDITION_150X143);
    }
    public String getProductManualUrl() {
        return (properties != null) ? properties.get(ProductConstant.PRODUCT_MANUAL_DOC_PROP, "#") : "#";
    }

    public String getProductBrochureUrl() {
        return (properties != null) ? properties.get(ProductConstant.PRODUCT_BROCHURE_PROP, "#") : "#";
    }

    public String getPageTitle() {
        return (properties != null) ?
                (properties.get(ProductConstant.JCR_TITLE_PROP, "").equals("") ? resource.getName(): properties.get(ProductConstant.JCR_TITLE_PROP, "")) : "";
    }

    public String getPagePath(){
        return (properties != null) ? properties.get(ProductConstant.PRODUCT_PAGE_PATH, "") : "";
    }
    public String getProductSubTitle() {
        return (properties != null) ? properties.get(ProductConstant.PRODUCT_SUB_TITLE, "") : "";
    }

    public double getPrice() {
        double priceInfo = 0.0;
        if(properties.containsKey(ProductConstant.PRODUCT_PRICE_PROP)){
            Double price = (properties != null) ? properties.get(ProductConstant.PRODUCT_PRICE_PROP, Double.class):0.0;
            int priceLastDigit = (int) ((int)(price - price.intValue())* Math.pow(10, 2));
            return priceLastDigit > 0 ? price : (priceLastDigit == 0 ? price : 0);
        }
        return priceInfo;
    }

    public String getAbsolutePrice(){
        double price = getPrice();
        return formattedPrice(price);
    }

    public double getSellingPrice() {
        return (properties != null) ? properties.get(ProductConstant.PRODUCT_SELLING_PRICE_PROP, 0.0):0.0;
    }

    public double getProductQty() {
        return (properties != null) ? properties.get(ProductConstant.PRODUCT_QTY_PROP, 0.0):0.0;
    }

    public double getMinProductQty() {
        return (properties != null) ? properties.get(ProductConstant.MIN_PRODUCT_QTY_PROP, 0.00):0.0;
    }

    public String[] getProductImages() {
        return (properties != null) ? properties.get(ProductConstant.PRODUCT_IMAGES_PROP, new String[0]) : new String[0];
    }

    public String getFaqs() {
        return (properties != null) ? properties.get(ProductConstant.FAQS_PROP, "") : "";
    }

    public ArrayList<HeadingDescModel> getQuickFeature() {
        return ProductOtherInfoUtil.getFeature(resource, ProductConstant.MAX_QUICK_FEATURE_PROPERTIES, ProductConstant.QUICK_FEATURES);
    }

    public ArrayList<HeadingDescModel> getDetailFeature() {
        return ProductOtherInfoUtil.getFeature(resource, ProductConstant.MAX_DETAIL_FEATURE_PROPERTIES, ProductConstant.DETAIL_FEATURES);
    }

    public ArrayList<HeadingDescModel> getAccessories() {
        return ProductOtherInfoUtil.getFeature(resource, ProductConstant.MAX_ACCESSARY_PROPERTIES, ProductConstant.ACCESSORY_FEATURES);
    }

    public Map<String, List> getTechnicalSpec() {
        return ProductOtherInfoUtil.getTechnicalSpec(resource);
    }
    public String[] getTechnicalSpecImages() throws RepositoryException {
        return ProductOtherInfoUtil.getTechnicalSpecImages(resource);
    }
    public  String[] getTechnicalDrawings() throws RepositoryException {
        return ProductOtherInfoUtil.getTechnicalDrawings(resource);
    }
    public List<ProductRecommendationModal> getRecommendedProducts(){
        return ProductOtherInfoUtil.getRecommendedProducts(resource);
    }
    public String formattedPrice(Double cost) {

        Locale locale=new Locale("en","IN");
        NumberFormat numberFormatter = NumberFormat.getCurrencyInstance(locale);

        return numberFormatter.format(cost);
    }
}
