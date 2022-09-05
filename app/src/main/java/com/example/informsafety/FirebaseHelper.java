package com.example.informsafety;

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
//        insertGuardian("Parent 1", "imaparent@gmail.com", "0270727676");
//        insertGuardian("Parent 2", "imaparenttoo@gmail.com", "0220727622");
//        insertChild("Parent 1", "Robert", "Bobby Tables");
//        insertChild("Parent 1", "Timothy", "Little Timmy");
//        insertChild("Parent 2", "Jackson", "Jack Jack");








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


    // Insert a Teacher
    public void insertTeacher(String UID, String name, String email, String phone) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("UID", UID);
        map.put("Name", name);
        map.put("Email", email);
        map.put("Phone", phone);
        ref.child("Teacher").push().updateChildren(map);
    }

    // Insert a Guardian
    public void insertGuardian(String UID, String name, String email, String phone) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("UID", UID);
        map.put("Name", name);
        map.put("Email", email);
        map.put("Phone", phone);
        ref.child("Guardian").push().updateChildren(map);
    }

    // Insert a Child with a reference to the ID of their Parent
    public void insertChild(String parentName, String name, String nickname) {

        // Query to get the parent given their name
        Query parentQuery = ref.child("Guardian").orderByChild("Name").equalTo(parentName);
        parentQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Get the key for the Parent
                DataSnapshot mySnapshot = dataSnapshot.getChildren().iterator().next();
                String parentKey = mySnapshot.getKey();
                System.out.println(parentKey);

                // Create a hashmap for Child data including Parent's key
                HashMap<String, Object> map = new HashMap<>();
                map.put("ParentKey", parentKey);
                map.put("Name", name);
                map.put("Nickname", nickname);
                ref.child("Child").push().updateChildren(map);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    // Insert an Incident Form
    public void insertForm(IncidentForm incidentForm) {
        ref.child("Minor Incident").push().updateChildren(incidentForm.toHashMap());
    }


}
