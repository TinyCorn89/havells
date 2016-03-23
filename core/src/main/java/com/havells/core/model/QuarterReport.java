package com.havells.core.model;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class QuarterReport {

    private List<String> yearList = new ArrayList<String>();
    private JSONObject pdfResources = new JSONObject();
    private Logger logger = LoggerFactory.getLogger(QuarterReport.class);

    public QuarterReport(Resource res, String pathToFolder) {
        if (res != null) {
            Resource resource = res.getResourceResolver().getResource(pathToFolder);
           try {
                if (resource != null) {
                    setYearList(resource);
                    setPdfResources(resource);
                }
            } catch (JSONException e) {
                logger.error("Error while putting data in pdfResource ", e.getMessage());
            }
        }
    }

    private void setYearList(Resource resource) {
        Iterator<Resource> yearFolders = resource.listChildren();

        while (yearFolders.hasNext()) {
            String year = yearFolders.next().getName();
            if (!year.equals(JcrConstants.JCR_CONTENT)) {
                yearList.add(year);
            }
        }
        Collections.sort(yearList, Collections.reverseOrder());
    }

    /**
     * iterate over the resource and finds the folder structure and creates exact json
     *
     * @param resource
     * @return
     * @throws JSONException
     */
    public void setPdfResources(Resource resource) throws JSONException {
        Iterator<Resource> yearIterator = resource.listChildren();
        JSONArray yearArray = new JSONArray();
        while (yearIterator.hasNext()) {
            Resource year = yearIterator.next();
            JSONArray quarterArray = new JSONArray();

            resolveQuarterToJSON(year, quarterArray);

            JSONObject yearJSON = new JSONObject();
            yearJSON.put("year", year.getName());
            yearJSON.put("quarters", quarterArray);

            yearArray.put(yearJSON);
        }


        pdfResources.put("report", yearArray);
    }

    private void resolveDocumentToJSON(Resource quarter, JSONArray documentArray) throws JSONException {
        Iterator<Resource> monthIterator = quarter.listChildren();

        while (monthIterator.hasNext()) {
            Resource document = monthIterator.next();
            JSONObject documentJson = new JSONObject();
            try {
                String documentName = getName(document);
                if (JcrConstants.JCR_CONTENT.equals(document.getName())) {
                    continue;
                }

                documentJson.put("title", documentName);
                documentJson.put("href", document.getPath());
                documentArray.put(documentJson);

            } catch (Exception e) {
                logger.info("exception occurs");
            }
        }
    }

    private String getName(Resource pdfResource) {
        Resource metadata = pdfResource.getChild(JcrConstants.JCR_CONTENT + "/metadata");
        String title = "";
        if (metadata != null) {
            title = pdfResource.getName();
            Node node = metadata.adaptTo(Node.class);
            Property prop = null;
            try {
                if (node.hasProperty("dc:title")) {
                    prop = node.getProperty("dc:title");
                    if (prop != null)
                        title = prop.isMultiple() ? metadata.adaptTo(ValueMap.class).get("dc:title", String[].class)[0] :
                                metadata.adaptTo(ValueMap.class).get("dc:title", String.class);
                }
            } catch (RepositoryException ex) {
               logger.info("exception occured");
            }
        }
        return title;
    }

    private void resolveQuarterToJSON(Resource year, JSONArray quarterArray) throws JSONException {
        Iterator<Resource> quarterIterator = year.listChildren();
        boolean isJcrContent = false;
        while (quarterIterator.hasNext()) {
            Resource quarter = quarterIterator.next();
            JSONArray documentArray = new JSONArray();
            isJcrContent = quarter.getName().equals(JcrConstants.JCR_CONTENT);

            if (!isJcrContent) {
                resolveDocumentToJSON(quarter, documentArray);

                JSONObject quarterJSON = new JSONObject();
                quarterJSON.put("quarter", quarter.getName());
                quarterJSON.put("documents", documentArray);

                quarterArray.put(quarterJSON);
            }
        }
    }

    public List<String> getYearList() {
        return yearList;
    }

    public JSONObject getPdfResources() {
        return pdfResources;
    }
}
