package com.example.informsafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// Test page for developing Minor Incident Form
public class TestActivity extends AppCompatActivity {

    FirebaseHelper fbh = new FirebaseHelper();
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://informsafetydb-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference ref = db.getReference();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        // Dropdown list for incident locations
        List<String> locationList = new ArrayList<>();
        locationList.add("Swings");
        locationList.add("Sandpit");
        locationList.add("Challenge course");
        locationList.add("Fixed equipment");
        locationList.add("Pathway/tracks");
        locationList.add("Grass/safety surface");
        locationList.add("Water trough");
        locationList.add("Shed/storage area");
        locationList.add("Ride-on vehicles");
        locationList.add("Carpentry area");
        locationList.add("Indoor play area");
        locationList.add("Bathroom/Toilets");
        locationList.add("Kitchen");
        locationList.add("Entry areas");
        locationList.add("Children in conflict");
        locationList.add("Natural play");
        locationList.add("Excursion");
        locationList.add("Unwell/ill");
        locationList.add("Other");

        // Dropdown list for incident treatments
        List<String> treatmentList = new ArrayList<>();
        treatmentList.add("Plaster/s");
        treatmentList.add("Icepack/cold flannel");
        treatmentList.add("Wiped clean");
        treatmentList.add("Cuddle/TLC");
        treatmentList.add("Monitored");
        treatmentList.add("Cleaned/dressed");
        treatmentList.add("Rest");
        treatmentList.add("Sling/splint");
        treatmentList.add("Elevation of limb");
        treatmentList.add("Isolated from children");
        treatmentList.add("Medication given");
        treatmentList.add("Other");

        // Dropdown list for injury types
        List<String> injuryList = new ArrayList<>();
        injuryList.add("Bruising");
        injuryList.add("Insect bite/sting");
        injuryList.add("Laceration/cut");
        injuryList.add("Nose bleed");
        injuryList.add("Choking");
        injuryList.add("Foreign body");
        injuryList.add("Dental/Mouth");
        injuryList.add("Strain/sprain");
        injuryList.add("Concussion/shock");
        injuryList.add("Allergic reaction");
        injuryList.add("Dislocation/Fracture");
        injuryList.add("Burn/scald");
        injuryList.add("Internal");
        injuryList.add("Chemical reaction");
        injuryList.add("Human bite");
        injuryList.add("Other");

        // Dropdown list for incident likelihood
        List<String> likelihoodList = new ArrayList<>();
        likelihoodList.add("Very likely");
        likelihoodList.add("Likely");
        likelihoodList.add("Rare");










        // Dropdown for Child
        Spinner childDropDown = findViewById(R.id.childSpinner);
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
        Spinner teacherDropDown = findViewById(R.id.teacherSpinner);
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


        // Dropdown for Location
        Spinner locationDropDown = findViewById(R.id.locationSpinner);
        ArrayAdapter locationAdapter = new ArrayAdapter<String>(this, R.layout.list_item, locationList);
        locationDropDown.setAdapter(locationAdapter);

        // Dropdown for Treatment
        Spinner treatmentDropDown = findViewById(R.id.treatmentSpinner);
        ArrayAdapter treatmentAdapter = new ArrayAdapter<String>(this, R.layout.list_item, treatmentList);
        treatmentDropDown.setAdapter(treatmentAdapter);


        // Create a Minor Incident Form
        // Run when user clicks "save as draft" and validation passes

        // User UID from Authentication
        String mUserID = "user001";

        // Data from user input
        String mChildName = "Jack Jack";
        String mDate = "02-09-2022";
        String mTime = "08:46";
        String mDescription = "Grazed his knee";
        String mLocation = "Grass/safety surface";
        String mTreatment = "Plaster/s";
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
//        ref.child("Minor Incident").push().updateChildren(map);




        // Display Form contents as a List View
        // This example uses the most recent form. TODO: Create a list of forms and select from it
        ListView formListView = findViewById(R.id.formListView);
        ArrayList<String> formList = new ArrayList<>();
        ArrayAdapter formAdapter = new ArrayAdapter<String>(this, R.layout.list_item, formList);
        formListView.setAdapter(formAdapter);
        DatabaseReference formRef = ref.child("Incident");
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



        // Display a list of all forms in a ListView
        ListView draftsListView = findViewById(R.id.draftsListView);
        ArrayList<String> draftsList = new ArrayList<>();
        ArrayList<String> draftsKeyList = new ArrayList<>();
        ArrayAdapter draftsAdapter = new ArrayAdapter<String>(this, R.layout.list_item, draftsList);
        draftsListView.setAdapter(draftsAdapter);
        DatabaseReference draftsRef = ref.child("Incident");
        Query draftsQuery = draftsRef.orderByKey();

        draftsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                draftsList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    // Get each draft form in order and do something with it
                    String qKey = snapshot.getKey();
                    String qChildName = snapshot.child("childName").getValue().toString();
                    String qIncidentDate = snapshot.child("incidentDate").getValue().toString();
                    String qIncidentTime = snapshot.child("incidentTime").getValue().toString();

                    // Add selected form data into one field in the ListView
                    draftsList.add(qChildName + ", " + qIncidentDate + ", " + qIncidentTime);
                    draftsKeyList.add(qKey);

                }

                // Reverse the list to get the latest incident at the top
                Collections.reverse(draftsList);
                Collections.reverse(draftsKeyList);
                draftsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


        // Click a form on the drafts list and do something with it
        draftsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> draftsListView, View view, int position, long id) {
                String myKey = draftsKeyList.get(position);
//                Toast.makeText(TestActivity.this,"id" + id + ", position" + position + ", view" + view.toString(),Toast.LENGTH_SHORT).show();
//                Toast.makeText(TestActivity.this,"You selected : " + item,Toast.LENGTH_SHORT).show();
//                Toast.makeText(TestActivity.this, myKey ,Toast.LENGTH_SHORT).show();

                // Query the database for the clicked record
                Query myDraftQuery = draftsRef.orderByKey().equalTo(myKey);
                myDraftQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            // Do something with the selected draft form
                            Toast.makeText(TestActivity.this, snapshot.toString() ,Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });

            }
        });


    }
}