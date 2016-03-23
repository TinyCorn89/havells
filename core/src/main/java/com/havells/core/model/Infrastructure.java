package com.havells.core.model;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Infrastructure Component Class. It will create the json object.
 */
public class Infrastructure {

    /*logger*/
    private static final Logger LOG = LoggerFactory.getLogger(Infrastructure.class);

    private Resource resource;

    private List<HashMap> list;

    /**
     * Default Constructor.
     */
    public Infrastructure() {
    }

    public Infrastructure(Resource resource) {
        this.resource = resource;
    }

    public List<HashMap> getList() {
        list = new ArrayList<HashMap>();
        try {
            if (resource != null) {
                ValueMap valueMap = resource.adaptTo(ValueMap.class);
                String[] strings = valueMap.get("sections", String[].class);
                if (strings != null) {
                    for (String str : strings) {
                        JSONObject jsonObject = new JSONObject(str);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("label", jsonObject.has("label") ? (String)jsonObject.get("label") : "");
                        map.put("number", jsonObject.has("number") ? (String)jsonObject.get("number") : "");
                        list.add(map);
                    }
                }
            }
        } catch (JSONException jexc) {
            LOG.error("Exception during json formation." + jexc);
        }
        return list;
    }

}
