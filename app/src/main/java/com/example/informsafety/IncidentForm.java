package com.example.informsafety;

// IncidentForm object holds the data entered into a created Incident Form.

public class IncidentForm {

    private int formID;
    private int teacherCreatedID;
    private int childID;
    private int formType;
    private String incidentDate;
    private String incidentTime;
    private String description;
    private String otherNotes;
    private int teacherProvidedID;
    private int teacherCheckedID;
    private int sentToGuardian;
    private int signedByGuardian;
    private String pdfFilename;

    // Create Form object with the info provided by user
    public IncidentForm(int teacherCreatedID, int childID, int formType, String incidentDate, String incidentTime,
                        String description, String otherNotes, int teacherProvidedID, int teacherCheckedID) {
        this.formID = -1;
        this.teacherCreatedID = teacherCreatedID;
        this.childID = childID;
        this.formType = formType;
        this.incidentDate = incidentDate;
        this.incidentTime = incidentTime;
        this.description = description;
        this.otherNotes = otherNotes;
        this.teacherProvidedID = teacherProvidedID;
        this.teacherCheckedID = teacherCheckedID;
        this.sentToGuardian = 0;
        this.signedByGuardian = 0;
        this.pdfFilename = "";
    }


    // toString - for printing
    @Override
    public String toString() {
        return "registrationForm{" +
                "id=" + formID +
                ", teacherCreatedID='" + teacherCreatedID + '\'' +
                ", childID='" + childID + '\'' +
                ", formType='" + formType + '\'' +
                ", incidentDate='" + incidentDate + '\'' +
                ", incidentTime='" + incidentTime + '\'' +
                ", description='" + description + '\'' +
                ", otherNotes='" + otherNotes + '\'' +
                ", teacherProvidedID='" + teacherProvidedID + '\'' +
                ", teacherCheckedID='" + teacherCheckedID + '\'' +
                ", sentToGuardian='" + sentToGuardian + '\'' +
                ", signedByGuardian='" + signedByGuardian + '\'' +
                ", pdfFilename=" + pdfFilename +
                '}';
    }


    // Getters and setters

    public IncidentForm() {
    }

    public int getFormID() {
        return formID;
    }

    public void setFormID(int formID) {
        this.formID = formID;
    }

    public int getTeacherCreatedID() {
        return teacherCreatedID;
    }

    public void setTeacherCreatedID(int teacherCreatedID) {
        this.teacherCreatedID = teacherCreatedID;
    }

    public int getChildID() {
        return childID;
    }

    public void setChildID(int childID) {
        this.childID = childID;
    }

    public int getFormType() {
        return formType;
    }

    public void setFormType(int formType) {
        this.formType = formType;
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

    public String getOtherNotes() {
        return otherNotes;
    }

    public void setOtherNotes(String otherNotes) {
        this.otherNotes = otherNotes;
    }

    public int getTeacherProvidedID() {
        return teacherProvidedID;
    }

    public void setTeacherProvidedID(int teacherProvidedID) {
        this.teacherProvidedID = teacherProvidedID;
    }

    public int getTeacherCheckedID() {
        return teacherCheckedID;
    }

    public void setTeacherCheckedID(int teacherCheckedID) {
        this.teacherCheckedID = teacherCheckedID;
    }

    public int getSentToGuardian() {
        return sentToGuardian;
    }

    public void setSentToGuardian(int sentToGuardian) {
        this.sentToGuardian = sentToGuardian;
    }

    public int getSignedByGuardian() {
        return signedByGuardian;
    }

    public void setSignedByGuardian(int signedByGuardian) {
        this.signedByGuardian = signedByGuardian;
    }

    public String getPdfFilename() {
        return pdfFilename;
    }

    public void setPdfFilename(String pdfFilename) {
        this.pdfFilename = pdfFilename;
    }



}