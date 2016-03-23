package com.havells.core.model.product;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductSiblings {

    public Page currentPage;
    public ResourceResolver resourceResolver;

    public ProductSiblings() { /* Empty Constructor */ }

    public ProductSiblings(Page currentPage, ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
        this.currentPage = currentPage;
    }

    public List<ProductSiblingModel> getProductDataLinks() {

        String productPath = "", productDataPath = "", imagePath = "";

        List<Page> validPages = getValidSiblingPages();
        List<ProductSiblingModel> siblingPages = new ArrayList<ProductSiblingModel>();
        ProductSiblingModel siblingModel = null;

        for (Page page : validPages) {
            productPath = page.getPath() + ProductConstant.PRODUCT_PATH;
            Resource productResource = resourceResolver.getResource(productPath);
            if (productResource != null) {
                ValueMap properties = productResource.adaptTo(ValueMap.class);
                productDataPath = properties.get(ProductConstant.PRODUCT_DATA, "");

                if (!productDataPath.equals("")) {
                    imagePath = getImagePath(productDataPath);
                    siblingModel = new ProductSiblingModel();
                    siblingModel.setPageUrl(page.getPath() + ".html");
                    siblingModel.setTitle(page.getTitle());
                    siblingModel.setImageUrl(imagePath);
                    siblingPages.add(siblingModel);
                }
            }
        }
        return siblingPages;
    }

    List<Page> getValidSiblingPages() {

        List<Page> pageList = new ArrayList<Page>();
        Page parentPage = currentPage.getParent();
        if (parentPage != null) {
            Iterator<Page> pages = parentPage.listChildren();
            while (pages.hasNext()) {
                Page tempPage = pages.next();
                if (isValidPage(tempPage)) {
                    pageList.add(tempPage);
                }
            }
        }
        return pageList;
    }

    boolean isValidPage(Page page) {
        return ((!page.isHideInNav()) && (!page.getPath().equals(currentPage.getPath())));
    }

    String getImagePath(String productPath) {
        String imagePath = "";
        Resource resource = resourceResolver.getResource(productPath + "/image");
        if (resource != null) {
            ValueMap properties = resource.adaptTo(ValueMap.class);
            imagePath = properties.get("fileReference", "");
        }
        return imagePath;
    }

}
