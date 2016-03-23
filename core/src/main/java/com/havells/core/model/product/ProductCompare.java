package com.havells.core.model.product;


import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.api.Product;
import com.adobe.cq.commerce.common.CommerceHelper;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class ProductCompare {

    private Resource resource;
    private static final Logger LOG = LoggerFactory.getLogger(ProductCompare.class);
    private TagManager tagManager;
    private List<Product> products = new ArrayList<Product>();
    private Set<String> uniqueTags = new HashSet<String>();

    private static final String COOKIE_NAME = "compareProductList";
    private Cookie cookie;
    private JSONObject compareProductObject;

    public ProductCompare(Resource resource, HttpServletRequest request) {
        this.resource = resource;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if( COOKIE_NAME.equals( cookie.getName())){
                this.cookie = cookie;
            }
        }

        try {
            JSONObject jsonObject = new JSONObject(java.net.URLDecoder.decode(cookie.getValue(), "UTF-8"));
            if(jsonObject != null)
                compareProductObject = (JSONObject)jsonObject.get("items");
        } catch (UnsupportedEncodingException | JSONException e) {
            LOG.error("Error while putting values in compareProductObject in class {}" , ProductCompare.class);
        }
        tagManager = resource.getResourceResolver().adaptTo(TagManager.class);
        setCompareProducts();
    }

    private void setCompareProducts() {
        Iterator<?> keys = compareProductObject.keys();

        while( keys.hasNext() ) {
            String key = (String)keys.next();
            try {
                if ( compareProductObject.get(key) instanceof JSONObject ) {
                    JSONObject productJson = (JSONObject) compareProductObject.get(key);
                    String pagePath = String.valueOf(productJson.get("pagePath"));
                    String productPageResource = pagePath.substring(0, pagePath.lastIndexOf('.'));
                    Resource productResource = resource.getResourceResolver().getResource(productPageResource);
                    if (productResource != null) {
                        Page page = productResource.adaptTo(Page.class);
                        Product product = CommerceHelper.findCurrentProduct(page);
                        products.add(product);
                        findUniqueTags(product);
                    }
                }
            } catch (JSONException e) {
                LOG.error("JSON Exception while adding to product list.");
            }
        }
    }

    private void findUniqueTags(Product product){
        Map<String, List> productDetailsTechSpec = getProductTechSpec(product);
        if (productDetailsTechSpec != null) {
            for(List listEntry : productDetailsTechSpec.values()){
                for (int count = listEntry.size()-1; count >= 0; count--) {
                    HeadingDescModel headingDescModel = (HeadingDescModel) listEntry.get(count);
                    uniqueTags.add(headingDescModel.getTag().getTitle());
                }
            }
        }
    }
    public List<Product> getProducts() {
        return products;
    }

    public int getProductsSize() {
        return products.size();
    }

    /**
     * Util method to resolve a tagId to it's name
     *
     * @param tagId
     * @return String tagTitle
     */
    public String resolveTagName(String tagId) {
        if(tagManager != null){
            Tag tag = tagManager.resolve(tagId);
            return tag != null ? tag.getTitle() : "";
        }
        return "";
    }

    /**
     * iterate over all the products and get the values of the tag selected by author in a Map
     *
     * @return List of Tag Values corresponding to the Products
     */
    List<String> warranty = null;

    public List<String> getWarranty(){
        return warranty;
    }
    public List<Map> getTechnicalSpecMap() {
        warranty = new ArrayList<String>();
        List<Map> techSpecValues = new ArrayList<Map>();
        ResourceResolver resolver = resource.getResourceResolver();
        try {
            for (Product product : products) {
                Map<String, String> techSpecMap = new HashMap<String, String>();
                String pimDataPath = resolver.getResource(product.getPath()).adaptTo(ValueMap.class).get("productData", String.class);
                Resource quickFeatureResource = resolver.getResource(pimDataPath + "/" + ProductConstant.OTHER_INFO + ProductConstant.QUICK_FEATURES);
                if(quickFeatureResource != null){
                    warranty.add( String.valueOf(quickFeatureResource.getValueMap().get(ProductConstant.WARRANTY_PROP)) );
                }
                Resource techSpecResource = resolver.getResource(pimDataPath + "/" + ProductConstant.OTHER_INFO + ProductConstant.TECHNICAL_SPECIFICATION);
                if( techSpecResource != null){
                    Iterable<Resource> itr = techSpecResource.getChildren();
                    for (Resource heading : itr) {
                        ValueMap valueMap = heading.getValueMap();
                        Tag tag = tagManager.resolve(valueMap.get("key", ""));
                        if (tag != null) {
                            techSpecMap.put(tag.getTitle(), valueMap.get("value", ""));
                        }
                    }
                    techSpecValues.add(techSpecMap);
                }
            }
        } catch (Exception ex) {

        }
        return techSpecValues;
    }

    private Map<String, List> getProductTechSpec(Product product) {
        try {
            ProductDetails productDetails = new ProductDetails(product.getPIMProduct().adaptTo(Resource.class));
            return productDetails.getTechnicalSpec();
        } catch (CommerceException e) {
            LOG.error("Error occurred while resolving product to get PIM Product", e.getMessage());
        }
        return null;
    }


    public List<String> resolveTagChildren(String tagId) {
        Resource res = resource.getResourceResolver().getResource(tagId);
        List<String> tagChildren = new ArrayList<String>();
        if(res != null) {
             Iterable<Resource> itr = res.getChildren();
             for (Resource heading : itr) {
                Tag tag = tagManager.resolve(heading.getPath());
                if (tag != null) {
                    if(uniqueTags.contains(tag.getTitle())){
                        tagChildren.add(tag.getTitle());
                    }
                }
             }
        }
        return tagChildren;
    }
}