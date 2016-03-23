package com.havells.core.model;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuickLinks {

    /*logger*/
    private static final Logger LOG = LoggerFactory.getLogger(QuickLinks.class);

    private Resource resource;

    private List<HashMap> list = new ArrayList<HashMap>();

    /**
     * Default Constructor.
     */
    public QuickLinks() {
    }

    public QuickLinks(Resource resource) {
        this.resource = resource;
    }

    public List<HashMap> getLinks() {
        try {
            if (resource != null) {
                ValueMap valueMap = resource.adaptTo(ValueMap.class);
                String[] strings = valueMap.get("links", String[].class);
                for (String str : strings) {
                    JSONObject jsonObject = new JSONObject(str);
                    HashMap<String, String> map = new HashMap<String, String>();
                    String path = (String) jsonObject.get("path");
                    Resource pageResource = resource.getResourceResolver().getResource(path);
                    if (pageResource != null && pageResource.adaptTo(Page.class) != null) {
                        path = path + ".html";
                    }
                    map.put("link", (String) jsonObject.get("link"));
                    map.put("path", path);
                    map.put("icon", (String) jsonObject.get("icon"));
                    list.add(map);
                }
            }
        } catch (Exception exp) {
            LOG.error("Exception during json object formation." + exp);
        }
        return list;
    }

}
