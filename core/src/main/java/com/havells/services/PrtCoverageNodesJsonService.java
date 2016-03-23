package com.havells.services;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONObject;

/**
 * Created by intelligrape on 10/3/15.
 */
public interface PrtCoverageNodesJsonService {
    JSONObject getNodesInfo(Resource resource, String selector, String resPath) throws Exception;

}
