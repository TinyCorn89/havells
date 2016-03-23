package com.havells.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface GenericService {

    ResourceResolver getAdminResourceResolver();
    String getProps(String propName, String propertyName);
}
