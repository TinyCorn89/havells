package com.havells.services;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONObject;

public interface NodesJsonService {

    JSONObject getNodesInfo(Resource resource, String selector, String resPath) throws Exception;
}
