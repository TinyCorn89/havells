package com.havells.core.model;

import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Manisha Bano on 6/8/15
 *
 * This class will populate all the Havells job.
 */
public class PopulateHavellsJob {

    ResourceResolver resourceResolver;
    private String location;

    public PopulateHavellsJob(ResourceResolver resourceResolver, String location) {
        this.resourceResolver = resourceResolver;
        this.location = location;
    }

    /**
     * This method will get the list of all the jobs which are use to populate the job detail page and the job
     * designation dropdown of apply job form page.
     *
     * @return list of all jobs.
     */
    public List<JobData> getJobsList() {

        JobData jobData = null;
        List<JobData> jobDataList = new ArrayList<JobData>();

        Resource resource = resourceResolver.getResource(location);
        if (resource != null) {
            for (Resource res : resource.getChildren()) {
                jobData = new JobData();
                jobData.setPath(res.getPath());
                jobData.setJobId(res.getPath());
                location = res.getPath() + "/" + JcrConstants.JCR_CONTENT + "/rightPar/job_application_form";
                res = resourceResolver.getResource(location);
                if (res != null) {
                    ValueMap valueMap = res.adaptTo(ValueMap.class);
                    jobData.setDepartment(valueMap.get("department", String.class));
                    jobData.setDepartmentMailId(valueMap.get("departmentMailId", String.class));
                    jobData.setPostDate(valueMap.get("posting", String.class));
                    jobData.setLocation(valueMap.get("location", String.class));
                    jobData.setJobTitle(valueMap.get("designation", String.class));
                    jobDataList.add(jobData);
                }
            }
        }
        return jobDataList;
    }

    public String getJobsDesignation() {
        Resource resource = resourceResolver.getResource(location + "/" + JcrConstants.JCR_CONTENT + "/rightPar/job_application_form");
        String designation = null;
        if (resource != null) {
            ValueMap valueMap = resource.adaptTo(ValueMap.class);
            designation = valueMap.get("designation", String.class);
        }
        return designation;
    }
}
