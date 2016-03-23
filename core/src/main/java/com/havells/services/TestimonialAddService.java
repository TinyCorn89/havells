package com.havells.services;

import org.apache.sling.api.resource.ResourceResolver;

import java.util.Map;

public interface TestimonialAddService {
    String USER_GENERATED_CONTENT = "/content/usergenerated";
    void addTestimonial(String currentProductPath, Map<String,Object> propertyMap, ResourceResolver resourceResolver);
   }
