package com.example.informsafety;

public class ResetPasswordForm {

    private String email;
    private String newPassword;
    private String confirmPassword;


    public ResetPasswordForm(String email, String newPassword, String confirmPassword) {
        this.email = email;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }


    // toString - for printing
    @Override
    public String toString() {
        return "resetPasswordForm{" +
                ", newPassword='" + newPassword + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }


    // Getters and setters

    public ResetPasswordForm() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


}