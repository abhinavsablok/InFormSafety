package com.example.informsafety;

// UserModel object holds data of the logged in user.

public class UserModel {

    private int userID;
    private int teacherID;
    private int guardianID;
    public boolean isTeacher;
    private String name;
    private String email;
    private String phone;
    private String password;


    public UserModel(int userID, int teacherID, int guardianID, boolean isTeacher, String name, String email, String phone, String password) {
        this.userID = userID;
        this.teacherID = teacherID;
        this.guardianID = guardianID;
        this.isTeacher = isTeacher;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public UserModel(String mName, String mContact, String mEmail, String mPassword, String mConfirmPassword) {
    }


    // toString - for printing
    @Override
    public String toString() {
        return "userModel{" +
                "id=" + userID +
                ", teacherID='" + teacherID + '\'' +
                ", guardianID='" + guardianID + '\'' +
                ", isTeacher=" + isTeacher + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password +
                '}';
    }


    // Getters and setters

    public UserModel() {
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public int getGuardianID() {
        return guardianID;
    }

    public void setGuardianID(int guardianID) {
        this.guardianID = guardianID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

}