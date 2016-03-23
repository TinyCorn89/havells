package com.havells.core.model.product

import com.adobe.cq.commerce.api.Product
import com.havells.services.SearchQuery
import com.havells.util.RenditionUtil
import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ResourceResolver
import org.apache.sling.commons.json.JSONArray
import org.apache.sling.commons.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

final class ProductFilterResultModel {

    private static final Logger LOG = LoggerFactory.getLogger(ProductFilterResultModel.class)

    public static JSONObject getAllProducts(final String filters, SearchQuery productSearch, final String commercePath, ResourceResolver resolver) {

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if (filters) {
            List<Resource> resourceList = productSearch.getProducts(filters, false, commercePath, resolver)
            LOG.info("size of results " + resourceList.size())
            for (Resource resource : resourceList){
                def path = getProductResourcePath(resource.path)
                if (path) resource = resolver.getResource(path)
                if(resource){
                    Product product = resource.adaptTo(Product.class);
                    ProductDetails productDetails = isResourceVariant(resource) ? new ProductDetails(resource.parent) : new ProductDetails(resource)
                    Map<String, String> map = new HashMap<String, String>();

                    map.put("title", productDetails.getPageTitle());
                    map.put("subTitle", productDetails.getProductSubTitle());
                    map.put("imagePath", productDetails.getDefaultImage());
                    if(productDetails.getPrice() != 0.0) {
                        map.put("price", ProductConstant.CurrencyConstant.MRP + String.valueOf(productDetails.getAbsolutePrice()));
                    }else{
                        map.put("price","");
                    }
                    map.put("pagePath", productDetails.getPagePath() + ".html");
                    map.put("pageId", productDetails.getPagePath().replaceAll("/", "_"));
                    map.put("imageWithRendition",productDetails.getSmallCoverImage());
                    map.put("imageWithRendition75_71", RenditionUtil.getRendition(productDetails.getDefaultImage(),
                            resource.getResourceResolver(), ProductConstant.THUMBNAIL_RENDITION_75X71));;
                    map.put("skuId",product.getSKU());
                    jsonArray.put(map);
                }
            }
            jsonObject.put("items", jsonArray);
            jsonObject.put("totalItems", jsonArray.length());
        }
        return jsonObject;
    }
    private static String getProductResourcePath(String searchResourcePath){
        return searchResourcePath.contains("/otherInfo/") ?
                searchResourcePath.substring(0, searchResourcePath.indexOf("/otherInfo/"))?.intern() :searchResourcePath
    }
    private static boolean isResourceVariant(Resource resource){
        if (resource) {
            return (resource.valueMap.get("cq:commerceType", "product").equalsIgnoreCase("variant"))
        }
        return false
    }

}
