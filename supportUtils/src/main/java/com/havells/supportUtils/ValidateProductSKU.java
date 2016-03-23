package com.havells.servlet.pim;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(immediate = true, metatype = false, label = "VerifyUserName")
@Service
@Properties(value = {
        @Property(name = "sling.servlet.methods", value = "GET"),
        @Property(name = "sling.servlet.paths", value = "/bin/validateSku.json")
})
public class ValidateProductSKU extends SlingAllMethodsServlet {

    protected static final Logger LOG = LoggerFactory.getLogger(ValidateProductSKU.class);
    private static final String PRODUCT_PATH = "/etc/commerce/products/havells";

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try {
            QueryBuilder queryBuilder = request.getResourceResolver().adaptTo(QueryBuilder.class);
            String skuCode = request.getParameter("sku");
            Map<String, String> parameter = new HashMap<String, String>();
            parameter.put("path", PRODUCT_PATH);
            parameter.put("type", "nt:unstructured");
            parameter.put("property", "sling:resourceType");
            parameter.put("property.value", "commerce/components/product");
            parameter.put("property", "identifier");
            parameter.put("property.value", skuCode);
            Query query = queryBuilder.createQuery(PredicateGroup.create(parameter), request.getResourceResolver().adaptTo(Session.class));
            query.setStart(0);
            SearchResult result = query.getResult();
            LOG.info("SearchResult is " + result.getQueryStatement());
            List<Hit> hits = result.getHits();
            if (hits.size() > 0) {
                response.setStatus(SlingHttpServletResponse.SC_EXPECTATION_FAILED);
                response.getWriter().write("SKU found".toString());
            } else {
                response.setStatus(SlingHttpServletResponse.SC_OK);
                response.getWriter().write(hits.size() + "SKU not found".toString());
            }

        } catch (Exception ex) {
            response.setStatus(SlingHttpServletResponse.SC_EXPECTATION_FAILED);
        }
    }
}
