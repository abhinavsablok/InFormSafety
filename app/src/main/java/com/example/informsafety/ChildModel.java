package com.example.informsafety;

// ChildModel object holds data of a child registered at the Centre.

public class ChildModel {

    private int childID;
    private int guardianID;
    private String name;
    private String nickname;


    public ChildModel(int childID, int guardianID, String name, String nickname) {
        this.childID = childID;
        this.guardianID = guardianID;
        this.name = name;
        this.nickname = nickname;
    }


    // toString - for printing
    @Override
    public String toString() {
        return "userModel{" +
                "id=" + childID +
                ", guardianID='" + guardianID + '\'' +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }


    // Getters and setters

    public ChildModel() {
    }

    public int getChildID() {
        return childID;
    }

    public void setChildID(int childID) {
        this.childID = childID;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


}