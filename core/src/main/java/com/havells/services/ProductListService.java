package com.havells.services;

import com.adobe.cq.commerce.api.CommerceSession;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONObject;

public interface ProductListService {

    JSONObject getProductsInfo(Resource resource, Page currentPage, CommerceSession session)throws Exception;
}
