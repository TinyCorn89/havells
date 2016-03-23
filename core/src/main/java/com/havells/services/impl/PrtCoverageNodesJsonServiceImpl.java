package com.havells.services.impl;

import com.havells.core.model.CustomDateFormatter;
import com.havells.services.PrtCoverageNodesJsonService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(metatype = true)
@Service
@org.apache.felix.scr.annotations.Properties({
        @Property(name = "service.vendor", value = "Havells India"),
        @Property(name = "service.description", value = "Component to get json on given resource")
})
public class PrtCoverageNodesJsonServiceImpl implements PrtCoverageNodesJsonService {
    private static final Logger logger = LoggerFactory.getLogger(PrtCoverageNodesJsonServiceImpl.class);

    JSONArray jsonArray = null;

    public JSONObject getNodesInfo(Resource resource, String suffix, String resPath) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonArray = new JSONArray();
            if (resource.getChild("prtcoverage").hasChildren()) {
                Iterator<Resource> itr = resource.getChild("prtcoverage").listChildren();
                while (itr.hasNext()) {
                    Resource temp = itr.next();
                    ValueMap properties = temp.adaptTo(ValueMap.class);
                    suffix = (suffix.substring(suffix.indexOf('/') + 1)).intern();
                    String date = properties.get("publicationdate", String.class);
                    String toPrintDate = date.substring(date.lastIndexOf('/') + 1);
                    if (properties.containsValue(resPath)) {
                        if ("all".equalsIgnoreCase(suffix)) {
                            buildJsonArray(properties, temp);
                        } else if (toPrintDate.equalsIgnoreCase(suffix)) {
                            buildJsonArray(properties, temp);
                        }
                    }

                }
            }
            jsonObject.put("items", jsonArray);
            jsonObject.put("totalItems", jsonArray.length());
            logger.info("JSON Object ::: " + jsonObject);

        } catch (Exception ex) {
            logger.error("Error in PrtCoverageNodesJsonService: " + ex);
        }
        return jsonObject;
    }

    /**
     * @param properties
     */
    private void buildJsonArray(ValueMap properties, Resource temp) {
        String color = "";
        String linkType = "";
        String newWindow = "";
        String textOverlay = "";
        String url = "";
        Map<String, String> map = new HashMap<String, String>();

        String description = properties.get("description", "");
        String publicationdate = properties.get("publicationdate", "");
        String pubsubtitle = properties.get("pubsubtitle", "");
        String pubtitle = properties.get("pubtitle", "");
        if (temp.hasChildren()) {
            ValueMap valueMapChild = temp.getChild("cta").getValueMap();
            color = valueMapChild.get("color", String.class);
            linkType = valueMapChild.get("linkType", String.class);
            newWindow = valueMapChild.get("newWindow", String.class);
            textOverlay = valueMapChild.get("textOverlay", String.class);
            url = valueMapChild.get("url", String.class);
        }
        map.put("description", description);
        map.put("publicationdate", new CustomDateFormatter().convertToPublicationDate(publicationdate));
        map.put("pubsubtitle", pubsubtitle);
        map.put("pubtitle", pubtitle);

        map.put("color", color);
        map.put("linkType", linkType);
        map.put("newWindow", newWindow);
        map.put("textOverlay", textOverlay);
        map.put("url", url);
        jsonArray.put(map);
    }


}
