package com.example.informsafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MinorFormActivity extends AppCompatActivity {

    Spinner child, teacherProvided, teacherChecked, location, treatment;
    EditText date, time, description, comments;
    Button save, send;
    FirebaseAuth mAuth;
    FirebaseHelper fbh;
    FirebaseDatabase db;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minor_form);

        fbh = new FirebaseHelper();
        db = FirebaseDatabase.getInstance("https://informsafetydb-default-rtdb.asia-southeast1.firebasedatabase.app/");
        ref = db.getReference();
        mAuth = FirebaseAuth.getInstance();

        // Get references for form elements
        child = findViewById(R.id.child);
        teacherProvided = findViewById(R.id.teacherProvided);
        teacherChecked = findViewById(R.id.teacherChecked);
        location = findViewById(R.id.location);
        treatment = findViewById(R.id.treatment);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        description = findViewById(R.id.description);
        comments = findViewById(R.id.comments);
        save = findViewById(R.id.save);
        send = findViewById(R.id.send);

        ClickSave();


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



        // Dropdown for Child
//        Spinner childDropDown = findViewById(R.id.childSpinner);
        ArrayList<String> childList = new ArrayList<>();
        ArrayAdapter childAdapter = new ArrayAdapter<String>(this, R.layout.list_item, childList);
        child.setAdapter(childAdapter);
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


        // Dropdown for Location
//        Spinner locationDropDown = findViewById(R.id.locationSpinner);
        ArrayAdapter locationAdapter = new ArrayAdapter<String>(this, R.layout.list_item, locationList);
        location.setAdapter(locationAdapter);

        // Dropdown for Treatment
//        Spinner treatmentDropDown = findViewById(R.id.treatmentSpinner);
        ArrayAdapter treatmentAdapter = new ArrayAdapter<String>(this, R.layout.list_item, treatmentList);
        treatment.setAdapter(treatmentAdapter);


        // Dropdowns for Teachers
//        Spinner teacherDropDown1 = findViewById(R.id.teacherSpinner1);
//        Spinner teacherDropDown2 = findViewById(R.id.teacherSpinner2);
        ArrayList<String> teacherList = new ArrayList<>();
        ArrayAdapter teacherAdapter = new ArrayAdapter<String>(this, R.layout.list_item, teacherList);
        teacherProvided.setAdapter(teacherAdapter);
        teacherChecked.setAdapter(teacherAdapter);
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


        // If user opened a saved form, populate the form with the saved values
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String myKey = extras.getString("Key");
//            Toast.makeText(MinorFormActivity.this, myKey, Toast.LENGTH_SHORT).show();

            // Query the database for the clicked record
            DatabaseReference draftsRef = ref.child("Incident");
            Query myDraftQuery = draftsRef.orderByKey().equalTo(myKey);
            myDraftQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        // Do something with the selected draft form
//                        Toast.makeText(MinorFormActivity.this, snapshot.toString() ,Toast.LENGTH_SHORT).show();

                        // Set form elements to show the saved values
                        String qChildName = snapshot.child("childName").getValue().toString();
                        if (qChildName != null) {
                            int spinnerPosition = childAdapter.getPosition(qChildName);
                            child.setSelection(spinnerPosition);
                        }

                        date.setText(snapshot.child("incidentDate").getValue().toString());
                        time.setText(snapshot.child("incidentTime").getValue().toString());
                        description.setText(snapshot.child("description").getValue().toString());

                        String qLocation = snapshot.child("location").getValue().toString();
                        if (qLocation != null) {
                            int spinnerPosition = locationAdapter.getPosition(qLocation);
                            location.setSelection(spinnerPosition);
                        }

                        String qTreatment = snapshot.child("treatment").getValue().toString();
                        if (qTreatment != null) {
                            int spinnerPosition = treatmentAdapter.getPosition(qTreatment);
                            treatment.setSelection(spinnerPosition);
                        }

                        String qTeacherProvided = snapshot.child("teacherProvided").getValue().toString();
                        if (qTeacherProvided != null) {
                            int spinnerPosition = teacherAdapter.getPosition(qTeacherProvided);
                            teacherProvided.setSelection(spinnerPosition);
                        }

                        String qTeacherChecked = snapshot.child("teacherChecked").getValue().toString();
                        if (qTeacherChecked != null) {
                            int spinnerPosition = teacherAdapter.getPosition(qTeacherChecked);
                            teacherChecked.setSelection(spinnerPosition);
                        }

                        comments.setText(snapshot.child("comments").getValue().toString());


                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });


        }

    }


    // When user clicks Save, add the entered information into Firebase Realtime Database
    private void ClickSave() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get UID of logged in user
                String myUID = mAuth.getCurrentUser().getUid();

                // Get text from form elements
                String myChild = child.getSelectedItem().toString();
                String myDate = date.getText().toString();
                String myTime = time.getText().toString();
                String myDescription = description.getText().toString();
                String myLocation = location.getSelectedItem().toString();
                String myTreatment = treatment.getSelectedItem().toString();
                String myTeacherProvided = teacherProvided.getSelectedItem().toString();
                String myTeacherChecked = teacherChecked.getSelectedItem().toString();
                String myComments = comments.getText().toString();

                // Additional data for form status
                String formType = "Minor Incident";
                boolean mSentToGuardian = false;
                boolean mSignedByGuardian = false;
                String mPdfFilename = "";

                // Create a HashMap of incident form contents
                HashMap<String, Object> map = new HashMap<>();
                map.put("userID", myUID);
                map.put("childName", myChild);
                map.put("incidentDate", myDate);
                map.put("incidentTime", myTime);
                map.put("description", myDescription);
                map.put("location", myLocation);
                map.put("treatment", myTreatment);
                map.put("teacherProvided", myTeacherProvided);
                map.put("teacherChecked", myTeacherChecked);
                map.put("comments", myComments);
                map.put("Form Type", formType);
                map.put("sentToGuardian", mSentToGuardian);
                map.put("signedByGuardian", mSignedByGuardian);
                map.put("pdfFilename", mPdfFilename);

                // Insert to Realtime Database
                ref.child("Incident").push().updateChildren(map);

                Toast.makeText(MinorFormActivity.this, "Saved", Toast.LENGTH_SHORT).show();

            }
        });
    }

}