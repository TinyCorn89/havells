package com.checkout.commerceProvider;

import com.adobe.cq.commerce.common.AbstractJcrProduct;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

public class HavellsProductImpl extends AbstractJcrProduct {
    public static final String PN_IDENTIFIER = "identifier";
    public static final String PN_PRICE = "price";

    protected final ResourceResolver resourceResolver;
    protected final PageManager pageManager;
    protected final Page productPage;
    protected String brand = null;

    public HavellsProductImpl(Resource resource) {
        super(resource);

        resourceResolver = resource.getResourceResolver();
        pageManager = resourceResolver.adaptTo(PageManager.class);
        productPage = pageManager.getContainingPage(resource);
    }

    public final String getSKU() {
        String sku = getProperty(PN_IDENTIFIER, String.class);
        // Geometrixx products don't have unique ids for size, so append the size to the sku:
        String size = getProperty("size", String.class);
        if (size != null && size.length() > 0) {
            sku += "-" + size;
        }
        return sku;
    }

    @Override
    public final <T> T getProperty(String name, Class<T> type) {
        if (name.equals("brand")) {
            return (T) getBrand();
        }

        return super.getProperty(name, type);
    }

    public final String getBrand() {
        // A null value is considered as non-initialized
        if (brand == null) {
            // Get value from root page title
            if (productPage != null) {
                brand = HavellsHelper.getPageTitle(productPage.getAbsoluteParent(2));
            }
            // Make sure that the value is not null, to avoid initializing it again
            if (HavellsHelper.isEmpty(brand)) {
                brand = "";
            }
        }
        return brand;
    }
}
