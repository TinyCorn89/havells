package com.havells.core.model;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Links {

    private static final Logger LOG = LoggerFactory.getLogger(Links.class);

    private Resource resource;

    public Links(Resource resource) {
        this.resource = resource;
    }

    public List getList() {
        List list = new ArrayList();
        try {
            if (resource != null) {
                ValueMap valueMap = resource.adaptTo(ValueMap.class);
                if (valueMap.containsKey("linkLabel")) {
                    String[] links = valueMap.get("linkLabel", String[].class);
                    for (String link : links) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("link", link);
                        list.add(map);
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception: " + ex);
        }
        return list;
    }
}
