package com.havells.supportUtils.servlet;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SlingServlet(
        methods = "POST",
        paths = "/bin/updateProductListing")
@Properties({
        @Property(name="service.description",value="this is a servlet", propertyPrivate=false),
        @Property(name="service.vendor",value="Havells", propertyPrivate=false),
})
public class UpdateProductList extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(UpdateProductList.class);

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        doPost(request, response);
    }
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        String path = request.getParameter("path");
        try{
            //form a query to get all pages with property cq:template
            ResourceResolver resourceResolver = request.getResourceResolver();
            Session session = resourceResolver.adaptTo(Session.class);
            QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);

            Map map = new HashMap();
            map.put("path", path);
            map.put("type", "cq:Page");
            map.put("1_property", "jcr:content/cq:template");
            map.put("1_property.value", "/apps/havells/templates/productListing");
            map.put("2_property", "jcr:content/sling:resourceType");
            map.put("2_property.value", "havells/components/page/categoryPage");
            map.put("p.limit", "-1");
            LOG.info("The Query for updating product listing pages: {}" + map.toString());

            Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
            SearchResult result = query.getResult();

            for (Hit hit : result.getHits()) {
                String resourcePath = hit.getPath() + "/jcr:content";
                Node node = resourceResolver.resolve(resourcePath).adaptTo(Node.class);
                LOG.info("sling resource type",node.getProperty("sling:resourceType"));
                node.setProperty("sling:resourceType", "havells/components/page/productListingPage");
                node.getSession().save();
            }

        }catch (Exception ex){
            LOG.error("Exception occurs in building json: " + ex);
        }
    }
}
