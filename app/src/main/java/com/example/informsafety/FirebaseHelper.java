package com.example.informsafety;

import static com.example.informsafety.EncryptDecrypt.*;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
        // Hello World test
//        DatabaseReference myRef = db.getReference("message");
//        myRef.setValue("Hello, World!");
//        db.getReference().child("Hello").push().child("World").setValue("test");

//        HashMap<String, Object> map = new HashMap<>();
//        map.put("Hello", true);
//        map.put("World", 1);
//        db.getReference().child("HelloWorldMap").push().updateChildren(map);

////      Test delete all
//        ref.removeValue();
//
////      Test insert a Teacher, Guardian and Child
//        insertTeacher("Teacher 1", "teacher1@huttkindergartens.org.nz", "0210727600");
//        insertTeacher("Teacher 2", "teacher2@huttkindergartens.org.nz", "0210727598");
//        insertTeacher("A Teacher", "donoterase@huttkindergartens.org.nz", "0210727598");
//        insertGuardian("Guardian 1", "imaparent@gmail.com", "0270727676");
//        insertGuardian("Guardian 2", "imaparenttoo@gmail.com", "0220727622");
//        insertUser("test123", "test name", "test email", "123", "test", false);

        // Test insert child: Get parents UID from Firebase Authentication
//        insertChild("PhVaPsq6QpZH3ykYMCgH1PT1xeS2", "Bart Simpson");
//        insertChild("PhVaPsq6QpZH3ykYMCgH1PT1xeS2", "Lisa Simpson");
//        insertChild("jfwOV5GCXNQi1q6vLobVxKyySqD3", "Ralph Wiggum");

        // Realistic child names for the manual
//        insertChild("PhVaPsq6QpZH3ykYMCgH1PT1xeS2", "Charlotte Smith");
//        insertChild("PhVaPsq6QpZH3ykYMCgH1PT1xeS2", "Isla Wilson");
//        insertChild("PhVaPsq6QpZH3ykYMCgH1PT1xeS2", "Amelia Williams");
//        insertChild("PhVaPsq6QpZH3ykYMCgH1PT1xeS2", "Olivia Brown");
//        insertChild("PhVaPsq6QpZH3ykYMCgH1PT1xeS2", "Ava Taylor");
//        insertChild("PhVaPsq6QpZH3ykYMCgH1PT1xeS2", "Oliver Jones");
//        insertChild("PhVaPsq6QpZH3ykYMCgH1PT1xeS2", "Noah Anderson");
//        insertChild("PhVaPsq6QpZH3ykYMCgH1PT1xeS2", "Jack Thompson");
//        insertChild("PhVaPsq6QpZH3ykYMCgH1PT1xeS2", "Leo Walker");
//        insertChild("PhVaPsq6QpZH3ykYMCgH1PT1xeS2", "George Lee");






        // Get key for a query
//        Query query = ref.child("Guardian").orderByChild("Name").equalTo("Parent 1");
//        String myKey_ = getKey(query);
//        System.out.println(myKey_);
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String myKey = "";
//                DataSnapshot mySnapshot = dataSnapshot.getChildren().iterator().next();
//                myKey = mySnapshot.getKey();
//                System.out.println(myKey);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e(TAG, "onCancelled", databaseError.toException());
//            }
//        });
    }

//    ----------------------------------------------------------------------------------------------

//    Get a list of all children




    // Get a key for a query
//    public String getKey(Query query) {
//
//        //final String[] myKey = new String[1];
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                DataSnapshot mySnapshot = dataSnapshot.getChildren().iterator().next();
//                myKey[0] = mySnapshot.getKey();
//
//                System.out.println(myKey[0]);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e(TAG, "onCancelled", databaseError.toException());
//            }
//        });
//
//        return myKey[0];
//    }



    // Insert a User
//    public void insertUser(UserModel userModel) {
//        ref.child("User").push().updateChildren(userModel.toHashMap());
//    }

    // Check whether the user is a Teacher based on their email address
    public boolean isTeacherEmail(String email) {
        List<String> teacherDomains = new ArrayList<>();
        teacherDomains.add("@huttkindergartens.org.nz");
        //String teacherDomains = "@huttkindergartens.org.nz";
        //return teacherDomains.contains(email);

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
//        map.put("UID", UID);
//        String encName = encrypt(name);
//        String plainName = decrypt(encName);
        map.put("Name", encrypt(name));
        map.put("Email", encrypt(email));
        map.put("Phone", encrypt(phone));
        map.put("Password", encrypt(password));
        map.put("isTeacher", isTeacher);
//        ref.child("User").push().updateChildren(map);
        ref.child("User").child(UID).setValue(map);
    }

//    // Insert a Guardian
//    public void insertGuardian(String UID, String name, String email, String phone, String password) {
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("UID", UID);
//        map.put("Name", encrypt(name));
//        map.put("Email", encrypt(email));
//        map.put("Phone", encrypt(phone));
//        map.put("Password", encrypt(password));
//        ref.child("Guardian").push().updateChildren(map);
//    }

    // Insert a Child with a reference to the ID of their Parent
    public void insertChild(String parentKey, String name) {

    // Create a hashmap for Child data including Parent's key
    HashMap<String, Object> map = new HashMap<>();
    map.put("ParentKey", parentKey);
    map.put("Name", encrypt(name));
    ref.child("Child").push().updateChildren(map);

        // Query to get the parent given their name
//        String encParentName = encrypt(parentName);
//        Query parentQuery = ref.child("User").orderByChild("Name").equalTo(encParentName);
//        parentQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                // Get the key for the Parent
//                DataSnapshot mySnapshot = dataSnapshot.getChildren().iterator().next();
//                String parentKey = mySnapshot.getKey();
//                System.out.println(parentKey);
//
//                // Create a hashmap for Child data including Parent's key
//                HashMap<String, Object> map = new HashMap<>();
//                map.put("ParentKey", parentKey);
//                map.put("Name", encrypt(name));
//                ref.child("Child").push().updateChildren(map);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e(TAG, "onCancelled", databaseError.toException());
//            }
//        });
    }

    // Insert an Incident Form
//    public void insertForm(IncidentForm incidentForm) {
//        ref.child("Minor Incident").push().updateChildren(incidentForm.toHashMap());
//    }





}
