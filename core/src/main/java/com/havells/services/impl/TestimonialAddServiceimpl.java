package com.havells.services.impl;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMException;
import com.havells.services.TestimonialAddService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(immediate = true)
@Service
@org.apache.felix.scr.annotations.Properties({
        @Property(name = "service.vendor", value = "Havells India"),
        @Property(name = "service.description", value = "Component to add testimonial")
})
public class TestimonialAddServiceimpl implements TestimonialAddService {

    private static final Logger LOG = LoggerFactory.getLogger(TestimonialAddServiceimpl.class);

    @Reference
    ResourceResolverFactory resolverFactory;

    @Override
    public void addTestimonial(String currentProductPath, Map<String,Object> propertyMap, ResourceResolver resourceResolver) {
        try {

            Map<String,Object> primaryType = new HashMap<String, Object>();

            Resource currentNodeResource = ResourceUtil.getOrCreateResource(resourceResolver,USER_GENERATED_CONTENT,"","",true);
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            String parentPagePath = currentNodeResource.getPath();
            Page page = null;
            Resource pageResource;
            for(String path : currentProductPath.split("/")){
                if(!path.isEmpty()){
                    pageResource = resourceResolver.getResource(parentPagePath+"/"+path);
                    if(pageResource == null){
                        page = pageManager.create(parentPagePath,path,"","");
                        parentPagePath = page.getPath();
                    } else {
                        parentPagePath = pageResource.getPath();
                    }
                }
            }
            page = pageManager.getPage(parentPagePath);
            primaryType.put(JcrConstants.JCR_PRIMARYTYPE, "cq:CommentContent");
            Resource pageJcrContent = page.getContentResource();
            Resource commentResource = ResourceUtil.getOrCreateResource(resourceResolver,pageJcrContent.getPath()+"/comments",primaryType,"",true);
            Resource postedComment = ResourceUtil.getOrCreateResource(resourceResolver,commentResource.getPath()+"/comment"+System.currentTimeMillis(),propertyMap,"",true);
            Double averageRating = 0.0;
            Integer size = 0;
            Iterator<Resource> resourceIt = commentResource.listChildren();
            while(resourceIt.hasNext()){
                averageRating = averageRating+Double.parseDouble(resourceIt.next().getValueMap().get("commentRating").toString());
                size++;
            }
            averageRating = averageRating/size;

            ModifiableValueMap productMap = commentResource.adaptTo(ModifiableValueMap.class);
            productMap.put("commentRating", averageRating.toString());
            commentResource.getResourceResolver().commit();

        } catch (PersistenceException e) {
            LOG.error("PersistenceException", e);
        } catch (WCMException e) {
            LOG.error("WCMException", e);
        } finally{
            if(resourceResolver != null && resourceResolver.isLive()){
                resourceResolver.close();
            }
        }
    }
}
