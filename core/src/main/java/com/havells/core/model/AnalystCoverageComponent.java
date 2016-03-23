package com.havells.core.model;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shashi on 8/9/15.
 */
public class AnalystCoverageComponent {

    private static final Logger LOG = LoggerFactory.getLogger(AnalystCoverageComponent.class);

    private static final String COLUMN_FIELD_PROP_NAME = "field";
    private static final String COLUMN_FIELD_PROP_WIDTH = "width";

    private static final String NODE_NAME = "headerInfo";

    private Resource resource;

    public AnalystCoverageComponent(Resource resource) {
        if (resource == null) {
            return;
        }
        this.resource = resource;
    }

    public List<Map<String, String> > getFields() {
        List<Map<String, String>> fieldInfoList = new ArrayList<Map<String, String>>();
        if (resource != null) {
            try {
                    ValueMap valueMap = resource.getValueMap();
                    String[] strings = valueMap.get(NODE_NAME , String[].class);
                    for (String str : strings) {
                        JSONObject jsonObject = new JSONObject(str);
                        Map<String,String> map=new HashMap<String, String>();
                        map.put("fieldName", jsonObject.has("field") ? String.valueOf( jsonObject.get("field") ) : "");
                        map.put("fieldWidth", jsonObject.has("width") ? String.valueOf( jsonObject.get("width") ) : "20");
                        map.put("priceDescription", jsonObject.has("priceDesc") ? String.valueOf( jsonObject.get("priceDesc") ) : "");
                        fieldInfoList.add(map);
                    }
            } catch (Exception exp) {
                LOG.error("Exception during json object formation." + exp);
            }
        }
        return fieldInfoList;
    }
}
