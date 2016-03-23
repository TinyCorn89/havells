package com.havells.core.model;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import java.util.ArrayList;

public class SocialMediaShareComponent {
    private static final String SOCIAL_PROP_NAME = "social";
    private Resource resource;

    public SocialMediaShareComponent(Resource resource) {
        if (resource == null) {
            return;
        }
        this.resource = resource.getChild("links");
    }

    public java.util.List<String> getSocialMediaNames() {
        java.util.List<String> socialMediaItemsList = new ArrayList<String>();
        if (resource != null) {
            for (Resource child : com.havells.commons.sling.Resources.listChildren(resource)) {
                if (child != null) {
                    ValueMap valueMap = child.adaptTo(ValueMap.class);
                    if (valueMap.get(SOCIAL_PROP_NAME) != null) {
                        socialMediaItemsList.add((String) valueMap.get(SOCIAL_PROP_NAME));
                    }
                }
            }
        }
        return socialMediaItemsList;
    }
}
