package com.havells.core.model.product;

/**
 * Created by jitendra on 05/05/15.
 */
public class ProductRecommendationModal {

    private String productTitle;
    private double price;
    private String imageSrc;
    private String productPagePath;

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
    public String getProductPagePath() {
        return productPagePath;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProductPagePath(String productPagePath) {
        this.productPagePath = productPagePath;
    }
    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

}
