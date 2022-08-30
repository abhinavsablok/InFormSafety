package com.example.informsafety;

public class User {

    public String name, contact, email, password, confirmPassword;

    public User(){}

    public User(String n, String c, String e, String p, String cp){
        name = n;
        contact = c;
        email = e;
        password = p;
        confirmPassword = cp;
    }
}