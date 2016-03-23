package com.havells.core.model;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.havells.commons.sling.Resources;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.omg.CORBA.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.*;

/**
 * The Model SearchFacetModel.
 *
 * @author karan sharma This model provides the details about Search facets
 *
 */
@Model(adaptables = { SlingHttpServletRequest.class, Request.class,Resource.class,Tag.class})
public class SearchFacetsModel {
    private static final Logger LOG = LoggerFactory.getLogger(SearchFacetsModel.class);

    @Inject
    Resource resource;
    @Inject
    private SlingHttpServletRequest request;

    private ResourceResolver resolver;
    private String facetTypeTitle;
    private String facetTypeName;
    private Map<String,String> facetValuesMap;
    private TagManager tagManager;

    public String getFacetTypeTitle() { return facetTypeTitle; }
    public String getFacetTypeName() {return facetTypeName; }
    public Map<String, String> getFacetValuesMap() { return facetValuesMap; }

    @PostConstruct
    protected void init() {
        tagManager = request.getResourceResolver().adaptTo(TagManager.class);
        //resolver = request.getResourceResolver();
        ValueMap valueMap = resource.getValueMap();
        List<String> filterValuesPathsList = new ArrayList<String>();
        String filterType = valueMap.get("filterType", String.class);
        try {
            boolean isFilterValueMultiple = Resources.isPropertyMultiple(resource, "filterValues");
            String[] filterValuePaths;

            if(isFilterValueMultiple){
                filterValuePaths = valueMap.get("filterValues", String[].class);
            }else{
                String searchedValue = valueMap.get("filterValues", String.class);
                searchedValue = StringUtils.isEmpty(searchedValue) ? StringUtils.EMPTY : searchedValue;
                filterValuePaths = searchedValue.split(",");
            }

            if(ArrayUtils.isNotEmpty(filterValuePaths)){
                for(String filterValuePath : filterValuePaths) {
                    filterValuesPathsList.add(filterValuePath);
                }
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        setTagInfo(filterType,filterValuesPathsList);
    }



    private void setTagInfo(String filterType, List<String> filterValuesPathsList) {

        this.facetValuesMap = new HashMap<String, String>();
        Tag filterTypeTag = tagManager.resolve(filterType);
        if(filterTypeTag != null){
            this.facetTypeName = filterTypeTag.getName().trim();
            try {
                this.facetTypeTitle = filterTypeTag.adaptTo(Node.class).getProperty(JcrConstants.JCR_TITLE).getString().trim();
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }

        for(String path : filterValuesPathsList){
            Tag filerValueTag = tagManager.resolve(path);
            if(filerValueTag != null){
                try {
                    this.facetValuesMap.put(filerValueTag.adaptTo(Node.class).getProperty(JcrConstants.JCR_TITLE).getString().trim(),filerValueTag.getTitle().trim());
                } catch (RepositoryException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
