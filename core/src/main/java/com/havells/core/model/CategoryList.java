package com.havells.core.model;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CategoryList {

    ResourceResolver resourceResolver;
    String path;

    public CategoryList(ResourceResolver resourceResolver, String path) {
        this.resourceResolver = resourceResolver;
        this.path = path;
    }

    public List<Categories> getCategoryList() {

        Categories categories = null;
        List<Categories> categoriesList = new ArrayList<Categories>();

        PageManager pm = resourceResolver.adaptTo(PageManager.class);

        if (pm != null) {
            Page currentPage = pm.getPage(path);
            Iterator<Page> iterator = currentPage.listChildren();
            while (iterator.hasNext()) {
                Page childPage = iterator.next();
                categories = new Categories();
                categories.setCategory(childPage.getTitle() == null ? childPage.getName() : childPage.getTitle());
                categories.setLink(childPage.getPath());

                Resource pageResource = childPage.adaptTo(Resource.class);
                Resource imageResource = pageResource.getChild(JcrConstants.JCR_CONTENT + "/image");
                if (imageResource != null)
                    categories.setImgPath(imageResource.getValueMap().get("fileReference", String.class));
                categoriesList.add(categories);
            }
        }
        return categoriesList;
    }
}
