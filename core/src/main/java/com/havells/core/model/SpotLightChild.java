package com.havells.core.model;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SpotLightChild {

    private String buttons[];
    private String imageUrl;

    private static final Logger LOG = LoggerFactory.getLogger(SpotLightChild.class);

    private Map<String, String> buttonsMap;

    public String[] getButtons() {
        return buttons;
    }

    public void setButtons(String[] buttons) {
        this.buttons = buttons;
    }

    public Map<String, String> getButtonsMap() {
        return buttonsMap;
    }

    public void setButtonsMap(Map<String, String> buttonsMap) {
        this.buttonsMap = buttonsMap;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public SpotLightChild(Resource resource) {

        try {
            buttonsMap = new HashMap<String, String>();
            ValueMap properties = resource.adaptTo(ValueMap.class);
            buttons = properties.get("buttons", new String[0]);
            if (buttons.length > 0) {
                for (String button : buttons) {
                    JSONObject jsonObject = new JSONObject(button);
                    String label = jsonObject.getString("label");
                    String url = jsonObject.getString("url");
                    if (url.equals("")) {
                        url = "#";
                    }
                    buttonsMap.put(label, url);
                }
            }
            Resource image = resource.getChild("image");

            if (image != null) {
                ValueMap imageProperties = image.adaptTo(ValueMap.class);
                imageUrl = imageProperties.get("fileReference", "");
            }
        } catch (JSONException jsonException) {
            LOG.error("JSON Exception: " + jsonException);
        }
    }
}
