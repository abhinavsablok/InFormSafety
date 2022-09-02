/*package com.example.informsafety;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivityDB extends AppCompatActivity {

    // Create references to user data
    DatabaseHelper databaseHelper;
    long userID;
    long teacherID;
    long guardianID;
    com.example.informsafety.RegistrationForm registrationForm;
    com.example.informsafety.UserModel userModel;
    LoginForm loginForm;
    com.example.informsafety.ResetPasswordForm resetPasswordForm;
    ChangePasswordForm changePasswordForm;
    ChangeUserDetailsForm changeUserDetailsForm;


    // Create the Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Import database functions
        databaseHelper = new DatabaseHelper(MainActivityDB.this);

        // FOR TESTING ONLY: Empty the database tables and start over
        databaseHelper.deleteRecords();

//      Test Registration
        registrationForm = new RegistrationForm("Teacher 1", "teacher1@huttkindergartens.org.nz", "0210727600", "password01", "password01");
        registerUser(registrationForm);

        registrationForm = new RegistrationForm("Teacher 2", "teacher2@huttkindergartens.org.nz", "0210727598", "password02", "password02");
        registerUser(registrationForm);

        registrationForm = new RegistrationForm("Parent 1", "imaparent@gmail.com", "0270727676", "password03", "password03");
        registerUser(registrationForm);

//        registrationForm = new RegistrationForm("Parent 2", "imaparenttoo@gmail.com", "0220727622", "password04", "password04");
//        registerUser(registrationForm);

////      Test Login
//        loginForm = new LoginForm("imaparent@gmail.com", "password03");
//        logInUser(loginForm);

////      Test Change User Details (through Settings) - must be logged in first
//        changeUserDetailsForm = new ChangeUserDetailsForm("First Teacher", "firstteacher@huttkindergartens.org.nz", "02101010101", "password01");
//        changeUserDetails(userModel, changeUserDetailsForm);
//
////      Test log in with new details
//        loginForm = new LoginForm("firstteacher@huttkindergartens.org.nz", "password01");
//        logInUser(loginForm);
//        Toast.makeText(MainActivity.this, userModel.toString(), Toast.LENGTH_SHORT).show();
//
////      Test Change Password (through Settings) - must be logged in first
//        changePasswordForm = new ChangePasswordForm("password01", "imapassword", "imapassword");
//        changePassword(userModel, changePasswordForm);
//
////      Test log in with new password
//        loginForm = new LoginForm("firstteacher@huttkindergartens.org.nz", "imapassword");
//        logInUser(loginForm);
//
////      Test password reset
//        resetPasswordForm = new ResetPasswordForm("firstteacher@huttkindergartens.org.nz", "mynewpassword", "mynewpassword");
//        resetPassword(resetPasswordForm);
//
////      Test log in with new password
//        loginForm = new LoginForm("firstteacher@huttkindergartens.org.nz", "mynewpassword");
//        logInUser(loginForm);


        // Test insert child (logged in as Parent)
        databaseHelper.insertChild(userModel.getGuardianID(), "Robert", "Bobby Tables");
        int myChildID = databaseHelper.getChildIDFromNickname("Bobby Tables");
        ChildModel childModel = databaseHelper.selectChild(myChildID);
//        Toast.makeText(MainActivity.this, childModel.toString(), Toast.LENGTH_SHORT).show();

        // Log in to Teacher before starting a Form
        loginForm = new LoginForm("teacher1@huttkindergartens.org.nz", "password01");
        logInUser(loginForm);

        // Create a Minor Incident Form (must be logged in as a Teacher)

        // DATE AND TIME
        // From DatePicker and TimePicker we will get Year, Month, Day, Hour, and Minute as Integers
        int mYear = 2022;
        int mMonth = 8;
        int mDay = 23;
        int mHour = 10;
        int mMinute = 15;

        // Create a Calendar object to hold date/time values
        // NOTE: Month is off by one in Calendar (0 = Jan, 1 = Feb, etc.)
        Calendar cal = Calendar.getInstance();
        cal.set(mYear, mMonth - 1, mDay, mHour, mMinute, 0);

        // Convert to Date object
        Date mydate = cal.getTime();
//        Toast.makeText(MainActivity.this, mydate.toString(), Toast.LENGTH_LONG).show();

        // Convert to Date String and Time String using SimpleDateFormat
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String myDateString = dateFormat.format(mydate);
        String myTimeString = timeFormat.format(mydate);
//        Toast.makeText(MainActivity.this, myDateString, Toast.LENGTH_LONG).show();
//        Toast.makeText(MainActivity.this, myTimeString, Toast.LENGTH_LONG).show();

        // Test retrieving Strings and converting back to Date
        Date checkDate = null;
        Date checkTime = null;
        try {
            checkDate = dateFormat.parse(myDateString);
            checkTime = timeFormat.parse(myTimeString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
//        Toast.makeText(MainActivity.this, checkDate.toString(), Toast.LENGTH_LONG).show();
//        Toast.makeText(MainActivity.this, checkTime.toString(), Toast.LENGTH_LONG).show();


        // Create a Minor Incident Form
        IncidentForm incidentForm = new IncidentForm(userModel.getTeacherID(), myChildID, 1, myDateString, myTimeString,
                "Fell off the swing", "", userModel.getTeacherID(), userModel.getTeacherID() + 1);
        long myformID = databaseHelper.insertForm(incidentForm);
        incidentForm.setFormID((int)myformID);
        Toast.makeText(MainActivityDB.this, incidentForm.toString(), Toast.LENGTH_LONG).show();

    }


    // Submit a form
    public void submitForm(IncidentForm incidentForm) {



    }



    // Register a user
    public void registerUser(RegistrationForm registrationForm) {

        // Check that password and confirm password match
        if (registrationForm.getPassword().equals(registrationForm.getConfirmPassword())) {

            // If user has an ECE Centre email address, register them as a Teacher
            // Otherwise register them as a Guardian (default value 0).
            if (registrationForm.getEmail().contains("@huttkindergartens.org.nz")) {
                registrationForm.setTeacher(1);
            }

            // Insert to Teacher/Guardian table according to isTeacher flag
            if (registrationForm.isTeacher() == 1) {
                teacherID = databaseHelper.insertTeacher(registrationForm);
                registrationForm.setTeacherID((int) teacherID);
            }
            else {
                guardianID = databaseHelper.insertGuardian(registrationForm);
                registrationForm.setGuardianID((int) guardianID);
            }

            // Insert to User table
            userID = databaseHelper.insertUser(registrationForm);
//            Toast.makeText(MainActivity.this, "Registered " + registrationForm.getName(), Toast.LENGTH_SHORT).show();

            // Log the user into their new account
            loginForm = new LoginForm(registrationForm.getEmail(), registrationForm.getPassword());
            logInUser(loginForm);
            Toast.makeText(MainActivityDB.this, "Logged in as " + userModel.getName(), Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(MainActivityDB.this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
        }

    }


    //    Log in a user
    public void logInUser(LoginForm loginForm) {

        // Encrypt email to match to database
        //String encEmail = encrypt(loginForm.getEmail());

        // Get user ID matching the provided email
        int id = databaseHelper.getUserIDFromEmail(loginForm.getEmail());

        // Get and decrypt password matching the provided id
        String correctPassword = databaseHelper.selectPassword(id);

        // If correct password given, generate a UserModel with all of the user's data
        if (loginForm.getPassword().equals(correctPassword)) {

            userModel = databaseHelper.selectUser(id);

//            Toast.makeText(MainActivity.this, "Logged in as " + userModel.getName(), Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(MainActivityDB.this, "Login failed", Toast.LENGTH_SHORT).show();
        }
    }


    // Update a user's details - must be logged in first
    public void changeUserDetails(UserModel userModel, ChangeUserDetailsForm changeUserDetailsForm) {
        // Check input password matches current password
        if (changeUserDetailsForm.getPassword().equals(userModel.getPassword())) {

            // If name/email/phone are blank, fill them in from User Model
            if (changeUserDetailsForm.getName().equals("")) {
                changeUserDetailsForm.setName(userModel.getName());
            }

            if (changeUserDetailsForm.getEmail().equals("")) {
                changeUserDetailsForm.setEmail(userModel.getEmail());
            }

            if (changeUserDetailsForm.getPhone().equals("")) {
                changeUserDetailsForm.setPhone(userModel.getPhone());
            }

            // Update the entered user details in the database
            databaseHelper.updateUserDetails(userModel.getUserID(),
                    changeUserDetailsForm.getName(),
                    changeUserDetailsForm.getEmail(),
                    changeUserDetailsForm.getPhone());

            // Update the user model;
            userModel.setName(changeUserDetailsForm.getName());
            userModel.setEmail(changeUserDetailsForm.getEmail());
            userModel.setPhone(changeUserDetailsForm.getPhone());

            // Toast updated user
            Toast.makeText(MainActivityDB.this, "Updated to: " + userModel.toString(), Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(MainActivityDB.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
        }
    }


    // Change a user's password - must be logged in first
    public void changePassword(UserModel userModel, ChangePasswordForm changePasswordForm) {

        // Check that entered password matches user's current password
        if (changePasswordForm.getCurrentPassword().equals(userModel.getPassword())) {

            // Check that new password matches confirm password
            if (changePasswordForm.getNewPassword().equals(changePasswordForm.getConfirmPassword())) {

                // Update the password in the database
                databaseHelper.updatePassword(userModel.getUserID(), changePasswordForm.getNewPassword());

                // Update the password in the logged in user object
                userModel.setPassword(changePasswordForm.getNewPassword());

                // toast usermodel new password to check it worked
                Toast.makeText(MainActivityDB.this, "Changed password to " + userModel.getPassword(), Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(MainActivityDB.this, "New Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(MainActivityDB.this, "Current Password is incorrect", Toast.LENGTH_SHORT).show();
        }
    }


    // Reset a user's password and recover their account
    public void resetPassword(ResetPasswordForm resetPasswordForm) {
        // Check that new password and confirm password fields match
        if (resetPasswordForm.getNewPassword().equals(resetPasswordForm.getConfirmPassword())) {

            // Get user id matching the email address
            int id = databaseHelper.getUserIDFromEmail(resetPasswordForm.getEmail());

            // Update the database with new password (encrypted)
            databaseHelper.updatePassword(id, resetPasswordForm.getNewPassword());
            Toast.makeText(MainActivityDB.this, "Changed password to " + resetPasswordForm.getNewPassword(), Toast.LENGTH_SHORT).show();

            // Log the user back in to their account
            loginForm = new LoginForm(resetPasswordForm.getEmail(), resetPasswordForm.getNewPassword());
            logInUser(loginForm);

        }
        else {
            Toast.makeText(MainActivityDB.this, "Confirm Password must match New Password", Toast.LENGTH_SHORT).show();
        }
    }



}*/