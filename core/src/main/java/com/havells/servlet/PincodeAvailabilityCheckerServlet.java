package com.havells.servlet;

import com.havells.core.model.PincodeAvailabilityChecker;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component(label = "Pin Checker", immediate = true, metatype = false)
@Service
@Properties(value = {
        @Property(name = "sling.servlet.methods", value = "GET"),
        @Property(name = "sling.servlet.paths", value = "/bin/pinChecker")
})
public class PincodeAvailabilityCheckerServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = (Logger) LoggerFactory.getLogger("PincodeAvailabilityChecker LOGS");

    public static final String JO_PINCODE = "pincode";
    public static final String JO_COD = "cod";
    public static final String JO_MESSAGE = "message";

    public static final String RP_PINCODE = "pin";
    public static final String RP_PINCODE_LOCATION = "pinloc";

    public static final String PN_PINCODE = "pincode";
    public static final String PN_COD = "cod";


    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        final String pincode = request.getParameter(RP_PINCODE);
        final String pincodeLocation = request.getParameter(RP_PINCODE_LOCATION);
        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource resource = resourceResolver.getResource(pincodeLocation + "/" + pincode);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            if (resource != null) {
                ValueMap valueMap = resource.adaptTo(ValueMap.class);
                PincodeAvailabilityChecker pincodeAvailabilityChecker = new PincodeAvailabilityChecker((String) valueMap.get(PN_PINCODE), (Boolean) valueMap.get(PN_COD));
                jsonObject.put(JO_PINCODE, pincodeAvailabilityChecker.getPincode());
                jsonObject.put(JO_COD, pincodeAvailabilityChecker.isCod());
                jsonObject.put(JO_MESSAGE, PincodeAvailabilityChecker.Status.available);
                jsonArray.put(jsonObject);
            } else {
                jsonObject.put(JO_PINCODE, "000000");
                jsonObject.put(JO_COD, false);
                jsonObject.put(JO_MESSAGE, PincodeAvailabilityChecker.Status.unavailable);
                jsonArray.put(jsonObject);
            }
        } catch (JSONException e) {
            LOG.error("JSONException in PincodeAvailabilityCheckerImpl :: " + e);
        }
        response.getWriter().write(jsonObject.toString());
    }


}
