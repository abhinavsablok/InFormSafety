package com.example.informsafety;

// IncidentForm object holds the data entered into a created Incident Form.

import java.util.HashMap;

public class IncidentForm {

    //    private int formID;
    private String userID;
    private String childName;
    private String incidentDate;
    private String incidentTime;
    private String description;
    private String location;
    private String treatment;
    private String teacherProvided;
    private String teacherChecked;
    private String comments;
    private boolean sentToGuardian;
    private boolean signedByGuardian;
    private String pdfFilename;

    // Create Form object with the info provided by user
    public IncidentForm(String userID, String childName, String incidentDate, String incidentTime,
                        String description, String location, String treatment,
                        String teacherProvided, String teacherChecked, String comments) {
//        this.formID = -1;
        this.userID = userID;
        this.childName = childName;
        this.incidentDate = incidentDate;
        this.incidentTime = incidentTime;
        this.description = description;
        this.location = location;
        this.treatment = treatment;
        this.teacherProvided = teacherProvided;
        this.teacherChecked = teacherChecked;
        this.comments = comments;
        this.sentToGuardian = false;
        this.signedByGuardian = false;
        this.pdfFilename = "";
    }


    // toHashMap - for entering into Firebase
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userID", userID);
        map.put("childName", childName);
        map.put("incidentDate", incidentDate);
        map.put("incidentTime", incidentTime);
        map.put("description", description);
        map.put("location", location);
        map.put("treatment", treatment);
        map.put("teacherProvided", teacherProvided);
        map.put("teacherChecked", teacherChecked);
        map.put("comments", comments);
        map.put("sentToGuardian", sentToGuardian);
        map.put("signedByGuardian", signedByGuardian);
        map.put("pdfFilename", pdfFilename);
        return map;
    }


    @Override
    public String toString() {
        return "IncidentForm{" +
                "userID='" + userID + '\'' +
                ", childName='" + childName + '\'' +
                ", incidentDate='" + incidentDate + '\'' +
                ", incidentTime='" + incidentTime + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", treatment='" + treatment + '\'' +
                ", teacherProvided='" + teacherProvided + '\'' +
                ", teacherChecked='" + teacherChecked + '\'' +
                ", comments='" + comments + '\'' +
                ", sentToGuardian=" + sentToGuardian +
                ", signedByGuardian=" + signedByGuardian +
                ", pdfFilename='" + pdfFilename + '\'' +
                '}';
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(String incidentDate) {
        this.incidentDate = incidentDate;
    }

    public String getIncidentTime() {
        return incidentTime;
    }

    public void setIncidentTime(String incidentTime) {
        this.incidentTime = incidentTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getTeacherProvided() {
        return teacherProvided;
    }

    public void setTeacherProvided(String teacherProvided) {
        this.teacherProvided = teacherProvided;
    }

    public String getTeacherChecked() {
        return teacherChecked;
    }

    public void setTeacherChecked(String teacherChecked) {
        this.teacherChecked = teacherChecked;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isSentToGuardian() {
        return sentToGuardian;
    }

    public void setSentToGuardian(boolean sentToGuardian) {
        this.sentToGuardian = sentToGuardian;
    }

    public boolean isSignedByGuardian() {
        return signedByGuardian;
    }

    public void setSignedByGuardian(boolean signedByGuardian) {
        this.signedByGuardian = signedByGuardian;
    }

    public String getPdfFilename() {
        return pdfFilename;
    }

    public void setPdfFilename(String pdfFilename) {
        this.pdfFilename = pdfFilename;
    }
}