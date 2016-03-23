package com.havells.core.model;

import com.havells.services.TestimonialAddService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Rima
 * Date: 3/5/15
 * Time: 12:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class FetchRating {

    private static final Logger LOG = LoggerFactory.getLogger(FetchRating.class);
    private String productPath;
    private ResourceResolver resourceResolver;

    public FetchRating(ResourceResolver resourceResolver, String productPath) {
        this.productPath = productPath;
        this.resourceResolver = resourceResolver;
    }

    public String getAverageRating() {
        String rating = "0";
        try {
            Resource resource = resourceResolver.getResource(TestimonialAddService.USER_GENERATED_CONTENT + productPath + "/jcr:content");
            if (resource != null) {
                rating = resource.getValueMap().get("commentRating", "0").toString();
            }
        } catch (Exception e) {
            LOG.error("Error fetching average rating" + e);
        }
        return rating;
    }
}
