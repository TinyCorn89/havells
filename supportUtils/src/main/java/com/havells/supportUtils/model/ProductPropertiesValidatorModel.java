package com.havells.supportUtils.model;

import java.util.Arrays;

/**
 * Created by shashi on 6/9/15.
 */
public class ProductPropertiesValidatorModel {
    private String productRange;
    private String productCategory;
    private String productSubCategory;

    private String productSKUId;
    private String productPath;
    private String productName;
    private String productCoverImage;
    private String productColorImage;
    private String productManual;
    private String productBrochure;

    private String[] productImages;
    private String[] technicalSpecImages;
    private String[] technicalDrawingImages;

    public ProductPropertiesValidatorModel(String productRange, String productCategory, String productSubCategory, String productSKUId, String productPath, String productName,
                                           String productCoverImage, String productColorImage, String[] productImages, String[] technicalSpecImages,
                                           String[] technicalDrawingImages, String productManual, String productBrochure) {
        this.productRange = productRange;
        this.productCategory = productCategory;
        this.productSubCategory = productSubCategory;
        this.productSKUId = productSKUId;
        this.productPath = productPath;
        this.productName = productName;
        this.productManual = productManual;
        this.productBrochure = productBrochure;
        this.productCoverImage = productCoverImage;
        this.productColorImage = productColorImage;
        this.productImages = productImages;
        this.technicalSpecImages = technicalSpecImages;
        this.technicalDrawingImages = technicalDrawingImages;
    }

    public String getProductRange() {
        return productRange;
    }

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPath() {
        return productPath;
    }

    public String getProductManual() {
        return productManual;
    }

    public String getProductBrochure() {
        return productBrochure;
    }

    public String getproductCoverImage() {
        return productCoverImage;
    }

    public String[] getTechnicalSpecImages() {
        return technicalSpecImages;
    }

    public String[] getTechnicalDrawingImages() {
        return technicalDrawingImages;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getProductSKUId() {
        return productSKUId;
    }

    public String getProductCoverImage() {
        return productCoverImage;
    }

    public String getProductColorImage() {
        return productColorImage;
    }

    public String[] getProductImages() {
        return productImages;
    }
}
