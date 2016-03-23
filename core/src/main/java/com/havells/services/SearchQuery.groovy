package com.havells.services

import org.apache.sling.api.resource.Resource
import org.apache.sling.api.resource.ResourceResolver


public interface SearchQuery {

    List<Resource> getProducts(String filters, boolean isFilterInRange, String commercePath, ResourceResolver resolver)
}
