package com.example.informsafety;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class FirebaseHelper {

    public FirebaseDatabase db;
    public DatabaseReference ref;
    public Query query;

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

        // Test delete all
        ref.removeValue();

        // Test insert a Teacher, Guardian and Child
        insertTeacher("Teacher 1", "0210727601", "teacher1@huttkindergartens.org.nz");
        insertGuardian("Parent 1", "0220726601", "imaparent@gmail.com");
//        insertChild("Parent 1", "Robert", "Bobby Tables");


        // Get key for a query
//        Query query = ref.child("Guardian").orderByChild("Name").equalTo("Parent 1");
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
//                    appleSnapshot.getRef().removeValue();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e(TAG, "onCancelled", databaseError.toException());
//            }
//        });

    }


    // Insert a User
    public void insertUser(UserModel userModel) {
        ref.child("User").push().updateChildren(userModel.toHashMap());
    }

    // Insert a Teacher
    public void insertTeacher(String name, String phone, String email) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("Name", name);
        map.put("Phone", phone);
        map.put("Email", email);
        ref.child("Teacher").push().updateChildren(map);
    }

    // Insert a Guardian
    public void insertGuardian(String name, String phone, String email) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("Name", name);
        map.put("Phone", phone);
        map.put("Email", email);
        ref.child("Guardian").push().updateChildren(map);
    }

    // Insert a Child
//    public void insertChild(String guardianName, String name, String nickname) {
//        HashMap<String, Object> map = new HashMap<>();
//        //map.put("GuardianID", guardianName);
//        map.put("Name", name);
//        map.put("Nickname", nickname);
//        db.getReference().child("Guardian").push().updateChildren(map);
//    }



}
