package com.havells.core.model;

public class AnnualReportsList {
    private String yearName;
    private String pdfPath;
    private String pdfName;

    public String getYearName() {
        return yearName;
    }

    public AnnualReportsList(String yearName, String pdfPath, String pdfName) {
        this.yearName = yearName;
        this.pdfPath = pdfPath;
        this.pdfName = pdfName;
    }
    public void setYearName(String yearName) {
        this.yearName = yearName;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }
}
