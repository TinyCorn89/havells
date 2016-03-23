package com.havells.core.model;

import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.wcm.api.Page;
import com.havells.commons.sling.Resources;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;


/**
 * Multifield Component Class. It will form the json object of the link and its path.
 */
public class Multifield {

    /*logger*/
    private static final Logger LOG = LoggerFactory.getLogger(Multifield.class);

    private Resource resource;

    private List<MultiFieldModel> list = new ArrayList<MultiFieldModel>();

    /**
     * Default Constructor.
     */
    public Multifield() {
    }

    public Multifield(Resource resource) {
        this.resource = resource;
    }

    public List<MultiFieldModel> getList() {
        try {
            if((resource != null) && (!ResourceUtil.isSyntheticResource(resource))){
                ValueMap valueMap = resource.adaptTo(ValueMap.class);
                String[] strings = valueMap.get("pages", String[].class);
                if(strings != null){
                    for (String str : strings){
                        JSONObject jsonObject = new JSONObject(str);
                        MultiFieldModel model = new MultiFieldModel();
                        if(jsonObject.has("link")){
                            String link = (String) jsonObject.get("link");
                            model.setLink(link).setEncodedName(JcrUtil.createValidName(link));
                        }
                        if(jsonObject.has("path")){
                            model.setPath(Resources.getValidURL(resource.getResourceResolver(), (String) jsonObject.get("path")) );
                        }
                        if(jsonObject.has("icon")){
                            model.setIcon((String) jsonObject.get("icon"));
                        }
                        if(jsonObject.has("iconsize")){
                            model.setIconsize((String) jsonObject.get("iconsize"));
                        }
                        list.add(model);
                    }
                }
            }
        } catch (JSONException exp) {
            LOG.error("Exception during json object formation." + exp.getMessage());
        }
        return list;
    }

    public static final class MultiFieldModel {
        private String link, path, icon, iconsize, encodedName;

        public String getLink() {
            return link;
        }

        public MultiFieldModel setLink(String link) {
            this.link = link;
            return this;
        }

        public String getPath() {
            return path;
        }

        public MultiFieldModel setPath(String path) {
            this.path = path;
            return this;
        }

        public String getIcon() {
            return icon;
        }

        public MultiFieldModel setIcon(String icon) {
            this.icon = icon;
            return this;
        }

        public String getIconsize() {
            return iconsize;
        }

        public MultiFieldModel setIconsize(String iconsize) {
            this.iconsize = iconsize;
            return this;
        }

        public String getEncodedName() {
            return encodedName;
        }

        public MultiFieldModel setEncodedName(String encodedName) {
            this.encodedName = encodedName;
            return this;
        }
    }

}
