package com.havells.core.model;

public class JobData {
    private String department;
    private String location;
    private String jobTitle;
    private String postDate;
    private String path;
    private String jobId;
    private String departmentMailId;
    private String departmentMailSubject;

    public String getJobId() {

        return jobId;
    }

    public String getDepartmentMailId() {
        return departmentMailId;

    }

    public void setDepartmentMailId(String departmentMailId) {
        this.departmentMailId = departmentMailId;
    }

    public String getDepartmentMailSubject() {
        return departmentMailSubject;
    }

    public void setDepartmentMailSubject(String departmentMailSubject) {
        this.departmentMailSubject = departmentMailSubject;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setJobId(String jobId) {
        String[] pathArray= jobId.split("/");
        this.jobId = pathArray[pathArray.length-1];
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}

