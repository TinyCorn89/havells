package com.havells.services.impl;


import com.adobe.cq.commerce.api.CommerceException;
import com.adobe.cq.commerce.api.CommerceSession;
import com.adobe.cq.commerce.api.Product;
import com.adobe.cq.commerce.common.CommerceHelper;
import com.day.cq.wcm.api.Page;
import com.havells.commons.sling.Resources;
import com.havells.core.model.product.ProductConstant;
import com.havells.core.model.product.ProductDetails;
import com.havells.services.ProductListService;
import com.havells.util.RenditionUtil;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

@Component(metatype = true)
@Service
@org.apache.felix.scr.annotations.Properties({
        @Property(name = "service.vendor", value = "Havells India"),
        @Property(name = "service.description", value = "Product listing component.")
})
public class ProductListServiceImpl implements ProductListService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductListServiceImpl.class);

    @Override
    public JSONObject getProductsInfo(Resource resource, Page currentPage, CommerceSession session) {

        JSONObject jsonObject = new JSONObject();
        try {
            Iterator<Page> itr = currentPage.listChildren();
            JSONArray jsonArray = new JSONArray();
            while (itr.hasNext()) {
                Page tempPage = itr.next();
                final Product product = CommerceHelper.findCurrentProduct(tempPage);
                if (product != null) {
                    final Resource productResource = product.adaptTo(Resource.class);
                    if(product.getPIMProduct() != null){
                        ProductDetails productDetails = new ProductDetails(product.getPIMProduct().adaptTo(Resource.class));
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("title", tempPage.getTitle());
                        map.put("subTitle", productDetails.getProductSubTitle());
                        map.put("imagePath", product.getImage().getSrc());
                        if(productDetails.getPrice() != 0.0) {
                            map.put("price", ProductConstant.CurrencyConstant.MRP + String.valueOf( productDetails.getAbsolutePrice() ));
                        }else{
                            map.put("price","");
                        }
                        map.put("pagePath", tempPage.getPath() + ".html");
                        map.put("pageId", tempPage.getPath().replaceAll("/", "_"));
                        map.put("imageWithRendition", RenditionUtil.getRendition(productDetails.getDefaultImage(),
                                resource.getResourceResolver(), ProductConstant.THUMBNAIL_RENDITION_342X325));
                        map.put("imageWithRendition75_71", RenditionUtil.getRendition(productDetails.getDefaultImage(),
                                resource.getResourceResolver(), ProductConstant.THUMBNAIL_RENDITION_75X71));
                        map.put("skuId",product.getSKU());
                        jsonArray.put(map);
                    }
                }
            }
            jsonObject.put("items", jsonArray);
            jsonObject.put("totalItems", jsonArray.length());
        } catch (JSONException ex) {
            LOG.error("JSONException: ", ex);
        } catch (CommerceException e) {
            LOG.error("CommerceException", e);
        }
        return jsonObject;
    }
}
