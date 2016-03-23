package com.havells.servlet;


import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.NameConstants;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(immediate = true, metatype = true, label = "JobListingServlet")
@Service
@Properties(value = {
        @Property(name = "sling.servlet.methods", value = "GET", propertyPrivate = true),
        @Property(name = "sling.servlet.paths", value = "/bin/servlets/JobListingServlet", propertyPrivate = true)
})
public class JobListingServlet extends SlingSafeMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(JobListingServlet.class);


    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws
            IOException {

        Resource jobListResource = request.getResourceResolver().getResource(request.getParameter("path"));
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = null;
        try {
            if(jobListResource != null){
                int jobCount = 0;
                Iterator<Resource> itr = jobListResource.listChildren();
                jsonArray = new JSONArray();
                while (itr.hasNext()) {
                    Resource jobDetailResource = itr.next();
                    if (jobDetailResource.isResourceType(NameConstants.NT_PAGE)) {
                        Resource jobContentResource = jobDetailResource.getChild(JcrConstants.JCR_CONTENT+"/rightPar/job_application_form");
                        if(jobContentResource != null){
                            ValueMap valueMap = jobContentResource.getValueMap();
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("count", String.valueOf(++jobCount));
                            map.put("branch", valueMap.get("branch", String.class));
                            map.put("product", valueMap.get("product", String.class));
                            map.put("experience", valueMap.get("experience", String.class));
                            map.put("location", valueMap.get("location", String.class));
                            map.put("position", valueMap.get("designation", String.class));
                            map.put("hashOfPosition", valueMap.get("hashOfPosition", String.class));
                            map.put("industryPrefrences", valueMap.get("industryPrefrences", String.class));
                            map.put("path", jobDetailResource.getPath());
                            jsonArray.put(map);
                        }
                    }
                }
            }
            jsonObject.put("items", jsonArray);
            jsonObject.put("totalItems", jsonArray.length());
        } catch (JSONException ex) {
            LOG.error("JSONException: ", ex);
        }
        response.getWriter().write(jsonObject.toString());
    }
}
