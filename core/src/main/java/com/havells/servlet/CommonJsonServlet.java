package com.havells.servlet;

import com.day.cq.wcm.api.Page;
import com.havells.core.model.product.ProductConstant;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@SlingServlet(
        paths={"/bin/getChildNodes.json"}
)
@Properties({
        @Property(name="service.description",value="this is a servlet", propertyPrivate=false),
        @Property(name="service.vendor",value="Havells", propertyPrivate=false)
})
public class CommonJsonServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(CommonJsonServlet.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        String path = request.getParameter("path");
        try{
            Resource resource = request.getResourceResolver().getResource(path);
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            if(resource != null){
                Page page = resource.adaptTo(Page.class);
                Iterator<Page> itr = page.listChildren();
                while(itr.hasNext()){
                    Page tempPage = itr.next();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("pagePath", tempPage.getPath()+".html");
                    map.put("pageName", tempPage.getPageTitle() != null ? tempPage.getTitle():tempPage.getName());
                    Resource imageResource = resource.getResourceResolver().getResource("/jcr:content/image");
                    String imagePath = ProductConstant.DEFAULT_IMAGE_PATH;
                    if(imageResource != null){
                        imagePath = imageResource.adaptTo(ValueMap.class).get("fileReference", "");
                    }
                    map.put("imagePath", imagePath);
                    jsonArray.put(map);
                }
            }
            jsonObject.put("items", jsonArray);
            jsonObject.put("totalItems", jsonArray.length());
            response.getWriter().write(jsonObject.toString());
        }catch (Exception ex){
            response.getWriter().write("Exception occurs in building json");
            LOG.error("Exception occurs in building json: " + ex);
        }
    }
}
