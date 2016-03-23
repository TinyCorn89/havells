package com.havells.core.model;

import com.day.cq.commons.jcr.JcrConstants;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Component(immediate = true, label = "Annual Report Servlet")
@Service(AnnualReports.class)
public class AnnualReports {

    private Logger log = LoggerFactory.getLogger(AnnualReports.class);
    private List<AnnualReportsList> list = new ArrayList<AnnualReportsList>();

    private int currentContentPage;
    private int linksSize;
    private int startIndex;
    private int lastIndex;

    private int maxItemsPerPage;
    private int lastContentPage;
    private String dataPath;
    private int count = 0;

    public AnnualReports() {
    }

    public AnnualReports(SlingHttpServletRequest slingHttpServletRequest, Resource resource) {

        currentContentPage = 1;
        lastContentPage = 1;
        currentContentPage = Integer.parseInt(slingHttpServletRequest.getRequestPathInfo().getSelectors()[1]);
        ValueMap valueMap = resource.getValueMap();
        if (valueMap.containsKey("items")) {
            maxItemsPerPage = valueMap.get("items", Integer.class);
        }
        if (valueMap.containsKey("path")) {
            String contentPath = valueMap.get("path", String.class);
            dataPath = resource.getPath() + ".data.";
            Resource contentPathRes = slingHttpServletRequest.getResourceResolver().getResource(contentPath);
            if(contentPathRes != null) {
                for (Iterator<Resource> it = contentPathRes.listChildren(); it.hasNext(); ) {
                    Resource years = it.next();
                    count++;
                    if(years.hasChildren()) {
                        for (Resource pdfPath : years.getChildren()) {
                            String pdfName = pdfPath.getName();
                            if(!pdfName.equals(JcrConstants.JCR_CONTENT))
                            {
                                list.add(new AnnualReportsList(years.getName(), pdfPath.getPath(), pdfName ));
                                break;
                            }
                        }
                    }
                }
                if (list.size() > 0) {
                    lastContentPage = list.size() / maxItemsPerPage;
                    if (list.size() / maxItemsPerPage != 0)
                        lastContentPage++;
                    linksSize = list.size();
                    startIndex = maxItemsPerPage * (currentContentPage - 1);
                    lastIndex = Math.min(list.size(), maxItemsPerPage * (currentContentPage));
                }
            }
        }

        Collections.sort(list, new Comparator<AnnualReportsList>() {
            @Override
            public int compare(AnnualReportsList o1, AnnualReportsList o2) {
                return o2.getYearName().compareTo(o1.getYearName());
            }
        });
    }

    public String getDataPath() {
        return dataPath;
    }

    public int getLastContentPage() {
        return lastContentPage;
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }


    public int getCount() {
        return count;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public int getCurrentContentPage() {
        return currentContentPage;
    }

    public int getLinksSize() {
        return linksSize;
    }

    public List<AnnualReportsList> getList() {
        return list;
    }
}
