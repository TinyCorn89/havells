package com.havells.servlet.search

import com.day.cq.wcm.api.Page
import com.havells.core.model.product.ProductConstant
import com.havells.core.model.product.ProductDetails
import com.havells.services.SearchQuery
import com.havells.util.RenditionUtil
import org.apache.felix.scr.annotations.Component
import org.apache.felix.scr.annotations.sling.SlingServlet
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.SlingHttpServletResponse
import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ResourceResolver
import org.apache.sling.api.servlets.SlingSafeMethodsServlet
import org.apache.sling.commons.json.JSONArray
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@SlingServlet(
        paths = ["/bin/search/products"],
        generateComponent = false
)
@Component(enabled = true, immediate = true, metatype = false)
public class ProductFilterSearchServlet extends SlingSafeMethodsServlet{

    private static final Logger LOG = LoggerFactory.getLogger(ProductFilterSearchServlet.class)

    static final String PRICE_FILTER = "price"
    static final String COLOR_FILTER = "color"
    static final String OTHER_FILTER = "*"

    @org.apache.felix.scr.annotations.Reference
    SearchQuery productSearch



    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {

        List<ProductDetails> products = []

//        Page currentPage = request.adaptTo(Page.class);
        ResourceResolver resolver = request.resourceResolver
        response.setContentType("application/json");
        try {
             String filterType = request.getParameter("filterType")
             String filterValue = request.getParameter("filterValue")
            JSONArray jsonArray = new JSONArray();
             if(filterValue) {
                List<Resource> resourceList = productSearch.getProducts(
                         filterType,
                         filterValue, false,
                         resolver)
                 LOG.info ("size of results "+ resourceList.size())

                 resourceList.each { it
                     Resource resource = it
                     def path = getProductResourcePath(it.path);
                     if(path) resource = resolver.getResource(path)
                     if (isResourceVariant(resource))
                         products.add(new ProductDetails(resource.parent))
                     else
                         products.add(new ProductDetails(resource))
                 }
             }
             for (ProductDetails productDetails : products){
                 Map<String, String> map = new HashMap<String, String>();
                 map.put("title", productDetails.getPageTitle());
                 map.put("subTitle", productDetails.getProductSubTitle());
                 map.put("imagePath", productDetails.getDefaultImage());
                 if(productDetails.getPrice() != 0.0) {
                     map.put("price", ProductConstant.CurrencyConstant.MRP + ProductConstant.CurrencyConstant.RUPEE + String.valueOf(productDetails.getAbsolutePrice()) + "/-");
                 }else{
                     map.put("price","");
                 }
                 map.put("pagePath", productDetails.getPagePath() + ".html");
                 map.put("pageId", productDetails.getPagePath().replaceAll("/", "_"));
                 map.put("imageWithRendition",productDetails.getSmallCoverImage());
                 jsonArray.put(map);
                 LOG.info(productDetails.pageTitle);
                 LOG.info(productDetails.productSubTitle);
             }
            response.getWriter().write(jsonArray.toString());
             response.setStatus(200)

        }catch(Exception ex){
            ex.printStackTrace()
            response.setStatus(500)
        } finally{
//            if (resolver?.isLive()) resolver.close()
        }
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

    public List<ProductDetails> getProducts(){
        return products
    }
}
