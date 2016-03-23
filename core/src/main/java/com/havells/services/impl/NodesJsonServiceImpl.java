package com.havells.services.impl;


import com.havells.services.NodesJsonService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(metatype = true)
@Service
@org.apache.felix.scr.annotations.Properties({
        @Property(name = "service.vendor", value = "Havells India"),
        @Property(name = "service.description", value = "Component to get json on given resource")
})
public class NodesJsonServiceImpl implements NodesJsonService {

    private static final Logger LOG = LoggerFactory.getLogger(NodesJsonServiceImpl.class);
    private JSONArray jsonArray = null;
    private final String VIDEO_URL = "http://www.youtube.com/embed/";
    private final String THUMBNAIL = "http://img.youtube.com/vi/";
    private final String DEFAULT_THUMBNAIL = "/mqdefault.jpg";

    private static final String YEAR = "year", CATEGORY = "category", TYPE = "type";

    public JSONObject getNodesInfo(Resource resource, String suffix, String resPath) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonArray = new JSONArray();
            Iterator<Resource> itr = resource.listChildren();
            String[] suffixArray = suffix.split("/");
            while (itr.hasNext()) {
                Resource temp = itr.next();
                ValueMap properties = temp.adaptTo(ValueMap.class);
                if (properties.containsValue(resPath)) {
                    if ("all".equalsIgnoreCase(suffixArray[1])) {
                        buildJsonArray(properties);
                    } else {
                        boolean flag = true;
                        /*
                         * Explicit interation from index 1
                         */
                        for (int i = 1; i < suffixArray.length; i++) {
                            if (!containsValue(properties, suffixArray[i])) {
                                flag = false;
                            }
                        }

                        if (flag) {
                            buildJsonArray(properties);
                        }
                    }
                }
            }
            jsonObject.put("items", jsonArray);
            jsonObject.put("totalItems", jsonArray.length());

        } catch (Exception ex) {
            LOG.error("Exception: " + ex);
        }
        return jsonObject;
    }

    private boolean containsValue(ValueMap properties, String valueToSearch) {
        boolean flag = false;
        if (properties.containsKey(CATEGORY)) {
            String category = properties.get(CATEGORY, String.class);

            if (category.equalsIgnoreCase(valueToSearch)) {
                flag = true;
            }
        }
        if (properties.containsKey(YEAR)) {
            String year = properties.get(YEAR, String.class);

            if (year.equalsIgnoreCase(valueToSearch)) {
                flag = true;
            }
        }
        if (properties.containsKey(TYPE)) {
            String type = properties.get(TYPE, String.class);

            if (type.equalsIgnoreCase(valueToSearch)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * @param properties
     */
    private void buildJsonArray(ValueMap properties) {
        String nodeType = properties.get("type", "");
        Map<String, String> map = new HashMap<String, String>();
        if ("image".equals(nodeType)) {
            map.put("fileReference", properties.get("fileReference", ""));
            map.put("mediaClass", "mediaPrint");
            map.put("anchorLink", properties.get("largeImage", ""));
        } else {
            String videoId = properties.get("videoID", "");
            String videoUrl = VIDEO_URL + videoId;
            String thumbNail = THUMBNAIL + videoId + DEFAULT_THUMBNAIL;
            map.put("mediaClass", "mediaVideo");
            map.put("anchorLink", videoUrl);
            map.put("fileReference", thumbNail);
        }
        map.put("label", properties.get("label", ""));
        jsonArray.put(map);
    }
}
