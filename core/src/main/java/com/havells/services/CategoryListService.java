package com.havells.services;


import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONObject;

public interface CategoryListService {
    JSONObject getCategoriesInfo(Resource resource, Page pageResource);
}
