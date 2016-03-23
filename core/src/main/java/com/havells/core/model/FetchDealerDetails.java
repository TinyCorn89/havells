package com.havells.core.model;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class FetchDealerDetails {

    private static final Logger LOG = LoggerFactory.getLogger(FetchDealerDetails.class);
    private ResourceResolver resourceResolver;
    private String resourcePath, dealerPath;

    private final String DEALER_PRODUCTS_PATH = "/etc/havells/dealerproducts";

    public FetchDealerDetails(String path, ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
        this.resourcePath = path;
    }
    public FetchDealerDetails(String path, String dealerPath, ResourceResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
        this.resourcePath = path;
        this.dealerPath = dealerPath;
    }

    public static <K extends Comparable,V extends Comparable> Map<K,V> sortByKeys(Map<K,V> map){
        List<K> keys = new LinkedList<K>(map.keySet());
        Collections.sort(keys);
        Map<K,V> sortedMap = new LinkedHashMap<K,V>();
        for(K key: keys){
            sortedMap.put(key, map.get(key));
        }
        return sortedMap;
    }

    public JSONObject getChildResourcesJson() throws JSONException {
        Map <String,String> map = getSortedMap();
        return new JSONObject().put("map",map);
    }

    public Map<String,String> getSortedMap() {
        Map<String, String> mapObject = new TreeMap<String, String>();
        if (!StringUtils.isEmpty(resourcePath)) {
            Resource rootResource = resourceResolver.getResource(resourcePath);
            if (rootResource != null && rootResource.hasChildren()) {
                for (Resource childRes : rootResource.getChildren()) {
                    String niceName = childRes.adaptTo(ValueMap.class).get("niceName").toString();
                    mapObject.put(childRes.getName(),StringUtils.isEmpty(niceName)?"":niceName);
                }
            }
        }
        mapObject = sortByKeys(mapObject);
        return mapObject;
    }

    public List<String> getDealerProducts(){
        List<String> list = new ArrayList<String>();
        if (!StringUtils.isEmpty(dealerPath)) {
            Resource rootResource = resourceResolver.getResource(dealerPath);
            if (rootResource != null && rootResource.hasChildren()) {
                for (Resource childRes : rootResource.getChildren()) {
                    list.add(childRes.adaptTo(ValueMap.class).get("productName").toString());
                }
            }
        }
        return list;
    }
}
