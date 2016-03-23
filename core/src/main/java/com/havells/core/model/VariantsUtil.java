package com.havells.core.model;

import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.api.Product;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VariantsUtil {

    private static final Logger LOG = LoggerFactory.getLogger(VariantsUtil.class);

    Product baseProduct;
    ResourceResolver resourceResolver;
    Product PIMProduct;
    private List<Product> genericVariantList = null;
    private List<Product> colorVariantList = null;

    public VariantsUtil(Product baseProduct, ResourceResolver resourceResolver){
        this.resourceResolver = resourceResolver;
        this.baseProduct = baseProduct;
    }

    public VariantsUtil(Product pimProduct) {
        this.PIMProduct = pimProduct;
    }

    public void setVariantList(){

        try {
            if(baseProduct != null) {
                LOG.info("base product is not null");

                // gets the product in /etc/commerce/products hierarchy
                PIMProduct = baseProduct.getPIMProduct();
                checkAndSetLists();
            }
        } catch (CommerceException ex) {
            LOG.error("Commerce exception thrown while adding product into variations list", ex);
        }

    }

    public void checkAndSetLists() throws CommerceException {
        if(PIMProduct != null) {
            genericVariantList = new ArrayList<Product>();
            colorVariantList = new ArrayList<Product>();
            LOG.info("path in check set list :" + PIMProduct.getPath());
            // put itself in list
            genericVariantList.add(PIMProduct.adaptTo(Product.class));

            //get variants
            Resource pimProductResource = PIMProduct.adaptTo(Resource.class);
            Iterator<Resource> childItr = pimProductResource.listChildren();
            while (childItr.hasNext()){
                Resource resource = childItr.next();
                ValueMap valueMap = resource.getValueMap();
                String cqCommerceType = valueMap.get("cq:commerceType",String.class);
                if(cqCommerceType != null){
                    String variantType = valueMap.get("variantType", String.class);
                    if(variantType != null ){
                        //adding main product to color variant list only when it has other color variants
                        if(colorVariantList.size() == 0){
                            colorVariantList.add(PIMProduct.adaptTo(Product.class));
                        }
                        LOG.info("value of variant type :" + variantType);
                        colorVariantList.add(resource.adaptTo(Product.class));
                    }else{
                        genericVariantList.add(resource.adaptTo(Product.class));
                    }
                }
            }
        }
    }

    public List<Product> getGenericVariantList() {
        return genericVariantList;
    }
    public List<Product> getColorVariantList() { return colorVariantList;
    }
}
