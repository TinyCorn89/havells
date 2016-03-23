package com.havells.services.impl;

import com.havells.services.RatingService;
import com.havells.services.TestimonialAddService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
@Service
@org.apache.felix.scr.annotations.Properties({
        @Property(name = "service.vendor", value = "Havells India"),
        @Property(name = "service.description", value = "Component to add or fetch rating")
})
public class RatingServiceImpl implements RatingService {

    private static final Logger LOG = LoggerFactory.getLogger(RatingServiceImpl.class);

    @Override
    public String fetchRating(String productPath, ResourceResolver resourceResolver) {
        String rating = "0";
        try {
            if (resourceResolver != null) {
                Resource resource = resourceResolver.getResource(TestimonialAddService.USER_GENERATED_CONTENT + productPath + "/jcr:content/comments");
                if( resource != null) {
                    ValueMap valueMap = resource.getValueMap();
                    rating = valueMap.get("commentRating") != null ? valueMap.get("commentRating").toString() : "0";
                }
            }
        } catch (Exception e) {
            LOG.error("Error fetching average rating" + e);
        }
        return rating;
    }
}
