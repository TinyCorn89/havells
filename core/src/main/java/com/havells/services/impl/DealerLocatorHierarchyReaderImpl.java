package com.havells.services.impl;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.havells.core.model.FetchDealerDetails;
import com.havells.services.DealerLocatorHierarchyReader;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

@Component(label = "DealerLocatorHierarchyReaderImpl", enabled = true, metatype = true, immediate = true,
        description = "Dealer Locator hierarchy reader service, it reads data from given hierarchy and returns the JSON response back")
@Service(DealerLocatorHierarchyReader.class)
public class DealerLocatorHierarchyReaderImpl implements DealerLocatorHierarchyReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DealerLocatorHierarchyReaderImpl.class);

    private static final String STATES_PROPERTY = "state";
    private static final String CITY_PROPERTY = "city";
    private static final String PRODUCTS_PROPERTY = "products";

    @Reference
    QueryBuilder queryBuilder;

    @Override
    public JSONObject getCities(String state, String rootContentPath, ResourceResolver rr) throws JSONException {
        return new FetchDealerDetails(rootContentPath + "/" + state, rr).getChildResourcesJson();
    }

    @Override
    public JSONArray getDealers(Map<String, String> dealerInfo, String rootContentPath, ResourceResolver rr) {
        return getDealersJSON(dealerInfo, rootContentPath, rr);
    }

    private JSONArray getDealersJSON(Map<String, String> dealerInfo, String rootContentPath, ResourceResolver rr) {

        JSONArray dealersArray = new JSONArray();
        try {
            JSONObject dealerJSON;

            if (dealerInfo.containsKey("state")) {
                Map queryMap = new HashMap();

                queryMap.put("type", "nt:unstructured");
                queryMap.put("path", rootContentPath);
                queryMap.put("1_property", STATES_PROPERTY);
                queryMap.put("1_property.value", dealerInfo.get("state"));
                if (!"".equals(dealerInfo.get("city"))) {
                    queryMap.put("2_property", CITY_PROPERTY);
                    queryMap.put("2_property.value", dealerInfo.get("city"));
                }
                if (!"".equals(dealerInfo.get(PRODUCTS_PROPERTY))) {
                    queryMap.put("3_property", PRODUCTS_PROPERTY);
                    queryMap.put("3_property.value", dealerInfo.get("products"));
                }
                Query query = queryBuilder.createQuery(PredicateGroup.create(queryMap), rr.adaptTo(Session.class));
                query.setHitsPerPage(2000);
                SearchResult result = query.getResult();

                LOGGER.info("size" + result.getHits().size());
                for (Hit hit : result.getHits()) {
                    dealerJSON = new JSONObject();
                    ValueMap valueMap = hit.getResource().adaptTo(ValueMap.class);
                    dealerJSON.put("dealername", valueMap.get("dealerName", ""));
                    dealerJSON.put("address", valueMap.get("address", ""));
                    String contactNo = valueMap.get("telephone2", "");
                    dealerJSON.put("contact", valueMap.get("telephone1", "") + (contactNo.equals("") ? "" : "/"+ contactNo) );
                    dealerJSON.put("email", valueMap.get("email", ""));
                    dealerJSON.put("state",dealerInfo.get("state"));
                    dealersArray.put(dealerJSON);
                }
            }
        } catch (JSONException e) {
            LOGGER.error("JSONException Occurred : ", e);
        } catch (RepositoryException e) {
            LOGGER.error("RepositoryException Occurred : ", e);
        }
        return dealersArray;
    }
}

