/*package com.example.informsafety;

import static com.example.informsafety.EncryptDecrypt.decrypt;
import static com.example.informsafety.EncryptDecrypt.encrypt;
import static com.example.informsafety.EncryptDecrypt.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, "user.db", null, 1);
    }

    // first time DB accessed
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create empty tables
        // x is a placeholder to allow inserting rows with no values

        String createTeacherTable = "CREATE TABLE IF NOT EXISTS Teacher (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "x INT" +
                ")";

        String createGuardianTable = "CREATE TABLE IF NOT EXISTS Guardian (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "x INT" +
                ")";

        String createUserTable = "CREATE TABLE IF NOT EXISTS User (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "teacher_id INTEGER REFERENCES Teacher (id), " +
                "guardian_id INTEGER REFERENCES Guardian (id), " +
                "is_teacher INT, " +
                "name TEXT, " +
                "email TEXT UNIQUE, " +
                "phone TEXT," +
                "password TEXT" +
                ")";

        String createChildTable = "CREATE TABLE IF NOT EXISTS Child (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "guardian_id INTEGER REFERENCES Guardian (id), " +
                "name TEXT," +
                "nickname TEXT UNIQUE" +
                ")";

        String createIncidentFormTable = "CREATE TABLE IF NOT EXISTS Incident_Form (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "teacher_created_id INTEGER REFERENCES Teacher (id), " +
                "child_id INTEGER REFERENCES Child (id), " +
                "form_type_id INT, " +
                "incident_date TEXT, " +
                "incident_time TEXT, " +
                "description TEXT, " +
                "other_notes TEXT, " +
                "teacher_provided_id INTEGER REFERENCES Teacher (id), " +
                "teacher_checked_id INTEGER REFERENCES Teacher (id), " +
                "sent_to_guardian INT, " +
                "signed_by_guardian INT, " +
                "pdf_filename TEXT" +
                ")";

        db.execSQL(createTeacherTable);
        db.execSQL(createGuardianTable);
        db.execSQL(createUserTable);
        db.execSQL(createChildTable);
        db.execSQL(createIncidentFormTable);
    }

    // When DB version changed
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }


    // Insert user based on Registration form
    public long insertUser(RegistrationForm registrationForm) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("teacher_id", registrationForm.getTeacherID());
        cv.put("guardian_id", registrationForm.getGuardianID());
        cv.put("is_teacher", registrationForm.isTeacher());
        cv.put("name", encrypt(registrationForm.getName()));
        cv.put("email", encrypt(registrationForm.getEmail()));
        cv.put("phone", encrypt(registrationForm.getPhone()));
        cv.put("password", encrypt(registrationForm.getPassword()));
        long userID = db.insert("User", null, cv);

        // Return autogenerated User ID for reference
        return userID;
    }

    // Insert teacher based on Registration form
    public long insertTeacher(RegistrationForm registrationForm) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        long teacher_ID = db.insert("Teacher", "x", cv);

        // Return autogenerated Teacher ID for reference
        return teacher_ID;
    }

    // Insert guardian based on Registration form
    public long insertGuardian(RegistrationForm registrationForm) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        long guardian_ID = db.insert("Guardian", "x", cv);

        // Return autogenerated Guardian ID for reference
        return guardian_ID;
    }


    // Delete user based on User Model of logged in user
    public void deleteUser(UserModel userModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM User WHERE id = " + userModel.getUserID());

    }

    // Delete teacher based on User Model of logged in user
    public void deleteTeacher(UserModel userModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Teacher WHERE id = " + userModel.getTeacherID());

    }

    // Delete guardian based on User Model of logged in user
    public void deleteGuardian(UserModel userModel) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Guardian WHERE id = " + userModel.getGuardianID());

    }


    // Get user ID from email
    public int getUserIDFromEmail(String inputEmail) {

        SQLiteDatabase db = this.getReadableDatabase();
        // Encrypt provided email to match with stored encrypted email
        String encryptedEmail = encrypt(inputEmail);
        String queryString = "SELECT id from User where email = '" + encryptedEmail + "'";
        int id = 0;

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            // Loop through result set, create new User objects, put into return list
            do {
                id = cursor.getInt(0);
            }
            while (cursor.moveToNext());
        }

        // clean up
        cursor.close();
        db.close();

        return id;
    }


    // Select user password based on user ID
    public String selectPassword(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT password from User where id = " + id;
        String correctPassword = "";

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            // Get and decrypt password from return dataset
            do {
                correctPassword = decrypt(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }

        // clean up
        cursor.close();
        db.close();

        return correctPassword;
    }


    // Update password based on user id
    public void updatePassword(int id, String newPassword) {

        // Encrypt details
        newPassword = encrypt(newPassword);

        // Insert into User table
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE User SET password = '" + newPassword + "' where id = " + id;
        db.execSQL(queryString);

    }


    // Update Name, Email, Phone based on ID
    public void updateUserDetails(int id, String newName, String newEmail, String newPhone) {

        // Encrypt details
        newName = encrypt(newName);
        newEmail = encrypt(newEmail);
        newPhone = encrypt(newPhone);

        // Insert into User table
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "UPDATE User SET name = '" + newName +
                "', email = '" + newEmail +
                "', phone = '" + newPhone +
                "' where id = " + id;
        db.execSQL(queryString);

    }



    // Get all info for a user based on user ID
    public UserModel selectUser(int id) {

        UserModel userModel = new UserModel();

        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT id, teacher_id, guardian_id, is_teacher, name, email, phone, password from User where id = " + id;

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            // Loop through result set, create new User objects, put into return list
            do {
                int userID = cursor.getInt(0);
                int teacherID = cursor.getInt(1);
                int guardianID = cursor.getInt(2);

                // Convert is_teacher from int to bool
                boolean isTeacher = cursor.getInt(3) != 0;

                // Decrypt personal info
                String name = decrypt(cursor.getString(4));
                String email = decrypt(cursor.getString(5));
                String phone = decrypt(cursor.getString(6));
                String password = decrypt(cursor.getString(7));

                userModel = new UserModel(userID, teacherID, guardianID, isTeacher, name, email, phone, password);

            }
            while(cursor.moveToNext());

        }
        else {
            // do nothing
        }

        // clean up
        cursor.close();
        db.close();

        return userModel;
    }


    // Delete all records from tables
    // FOR TESTING ONLY!
    public void deleteRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Teacher");
        db.execSQL("DELETE FROM Guardian");
        db.execSQL("DELETE FROM User");
        db.execSQL("DELETE FROM Child");
        db.execSQL("DELETE FROM Incident_Form");

    }



    // Insert Child
    public long insertChild(int guardian_ID, String name, String nickname) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("guardian_id", guardian_ID);
        cv.put("name", encrypt(name));
        cv.put("nickname", encrypt(nickname));

        long child_ID = db.insert("Child", null, cv);

        // Return autogenerated Guardian ID for reference
        return child_ID;
    }


    // Get all info for a child based on Child ID
    public ChildModel selectChild(int id) {

        ChildModel childModel = new ChildModel();

        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT id, guardian_ID, name, nickname from Child where id = " + id;

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            // Loop through result set, create new User objects, put into return list
            do {
                int childID = cursor.getInt(0);
                int guardianID = cursor.getInt(1);
                String name = decrypt(cursor.getString(2));
                String nickname = decrypt(cursor.getString(3));

                childModel = new ChildModel(childID,  guardianID, name, nickname);

            }
            while(cursor.moveToNext());

        }
        else {
            // do nothing
        }

        // clean up
        cursor.close();
        db.close();

        return childModel;
    }


    // Get child ID from nickname
    public int getChildIDFromNickname(String nickname) {

        SQLiteDatabase db = this.getReadableDatabase();
        // Encrypt provided nickname to match with stored encrypted nickname
        String encryptedNickname = encrypt(nickname);
        String queryString = "SELECT id from Child where nickname = '" + encryptedNickname + "'";
        int id = 0;

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            // Loop through result set, create new User objects, put into return list
            do {
                id = cursor.getInt(0);
            }
            while (cursor.moveToNext());
        }

        // clean up
        cursor.close();
        db.close();

        return id;
    }


    // Insert Form record based on Incident form
    public long insertForm(IncidentForm incidentForm) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("teacher_created_id", incidentForm.getTeacherCreatedID());
        cv.put("child_id", incidentForm.getChildID());
        cv.put("form_type_id", incidentForm.getFormType());
        cv.put("incident_date", incidentForm.getIncidentDate());
        cv.put("incident_time", incidentForm.getIncidentTime());
        cv.put("description", incidentForm.getDescription());
        cv.put("other_notes", incidentForm.getOtherNotes());
        cv.put("teacher_provided_id", incidentForm.getTeacherProvidedID());
        cv.put("teacher_checked_id", incidentForm.getTeacherCheckedID());
        cv.put("sent_to_guardian", incidentForm.getSentToGuardian());
        cv.put("signed_by_guardian", incidentForm.getSignedByGuardian());
        cv.put("pdf_filename", incidentForm.getPdfFilename());
        long formID = db.insert("Incident_Form", null, cv);

        // Return autogenerated User ID for reference
        return formID;
    }
}*/