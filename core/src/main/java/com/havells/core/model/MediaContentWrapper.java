package com.havells.core.model;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ttnd on 30/7/15.
 */
public class MediaContentWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(MediaContentWrapper.class);

    private SlingHttpServletRequest request;
    private int maxItemsPerPage;
    private List<String> links;
    private String yearSelector;
    private int currentContentPage;
    private int lastContentPage;
    private int linksSize;
    private int startIndex;
    private int lastIndex;
    private String dataPath;


    public MediaContentWrapper(SlingHttpServletRequest slingRequest, Resource resource) {
        String contentPath = null;
        links = new ArrayList<String>();
        lastContentPage = 1;
        currentContentPage = 1;

        try {
            yearSelector = slingRequest.getRequestPathInfo().getSelectors()[1];
            currentContentPage = Integer.parseInt(slingRequest.getRequestPathInfo().getSelectors()[2]);

            ValueMap valueMap = resource.getValueMap();
            if (valueMap.containsKey("items")) {
                maxItemsPerPage = valueMap.get("items", Integer.class);
            }
            if (valueMap.containsKey("path")) {
                contentPath = valueMap.get("path", String.class) + "/" + yearSelector;
            }

            dataPath = resource.getPath() + ".data." + yearSelector + ".";
            PageManager pm = slingRequest.getResourceResolver().adaptTo(PageManager.class);
            Page contentPage = null;
            if (contentPath != null) {
                contentPage = pm.getPage(contentPath);
            }

            if (contentPage != null) {
                for (Iterator<Page> it = contentPage.listChildren(); it.hasNext(); ) {
                    Page childPage = it.next();
                    if (resource.getResourceResolver().getResource(childPage.getPath() + "/jcr:content/par") != null) {
                        String url = childPage.getPath() + "/jcr:content/par.html";
                        links.add(url);
                    }
                }
            }

            if (links.size() > 0) {
                lastContentPage = (int) Math.ceil(links.size() / (double) maxItemsPerPage);
                linksSize = links.size();
                startIndex = maxItemsPerPage * (currentContentPage - 1);
                lastIndex = Math.min(links.size(), maxItemsPerPage * (currentContentPage));
            }
        } catch (Exception exception) {
            LOG.error("Exception while getting list os Content list: " + exception);
        }

    }

    public final int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }

    public final List<String> getLinks() {
        return links;
    }

    public final int getCurrentContentPage() {
        return currentContentPage;
    }

    public final String getYearSelector() {
        return yearSelector;
    }

    public final int getLastContentPage() {
        return lastContentPage;
    }

    public final int getLinksSize() {
        return linksSize;
    }

    public final int getStartIndex() {
        return startIndex;
    }

    public final int getLastIndex() {
        return lastIndex;
    }

    public final String getDataPath() {
        return dataPath;
    }

}
