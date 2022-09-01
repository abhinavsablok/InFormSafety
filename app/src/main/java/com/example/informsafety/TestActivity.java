package com.example.informsafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

// Test page for developing Minor Incident Form
public class TestActivity extends AppCompatActivity {

    FirebaseHelper fbh = new FirebaseHelper();
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://informsafetydb-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference ref = db.getReference();
    private Spinner childDropDown;
    private Spinner teacherDropDown;
    private ListView formListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Dropdown for Child
        childDropDown = findViewById(R.id.childSpinner);
        ArrayList<String> childList = new ArrayList<>();
        ArrayAdapter childAdapter = new ArrayAdapter<String>(this, R.layout.list_item, childList);
        childDropDown.setAdapter(childAdapter);
        DatabaseReference childRef = ref.child("Child");

        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                childList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    childList.add(snapshot.child("Nickname").getValue().toString());
                }
                childAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        // Dropdown for Teacher
        teacherDropDown = findViewById(R.id.teacherSpinner);
        ArrayList<String> teacherList = new ArrayList<>();
        ArrayAdapter teacherAdapter = new ArrayAdapter<String>(this, R.layout.list_item, teacherList);
        teacherDropDown.setAdapter(teacherAdapter);
        DatabaseReference teacherRef = ref.child("Teacher");

        teacherRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teacherList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    teacherList.add(snapshot.child("Name").getValue().toString());
                }
                teacherAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        // Create a Minor Incident Form
        // Run when user clicks "save as draft" and validation passes

        // User UID from Authentication
        String mUserID = "user001";

        // Data from user input
        String mChildName = "Bobby Tables";
        String mDate = "01-09-2022";
        String mTime = "12:53";
        String mDescription = "Fell off the swing";
        String mLocation = "Swings";
        String mTreatment = "Icepack/cold flannel";
        String mTeacherProvided = "Teacher 1";
        String mTeacherChecked = "Teacher 2";
        String mComments = "";

        // Data for form status
        boolean mSentToGuardian = false;
        boolean mSignedByGuardian = false;
        String mPdfFilename = "";


        // Insert a Form
//        IncidentForm incidentForm = new IncidentForm(mUserID, mChildName, mDate, mTime,
//                mDescription, mLocation, mTreatment, mTeacher1, mTeacher2, mComments);
//        fbh.insertForm(incidentForm);

        // Create a HashMap of incident form contents
        HashMap<String, Object> map = new HashMap<>();
        map.put("userID", mUserID);
        map.put("childName", mChildName);
        map.put("incidentDate", mDate);
        map.put("incidentTime", mTime);
        map.put("description", mDescription);
        map.put("location", mLocation);
        map.put("treatment", mTreatment);
        map.put("teacherProvided", mTeacherProvided);
        map.put("teacherChecked", mTeacherChecked);
        map.put("comments", mComments);
        map.put("sentToGuardian", mSentToGuardian);
        map.put("signedByGuardian", mSignedByGuardian);
        map.put("pdfFilename", mPdfFilename);

        // Insert to Realtime Database
        ref.child("Minor Incident").push().updateChildren(map);




        // Display Form contents as a List View
        // This example uses the most recent form. TODO: Create a list of forms and select from it
        formListView = findViewById(R.id.formListView);
        ArrayList<String> formList = new ArrayList<>();
        ArrayAdapter formAdapter = new ArrayAdapter<String>(this, R.layout.list_item, formList);
        formListView.setAdapter(formAdapter);
        DatabaseReference formRef = ref.child("Minor Incident");
        Query query = formRef.limitToLast(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                formList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    // Get each form item in order and do something with it
                    String qUserID = snapshot.child("userID").getValue().toString();
                    String qChildName = snapshot.child("childName").getValue().toString();
                    String qIncidentDate = snapshot.child("incidentDate").getValue().toString();
                    String qIncidentTime = snapshot.child("incidentTime").getValue().toString();
                    String qDescription = snapshot.child("description").getValue().toString();
                    String qLocation = snapshot.child("location").getValue().toString();
                    String qTreatment = snapshot.child("treatment").getValue().toString();
                    String qTeacherProvided = snapshot.child("teacherProvided").getValue().toString();
                    String qTeacherChecked = snapshot.child("teacherChecked").getValue().toString();
                    String qComments = snapshot.child("comments").getValue().toString();
                    boolean qSentToGuardian = (boolean) snapshot.child("sentToGuardian").getValue();
                    boolean qSignedByGuardian = (boolean) snapshot.child("signedByGuardian").getValue();
                    String qPdfFilename = snapshot.child("pdfFilename").getValue().toString();


                    // Test: Dump all fields to a ListView
                    formList.add("User ID: " + qUserID);
                    formList.add("Child: " + qChildName);
                    formList.add("Date: " + qIncidentDate);
                    formList.add("Time: " + qIncidentTime);
                    formList.add("Description: " + qDescription);
                    formList.add("Location: " + qLocation);
                    formList.add("Treatment: " + qTreatment);
                    formList.add("Provided by: " + qTeacherProvided);
                    formList.add("Checked by: " + qTeacherChecked);
                    formList.add("Comments: " + qComments);
                    formList.add("Sent to guardian: " + String.valueOf(qSentToGuardian));
                    formList.add("Signed by guardian: " + String.valueOf(qSignedByGuardian));
                    formList.add("PDF Filename: " + qPdfFilename);
                }
                formAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


    }
}