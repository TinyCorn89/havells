package com.havells.servlet;

import com.havells.services.DealerLocatorHierarchyReader;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@SlingServlet(paths = {"/bin/havells/dealerlocator"}, selectors = {"states", "city"},
        methods = {"GET"}, generateComponent = false)
@Component(label = "DealerLocatorServlet", enabled = true, metatype = false, immediate = true,
        description = "Dealer locator servlet, it returns the dealer's information from a given hierarchy in the CRX based on selectors")
public class DealerLocatorServlet extends SlingAllMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(DealerLocatorServlet.class);
    private static final String STATE_SELECTOR = "state";
    private static final String CITY_SELECTOR = "city";
    private static final String PRODUCT_SELECTOR = "products";

    @Reference
    DealerLocatorHierarchyReader dealerLocatorHierarchyReader;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        ResourceResolver resourceResolver = request.getResourceResolver();
        try {
            PrintWriter out = response.getWriter();
            String selector = request.getRequestPathInfo().getSelectors()[0];
            Map<String, String> parameterMap = request.getParameterMap();
            String contentPath = "";
            if (parameterMap.containsKey("contentPath") && resourceResolver != null) {
                contentPath = request.getParameter("contentPath");
                 if (CITY_SELECTOR.equals(selector) && parameterMap.containsKey(STATE_SELECTOR)) {
                    String state = request.getParameter(STATE_SELECTOR);
                    out.println(dealerLocatorHierarchyReader.getCities(state, contentPath, resourceResolver));
                } else if ("dealer".equals(selector) && parameterMap.containsKey(STATE_SELECTOR)) {
                    Map<String, String> dealerInfo = new HashMap();
                    dealerInfo.put(STATE_SELECTOR, request.getParameter(STATE_SELECTOR));
                    if (parameterMap.containsKey(CITY_SELECTOR)) {
                        dealerInfo.put(CITY_SELECTOR, request.getParameter(CITY_SELECTOR));
                    }
                    dealerInfo.put(PRODUCT_SELECTOR, request.getParameter(PRODUCT_SELECTOR));
                    out.println(dealerLocatorHierarchyReader.getDealers(dealerInfo, contentPath, resourceResolver));
                }
            }
        } catch (Exception ex) {
            LOGGER.error("IOException occured in Dealer locator servlet : ", ex);
        } finally {
            if (resourceResolver != null) {
                resourceResolver.close();
            }
        }
    }
}
