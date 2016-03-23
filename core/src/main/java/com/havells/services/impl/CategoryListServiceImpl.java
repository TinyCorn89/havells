package com.havells.services.impl;

import com.adobe.cq.social.blog.Blog;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.havells.core.model.product.ProductConstant;
import com.havells.services.CategoryListService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(metatype = true)
@Service
@org.apache.felix.scr.annotations.Properties({
        @Property(name = "service.vendor", value = "Havells India"),
        @Property(name = "service.description", value = "Component to get json on given resource")
})
public class CategoryListServiceImpl implements CategoryListService{

    private static final Logger LOG = LoggerFactory.getLogger(CategoryListServiceImpl.class);
    private static final String SUBCATEGORY_TEMPLATE_PATH ="/apps/havells/templates/subCategoryPage";
    private static final String PRODUCT_LISTING_TEMPLATE_PATH ="/apps/havells/templates/productListing";

    @Override
    public JSONObject getCategoriesInfo(Resource resource, Page pageResource){
        JSONObject jsonObject = new JSONObject();
        try{

            JSONArray jsonArray = new JSONArray();
            if(pageResource != null){
                //Page page = resource.adaptTo(Page.class);
                Iterator<Page> itr = pageResource.listChildren();
                while(itr.hasNext()){
                    Page tempPage = itr.next();

                    String templatePath =  tempPage.getProperties().get(Blog.PROP_TEMPLATE, String.class);
                    if(SUBCATEGORY_TEMPLATE_PATH.equalsIgnoreCase(templatePath) || PRODUCT_LISTING_TEMPLATE_PATH.equalsIgnoreCase(templatePath)){
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("pagePath", tempPage.getPath()+".html");
                        map.put("pageName", tempPage.getTitle() != null ? tempPage.getTitle():tempPage.getName());
                        Resource imageResource = resource.getResourceResolver().getResource(tempPage.getPath()+"/"+ JcrConstants.JCR_CONTENT+"/image");
                        String imagePath = ProductConstant.DEFAULT_IMAGE_PATH;
                        if(imageResource != null){
                            imagePath = imageResource.adaptTo(ValueMap.class).get("fileReference", "");
                        }
                        map.put("imagePath", imagePath);
                        jsonArray.put(map);
                    }
                }
            }
            jsonObject.put("items", jsonArray);
            jsonObject.put("totalItems", jsonArray.length());
        }catch (Exception ex){
           LOG.error("Exception: " + ex);
        }
        return jsonObject;
    }
}
