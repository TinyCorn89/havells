package com.havells.servlet.catalogPageGenerator

import com.day.cq.commons.jcr.JcrConstants
import org.apache.sling.api.resource.Resource

/**
 * Created by jitendra on 08/08/15.
 */
public class CatalogTemplatesModal {


    public List<ChildModal> getTemplatesPath(Resource resource ){

        List<ChildModal> list = new ArrayList<ChildModal>()
        if(resource != null) {
            resource.getChildren().each { Resource child ->
                if(!child.getName().equals(JcrConstants.JCR_CONTENT)) {
                    list.add(new ChildModal(child.getPath(), child.getName()))
                }
            }
        }
       return list;
    }
}

