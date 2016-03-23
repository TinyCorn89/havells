package com.havells.services;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import java.util.Map;

public interface DealerLocatorHierarchyReader {

    JSONObject getCities(String state, String rootContentPath, ResourceResolver rr) throws JSONException;

    JSONArray getDealers(Map<String, String> dealerInfo, String rootContentPath, ResourceResolver rr);

}
