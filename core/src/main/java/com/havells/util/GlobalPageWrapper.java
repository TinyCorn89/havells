package com.havells.util;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.ValueMap;

public class GlobalPageWrapper {
    private static final String GLOBAL_PAGE_PROP = "globalPagePath" ;

    private String globalPage = "/content/havells/en/globalpage";

    public GlobalPageWrapper(Page page) {
       Page parent = page.getAbsoluteParent(2);
       if(parent != null) {
           ValueMap valueMap = parent.getProperties();

           if (valueMap.get(GLOBAL_PAGE_PROP, null) != null) {
               this.globalPage = (String) valueMap.get(GLOBAL_PAGE_PROP);
           }
       }
    }

    public String getGlobalPage() {
        return globalPage;
    }
}
