package com.example.informsafety;

public class LoginForm {

    private String email;
    private String password;


    public LoginForm(String email, String password) {
        this.email = email;
        this.password = password;
    }


    // toString - for printing
    @Override
    public String toString() {
        return "loginForm{" +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


    // Getters and setters

    public LoginForm() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}