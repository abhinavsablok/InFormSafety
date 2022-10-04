package com.example.informsafety;

import static com.example.informsafety.EncryptDecrypt.encrypt;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class FirebaseHelper {

    public FirebaseDatabase db;
    public DatabaseReference ref;
    public Query query;
    private static final String TAG = "FirebaseHelper";

    public FirebaseHelper() {

        db = FirebaseDatabase.getInstance("https://informsafetydb-default-rtdb.asia-southeast1.firebasedatabase.app/");
        ref = db.getReference();
    }

    // Check whether the user is a Teacher based on their email address
    public boolean isTeacherEmail(String email) {
        List<String> teacherDomains = new ArrayList<>();
        teacherDomains.add("@huttkindergartens.org.nz");

        for (String domain : teacherDomains){
            if (email.contains(domain)) {
                return true;
            }
        }
        return false;

    }

    // Insert a User
    public void insertUser(String UID, String name, String email, String phone, String password, boolean isTeacher) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("Name", encrypt(name));
        map.put("Email", encrypt(email));
        map.put("Phone", encrypt(phone));
        map.put("Password", encrypt(password));
        map.put("isTeacher", isTeacher);
        ref.child("User").child(UID).setValue(map);
    }


    // Insert a Child with a reference to the ID of their Parent
    public void insertChild(String parentKey, String name) {
        // Create a hashmap for Child data including Parent's key
        HashMap<String, Object> map = new HashMap<>();
        map.put("ParentKey", parentKey);
        map.put("Name", encrypt(name));
        ref.child("Child").push().updateChildren(map);
    }
}
