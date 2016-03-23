package com.havells.supportUtils;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shashi on 7/9/15.
 */
public class JcrQueryUtil {

    public static final Query getQuery(ResourceResolver resourceResolver, Map<String,String> parameter){
        QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);

        return queryBuilder.createQuery(PredicateGroup.create(parameter), resourceResolver.adaptTo(Session.class));
    }
}
