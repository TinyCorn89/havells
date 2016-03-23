package com.havells.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface RatingService {
    String fetchRating(String productPath, ResourceResolver resourceResolver);
}
