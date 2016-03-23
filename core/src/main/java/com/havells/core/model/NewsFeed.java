package com.havells.core.model;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsFeed {
    private static final Logger LOG = LoggerFactory.getLogger(NewsFeed.class);
    private Resource resource;
    private ResourceResolver resourceResolver;
    private final String DATE_INFO_NODE = "left/richtexteditor";
    private final String PRESS_TITLE_INFO_NODE = "par/richtexteditor";
    private final String NEWS_MEDIA_RESOURCE_TYPE = "havells/components/content/news-and-media-content";

    public NewsFeed(Resource resource) {
        this.resource = resource;
        resourceResolver = resource.getResourceResolver();
    }

    public List getPressReleases(){
        QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
        List<ArticleComponent> list = new ArrayList<ArticleComponent>();
        ArticleComponent articleComponent;
        try {
            if (resource != null) {
                ValueMap valueMap = resource.adaptTo(ValueMap.class);
                String path = valueMap.get("path", "");
                if (!path.equals("")) {
                    Map<String, String> parameter = new HashMap<String, String>();
                    parameter.put("path", path);
                    parameter.put("type", JcrConstants.NT_UNSTRUCTURED);
                    parameter.put("property", JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY);
                    parameter.put("property.value",NEWS_MEDIA_RESOURCE_TYPE );
                    parameter.put("orderby", "@jcr:lastModified");

                    Query query = queryBuilder.createQuery(PredicateGroup.create(parameter), resource.getResourceResolver().adaptTo(Session.class));
                    query.setStart(0);
                    SearchResult result = query.getResult();
                    List<Hit> hits = result.getHits();
                    for (Hit temp : hits) {
                        Resource res = temp.getResource();
                        LOG.info("resource found "+res.getPath());
                        Resource date = res.getChild(DATE_INFO_NODE);
                        Resource pressTitle = res.getChild(PRESS_TITLE_INFO_NODE);
                        if(date != null && pressTitle != null){
                            ValueMap dateProp = date.adaptTo(ValueMap.class);
                            ValueMap pressTitleProp = pressTitle.adaptTo(ValueMap.class);
                            articleComponent = new ArticleComponent();
                            articleComponent.setDate(dateProp.get("richtext", ""));
                            articleComponent.setArticleTitle(pressTitleProp.get("richtext", ""));
                            list.add(articleComponent);
                        }
                    }
                }
            }
            LOG.info("found articles "+list);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }
}
