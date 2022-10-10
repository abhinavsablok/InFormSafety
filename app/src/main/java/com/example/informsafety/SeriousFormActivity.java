package com.example.informsafety;

import static com.example.informsafety.EncryptDecrypt.decrypt;
import static com.example.informsafety.EncryptDecrypt.encrypt;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class SeriousFormActivity extends AppCompatActivity {

    AutoCompleteTextView child;
    Spinner injury, location, treatment, ambulanceDoctorCalled, likelihood,
            teacherActionsRequiredBy, seniorTeacherInvestigationRequired, worksafeMoeAdvised,
            adviseRph, followUpWithGuardian, teacherProvided, teacherChecked;
    EditText incidentTime, date, description, ambulanceDoctorCalledTime, guardianContactedTime,
            guardianArrivedTime, actionsRequired, dateActionsRequired, comments;
    Button save, send;
    FirebaseAuth mAuth;
    FirebaseHelper fbh;
    FirebaseDatabase db;
    DatabaseReference ref;
    String myKey;
    int timeHour, timeMinute;
    ArrayList<String> childList;
    Bitmap bmp, scaledBmp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Action bar with page title and back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Serious Incident Form");
        actionBar.setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_serious_form);


        fbh = new FirebaseHelper();
        db = FirebaseDatabase.getInstance("https://informsafetydb-default-rtdb.asia-southeast1.firebasedatabase.app/");
        ref = db.getReference();
        mAuth = FirebaseAuth.getInstance();

        // Get references for form elements
        child = findViewById(R.id.child);
        date = findViewById(R.id.date);
        incidentTime = findViewById(R.id.time);
        description = findViewById(R.id.description);
        injury = findViewById(R.id.injury);
        location = findViewById(R.id.location);
        treatment = findViewById(R.id.treatment);
        ambulanceDoctorCalled = findViewById(R.id.ambulanceDoctorCalled);
        ambulanceDoctorCalledTime = findViewById(R.id.ambulanceDoctorCalledTime);
        guardianContactedTime = findViewById(R.id.guardianContactedTime);
        guardianArrivedTime = findViewById(R.id.guardianArrivedTime);
        likelihood = findViewById(R.id.likelihood);
        actionsRequired = findViewById(R.id.actionsRequired);
        teacherActionsRequiredBy = findViewById(R.id.teacherActionsRequiredBy);
        dateActionsRequired = findViewById(R.id.dateActionsRequired);
        seniorTeacherInvestigationRequired = findViewById(R.id.seniorTeacherInvestigationRequired);
        worksafeMoeAdvised = findViewById(R.id.worksafeMoeAdvised);
        adviseRph = findViewById(R.id.adviseRph);
        followUpWithGuardian = findViewById(R.id.followUpWithGuardian);
        teacherProvided = findViewById(R.id.teacherProvided);
        teacherChecked = findViewById(R.id.teacherChecked);
        comments = findViewById(R.id.comments);
        save = findViewById(R.id.save);
        send = findViewById(R.id.send);
        ClickSave();
        ClickSend();

        Calendar calender = Calendar.getInstance();

        final int year = calender.get(calender.YEAR);
        final int month = calender.get(calender.MONTH);
        final int day = calender.get(calender.DAY_OF_MONTH);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        SeriousFormActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String selectedDate = day+"/"+month+"/"+year;
                        date.setText(selectedDate);
                        // Clear the error on this field if there was one
                        date.setError(null);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        dateActionsRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        SeriousFormActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String selectedDate = day+"/"+month+"/"+year;
                        dateActionsRequired.setText(selectedDate);
                        // Clear the error on this field if there was one
                        dateActionsRequired.setError(null);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        // If action required is removed, clear validation error on date required
        actionsRequired.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String txt = actionsRequired.getText().toString();
                if(txt.isEmpty() ) {
                    dateActionsRequired.setError(null);
                }
            }
        });

        incidentTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        SeriousFormActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeHour = hourOfDay;
                        timeMinute = minute;

                        String time = timeHour+":"+timeMinute;

                        SimpleDateFormat f24Hours = new SimpleDateFormat(
                                "HH:mm"
                        );
                        try {
                            Date date = f24Hours.parse(time);

                            SimpleDateFormat f12Hours = new SimpleDateFormat(
                                    "hh:mm aa"
                            );
                            incidentTime.setText(f12Hours.format(date));
                            // Clear the error on this field if there was one
                            incidentTime.setError(null);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, 12, 0, false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(timeHour, timeMinute);
                timePickerDialog.show();
            }
        });

        guardianContactedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        SeriousFormActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeHour = hourOfDay;
                        timeMinute = minute;

                        String time = timeHour+":"+timeMinute;

                        SimpleDateFormat f24Hours = new SimpleDateFormat(
                                "HH:mm"
                        );
                        try {
                            Date date = f24Hours.parse(time);

                            SimpleDateFormat f12Hours = new SimpleDateFormat(
                                    "hh:mm aa"
                            );
                            guardianContactedTime.setText(f12Hours.format(date));
                            // Clear the error on this field if there was one
                            guardianContactedTime.setError(null);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, 12, 0, false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(timeHour, timeMinute);
                timePickerDialog.show();
            }
        });

        guardianArrivedTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        SeriousFormActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeHour = hourOfDay;
                        timeMinute = minute;

                        String time = timeHour+":"+timeMinute;

                        SimpleDateFormat f24Hours = new SimpleDateFormat(
                                "HH:mm"
                        );
                        try {
                            Date date = f24Hours.parse(time);

                            SimpleDateFormat f12Hours = new SimpleDateFormat(
                                    "hh:mm aa"
                            );
                            guardianArrivedTime.setText(f12Hours.format(date));
                            // Clear the error on this field if there was one
                            guardianArrivedTime.setError(null);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, 12, 0, false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(timeHour, timeMinute);
                timePickerDialog.show();
            }
        });

        ambulanceDoctorCalledTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        SeriousFormActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeHour = hourOfDay;
                        timeMinute = minute;

                        String time = timeHour+":"+timeMinute;

                        SimpleDateFormat f24Hours = new SimpleDateFormat(
                                "HH:mm"
                        );
                        try {
                            Date date = f24Hours.parse(time);

                            SimpleDateFormat f12Hours = new SimpleDateFormat(
                                    "hh:mm aa"
                            );
                            ambulanceDoctorCalledTime.setText(f12Hours.format(date));
                            // Clear the error on this field if there was one
                            ambulanceDoctorCalledTime.setError(null);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, 12, 0, false
                );
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(timeHour, timeMinute);
                timePickerDialog.show();
            }
        });

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

        // Dropdown list for incident likelihood
        List<String> likelihoodList = new ArrayList<>();
        likelihoodList.add("Very likely");
        likelihoodList.add("Likely");
        likelihoodList.add("Rare");

        // List for Yes/No dropdowns
        List<String> yesNo = new ArrayList<>();
        yesNo.add("No");
        yesNo.add("Yes");


        // Dropdown for Injury Type
        ArrayAdapter injuryAdapter = new ArrayAdapter<String>(this, R.layout.list_item, injuryList);
        injury.setAdapter(injuryAdapter);

        // Dropdown for Location
        ArrayAdapter locationAdapter = new ArrayAdapter<String>(this, R.layout.list_item, locationList);
        location.setAdapter(locationAdapter);

        // Dropdown for Treatment
        ArrayAdapter treatmentAdapter = new ArrayAdapter<String>(this, R.layout.list_item, treatmentList);
        treatment.setAdapter(treatmentAdapter);

        // Dropdown for Likelihood
        ArrayAdapter likelihoodAdapter = new ArrayAdapter<String>(this, R.layout.list_item, likelihoodList);
        likelihood.setAdapter(likelihoodAdapter);

        // Yes/No dropdowns
        ArrayAdapter yesNoAdapter = new ArrayAdapter<String>(this, R.layout.list_item, yesNo);
        ambulanceDoctorCalled.setAdapter(yesNoAdapter);
        seniorTeacherInvestigationRequired.setAdapter(yesNoAdapter);
        worksafeMoeAdvised.setAdapter(yesNoAdapter);
        adviseRph.setAdapter(yesNoAdapter);
        followUpWithGuardian.setAdapter(yesNoAdapter);

        // If ambulance/doctor called is changed to No, clear validation error on the time
        ambulanceDoctorCalled.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {ambulanceDoctorCalledTime.setError(null);}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });



        // Autocomplete text + dropdown for Child
        childList = new ArrayList<>();
        ArrayAdapter childAdapter = new ArrayAdapter<String>(this, R.layout.list_item, childList);
        child.setAdapter(childAdapter);
        child.setThreshold(1);

        // Get child names
        DatabaseReference childRef = ref.child("Child");
        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                childList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    childList.add(decrypt(snapshot.child("Name").getValue().toString()));
                }
                childAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // Add a dropdown when clicked
        child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                child.showDropDown();
            }
        });

        // Clear validation error when child selected
        child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                child.setError(null);
            }
        });


        // Dropdowns for Teachers
        ArrayList<String> teacherList = new ArrayList<>();
        ArrayAdapter teacherAdapter = new ArrayAdapter<String>(this, R.layout.list_item, teacherList);
        teacherActionsRequiredBy.setAdapter(teacherAdapter);
        teacherProvided.setAdapter(teacherAdapter);
        teacherChecked.setAdapter(teacherAdapter);

        DatabaseReference userRef = ref.child("User");
        Query myTeacherQuery = userRef.orderByChild("isTeacher").equalTo(true);
        myTeacherQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teacherList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    teacherList.add(decrypt(snapshot.child("Name").getValue().toString()));
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
            myKey = extras.getString("Key");

            // Query the database for the clicked record
            DatabaseReference draftsRef = ref.child("Incident");
            Query myDraftQuery = draftsRef.orderByKey().equalTo(myKey);
            myDraftQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        // Set form elements to show the saved values
                        // Text/date/time fields
                        child.setText(decrypt(snapshot.child("childName").getValue().toString()));
                        description.setText(decrypt(snapshot.child("description").getValue().toString()));
                        ambulanceDoctorCalledTime.setText(snapshot.child("ambulanceDoctorCalledTime").getValue().toString());
                        guardianContactedTime.setText(snapshot.child("guardianContactedTime").getValue().toString());
                        guardianArrivedTime.setText(snapshot.child("guardianArrivedTime").getValue().toString());
                        actionsRequired.setText(decrypt(snapshot.child("actionsRequired").getValue().toString()));
                        dateActionsRequired.setText(snapshot.child("dateActionsRequired").getValue().toString());
                        comments.setText(decrypt(snapshot.child("comments").getValue().toString()));

                        // Teachers
                        String qTeacherActionsRequiredBy = decrypt(snapshot.child("teacherActionsRequiredBy").getValue().toString());
                        if (qTeacherActionsRequiredBy != null) {
                            int spinnerPosition = teacherAdapter.getPosition(qTeacherActionsRequiredBy);
                            teacherActionsRequiredBy.setSelection(spinnerPosition);
                        }

                        String qTeacherProvided = decrypt(snapshot.child("teacherProvided").getValue().toString());
                        if (qTeacherProvided != null) {
                            int spinnerPosition = teacherAdapter.getPosition(qTeacherProvided);
                            teacherProvided.setSelection(spinnerPosition);
                        }

                        String qTeacherChecked = decrypt(snapshot.child("teacherChecked").getValue().toString());
                        if (qTeacherChecked != null) {
                            int spinnerPosition = teacherAdapter.getPosition(qTeacherChecked);
                            teacherChecked.setSelection(spinnerPosition);
                        }

                        // Incident categories
                        String qInjury = snapshot.child("injury").getValue().toString();
                        if (qInjury != null) {
                            int spinnerPosition = injuryAdapter.getPosition(qInjury);
                            injury.setSelection(spinnerPosition);
                        }

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

                        String qLikelihood = snapshot.child("likelihood").getValue().toString();
                        if (qLikelihood != null) {
                            int spinnerPosition = likelihoodAdapter.getPosition(qLikelihood);
                            likelihood.setSelection(spinnerPosition);
                        }

                        // Yes/No fields
                        String qAmbulanceDoctorCalled = snapshot.child("ambulanceDoctorCalled").getValue().toString();
                        if (qAmbulanceDoctorCalled != null) {
                            int spinnerPosition = yesNoAdapter.getPosition(qAmbulanceDoctorCalled);
                            ambulanceDoctorCalled.setSelection(spinnerPosition);
                        }

                        String qSeniorTeacherInvestigationRequired = snapshot.child("seniorTeacherInvestigationRequired").getValue().toString();
                        if (qSeniorTeacherInvestigationRequired != null) {
                            int spinnerPosition = yesNoAdapter.getPosition(qSeniorTeacherInvestigationRequired);
                            seniorTeacherInvestigationRequired.setSelection(spinnerPosition);
                        }

                        String qWorksafeMoeAdvised = snapshot.child("worksafeMoeAdvised").getValue().toString();
                        if (qWorksafeMoeAdvised != null) {
                            int spinnerPosition = yesNoAdapter.getPosition(qWorksafeMoeAdvised);
                            worksafeMoeAdvised.setSelection(spinnerPosition);
                        }

                        String qAdviseRph = snapshot.child("adviseRph").getValue().toString();
                        if (qAdviseRph != null) {
                            int spinnerPosition = yesNoAdapter.getPosition(qAdviseRph);
                            adviseRph.setSelection(spinnerPosition);
                        }

                        String qFollowUpWithGuardian = snapshot.child("followUpWithGuardian").getValue().toString();
                        if (qFollowUpWithGuardian != null) {
                            int spinnerPosition = yesNoAdapter.getPosition(qFollowUpWithGuardian);
                            followUpWithGuardian.setSelection(spinnerPosition);
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }
    }


    // Implement Back button in action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();  return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Function to save an Illness form to Firebase
    private void saveSeriousIncidentForm() {
        // Get UID of logged in user
        String myUID = mAuth.getCurrentUser().getUid();

        // Get text from form elements
        String myChild = child.getText().toString();
        String myDate = date.getText().toString();
        String myTime = incidentTime.getText().toString();
        String myDescription = description.getText().toString();
        String myInjury = injury.getSelectedItem().toString();
        String myLocation = location.getSelectedItem().toString();
        String myTreatment = treatment.getSelectedItem().toString();
        String myAmbulanceDoctorCalled = ambulanceDoctorCalled.getSelectedItem().toString();
        String myAmbulanceDoctorCalledTime = ambulanceDoctorCalledTime.getText().toString();
        String myGuardianContactedTime = guardianContactedTime.getText().toString();
        String myGuardianArrivedTime = guardianArrivedTime.getText().toString();
        String myLikelihood = likelihood.getSelectedItem().toString();
        String myActionsRequired = actionsRequired.getText().toString();
        String myTeacherActionsRequiredBy = teacherActionsRequiredBy.getSelectedItem().toString();
        String myDateActionsRequired = dateActionsRequired.getText().toString();
        String mySeniorTeacherInvestigationRequired = seniorTeacherInvestigationRequired.getSelectedItem().toString();
        String myWorksafeMoeAdvised = worksafeMoeAdvised.getSelectedItem().toString();
        String myAdviseRph = adviseRph.getSelectedItem().toString();
        String myFollowUpWithGuardian = followUpWithGuardian.getSelectedItem().toString();
        String myTeacherProvided = teacherProvided.getSelectedItem().toString();
        String myTeacherChecked = teacherChecked.getSelectedItem().toString();
        String myComments = comments.getText().toString();

        // Additional data for form status
        String formType = "Serious Incident";
        String mFormStatus = "Draft";
        String mPdfFilename = "";

        // Create a HashMap of incident form contents
        HashMap<String, Object> map = new HashMap<>();
        map.put("userID", myUID);
        map.put("childName", encrypt(myChild));
        map.put("incidentDate", myDate);
        map.put("incidentTime", myTime);
        map.put("description", encrypt(myDescription));
        map.put("injury", myInjury);
        map.put("location", myLocation);
        map.put("treatment", myTreatment);
        map.put("ambulanceDoctorCalled", myAmbulanceDoctorCalled);
        map.put("ambulanceDoctorCalledTime", myAmbulanceDoctorCalledTime);
        map.put("guardianContactedTime", myGuardianContactedTime);
        map.put("guardianArrivedTime", myGuardianArrivedTime);
        map.put("likelihood", myLikelihood);
        map.put("actionsRequired", encrypt(myActionsRequired));
        map.put("teacherActionsRequiredBy", encrypt(myTeacherActionsRequiredBy));
        map.put("dateActionsRequired", myDateActionsRequired);
        map.put("seniorTeacherInvestigationRequired", mySeniorTeacherInvestigationRequired);
        map.put("worksafeMoeAdvised", myWorksafeMoeAdvised);
        map.put("adviseRph", myAdviseRph);
        map.put("followUpWithGuardian", myFollowUpWithGuardian);
        map.put("teacherProvided", encrypt(myTeacherProvided));
        map.put("teacherChecked", encrypt(myTeacherChecked));
        map.put("comments", encrypt(myComments));
        map.put("formType", formType);
        map.put("formStatus", mFormStatus);
        map.put("pdfFilename", mPdfFilename);

        // Insert to Realtime Database
        // If already created, update values instead
        if (myKey != null) {
            ref.child("Incident").child(myKey).setValue(map);
        } else {
            myKey = ref.child("Incident").push().getKey();
            ref.child("Incident").child(myKey).setValue(map);
        }
    }


    // When user clicks Save, add the entered information into Firebase Realtime Database
    private void ClickSave() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validations
                // Child name must match an enrolled child
                if (!(childList.contains(child.getText().toString()))) {
                    child.setError("Please select an enrolled child");
                }
                // Date and time must be selected
                else if (date.getText().toString().isEmpty()) {
                    date.setError("Please fill out this field");
                }
                else if (incidentTime.getText().toString().isEmpty()) {
                    incidentTime.setError("Please fill out this field");
                }
                else {
                    saveSeriousIncidentForm();
                    Toast.makeText(SeriousFormActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    // When user clicks Send, add the teacher's signature and send a notification to the Guardian
    private void ClickSend() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validations
                // Child name must match an enrolled child
                if (!(childList.contains(child.getText().toString()))) {
                    child.setError("Please select an enrolled child");
                }
                // Date and time must be selected
                else if (date.getText().toString().isEmpty()) {
                    date.setError("Please fill out this field");
                }
                else if (incidentTime.getText().toString().isEmpty()) {
                    incidentTime.setError("Please fill out this field");
                }
                // Description must not be empty
                else if (description.getText().toString().isEmpty()) {
                    description.setError("Please fill out this field");
                }
                // If ambulance or doctor called, time of call must not be empty
                else if (ambulanceDoctorCalled.getSelectedItem().toString().equals("Yes")
                        && ambulanceDoctorCalledTime.getText().toString().isEmpty()) {
                    ambulanceDoctorCalledTime.setError("Please fill out this field");
                }
                // Guardian contacted and arrived times must be selected
                else if (guardianContactedTime.getText().toString().isEmpty()) {
                    guardianContactedTime.setError("Please fill out this field");
                }
                else if (guardianArrivedTime.getText().toString().isEmpty()) {
                    guardianArrivedTime.setError("Please fill out this field");
                }
                // If an action is required, date when actions required must not be empty
                else if (!(actionsRequired.getText().toString().isEmpty())
                        && dateActionsRequired.getText().toString().isEmpty()) {
                    dateActionsRequired.setError("Please fill out this field");
                }
                // Teacher Provided and Teacher Checked must be different
                else if (teacherProvided.getSelectedItem().toString().equals(teacherChecked.getSelectedItem().toString())) {
                    TextView errorText = (TextView)teacherChecked.getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Form must be checked by another teacher");
                }
                else {

                    if (child.getText().toString().length() == 0 || date.getText().toString().length() == 0 ||
                            incidentTime.getText().toString().length() == 0 || description.getText().toString().length() == 0
                            || description.getText().toString().length() == 0 || guardianArrivedTime.getText().toString().length() == 0
                            || guardianArrivedTime.getText().toString().length() == 0) {
                        Toast.makeText(SeriousFormActivity.this, "Some Fields are empty!", Toast.LENGTH_SHORT).show();
                    } else {

                        PdfDocument pdfDocument = new PdfDocument();
                        Paint paint = new Paint();
                        Paint titlePaint = new Paint();
                        Paint name = new Paint();
                        Paint date = new Paint();
                        Paint time = new Paint();
                        Paint description = new Paint();
                        Paint type = new Paint();
                        Paint location = new Paint();
                        Paint treatment = new Paint();
                        Paint ambulanceCalled = new Paint();
                        Paint contacted = new Paint();
                        Paint arrived = new Paint();
                        Paint happenAgain = new Paint();
                        Paint actions = new Paint();
                        Paint whoAction = new Paint();
                        Paint dateAction = new Paint();
                        Paint given = new Paint();
                        Paint checked = new Paint();
                        Paint comments = new Paint();
                        Paint staff = new Paint();
                        Paint caregiver = new Paint();

                        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(2100, 2970, 1).create();
                        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                        Canvas canvas = page.getCanvas();

                        canvas.drawBitmap(scaledBmp, 0, 0, paint);

                        titlePaint.setTextAlign(Paint.Align.CENTER);
                        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        titlePaint.setTextSize(50);
                        canvas.drawText("Serious accident, incident or injury form", 1100, 200, titlePaint);

                        name.setTextAlign(Paint.Align.LEFT);
                        name.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        name.setTextSize(30);
                        canvas.drawText("Child Name:", 200, 350, name);

                        date.setTextAlign(Paint.Align.LEFT);
                        date.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        date.setTextSize(30);
                        canvas.drawText("Date:", 200, 450, date);

                        time.setTextAlign(Paint.Align.LEFT);
                        time.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        time.setTextSize(30);
                        canvas.drawText("Time:", 200, 550, time);

                        description.setTextAlign(Paint.Align.LEFT);
                        description.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        description.setTextSize(30);
                        canvas.drawText("Description:", 200, 650, description);

                        type.setTextAlign(Paint.Align.LEFT);
                        type.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        type.setTextSize(30);
                        canvas.drawText("Description:", 200, 750, type);

                        location.setTextAlign(Paint.Align.LEFT);
                        location.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        location.setTextSize(30);
                        canvas.drawText("Location:", 200, 850, location);

                        treatment.setTextAlign(Paint.Align.LEFT);
                        treatment.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        treatment.setTextSize(30);
                        canvas.drawText("Given Treatment:", 200, 950, treatment);

                        ambulanceCalled.setTextAlign(Paint.Align.LEFT);
                        ambulanceCalled.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        ambulanceCalled.setTextSize(30);
                        canvas.drawText("Given Treatment:", 200, 1050, ambulanceCalled);

                        contacted.setTextAlign(Paint.Align.LEFT);
                        contacted.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        contacted.setTextSize(30);
                        canvas.drawText("Given Treatment:", 200, 1150, contacted);

                        arrived.setTextAlign(Paint.Align.LEFT);
                        arrived.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        arrived.setTextSize(30);
                        canvas.drawText("Given Treatment:", 200, 1250, arrived);

                        happenAgain.setTextAlign(Paint.Align.LEFT);
                        happenAgain.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        happenAgain.setTextSize(30);
                        canvas.drawText("Given Treatment:", 200, 1350, happenAgain);

                        actions.setTextAlign(Paint.Align.LEFT);
                        actions.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        actions.setTextSize(30);
                        canvas.drawText("Given Treatment:", 200, 1450, actions);

                        whoAction.setTextAlign(Paint.Align.LEFT);
                        whoAction.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        whoAction.setTextSize(30);
                        canvas.drawText("Given Treatment:", 200, 1550, whoAction);

                        dateAction.setTextAlign(Paint.Align.LEFT);
                        dateAction.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        dateAction.setTextSize(30);
                        canvas.drawText("Given Treatment:", 200, 1650, dateAction);

                        given.setTextAlign(Paint.Align.LEFT);
                        given.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        given.setTextSize(30);
                        canvas.drawText("Given By:", 200, 1750, given);

                        checked.setTextAlign(Paint.Align.LEFT);
                        checked.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        checked.setTextSize(30);
                        canvas.drawText("Checked By:", 200, 1850, checked);

                        comments.setTextAlign(Paint.Align.LEFT);
                        comments.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        comments.setTextSize(30);
                        canvas.drawText("Comments:", 200, 1950, comments);

                        staff.setTextAlign(Paint.Align.LEFT);
                        staff.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        staff.setTextSize(30);
                        canvas.drawText("Staff Signature:", 200, 2050, staff);

                        caregiver.setTextAlign(Paint.Align.LEFT);
                        caregiver.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        caregiver.setTextSize(30);
                        canvas.drawText("Caregiver Signature:", 200, 2150, caregiver);


                        pdfDocument.finishPage(page);

                        String fileName = "stored.pdf";
                        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                        String pathDir = baseDir + "/Android/data/com.example.informsafety";
                        File file = new File(pathDir + File.separator + fileName);

                        try {
                            pdfDocument.writeTo(new FileOutputStream(file));
                            Toast.makeText(SeriousFormActivity.this, " PDF Created!", Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(SeriousFormActivity.this, "Something Went Wrong! Please Try Again." + e, Toast.LENGTH_SHORT).show();
                        }

                        pdfDocument.close();
                    }
                    // Save the form before sending
                    saveSeriousIncidentForm();

                    // Get Guardian's ID by querying on Child's name
                    String myChild = child.getText().toString();
                    Query guardianQuery = ref.child("Child").orderByChild("Name").equalTo(encrypt(myChild));
                    guardianQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                // Get the associated key for Guardian
                                String myGuardianID = snapshot.child("ParentKey").getValue().toString();

                                // Query the database for guardian's email
                                DatabaseReference userRef = ref.child("User");
                                Query myUserQuery = userRef.orderByKey().equalTo(myGuardianID);
                                myUserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            // Set form elements to show the saved values
                                            String myGuardianEmail = decrypt(snapshot.child("Email").getValue().toString());

                                            // Go to Passcode to continue
                                            Intent intent = new Intent(SeriousFormActivity.this, PasscodeActivity.class);
                                            intent.putExtra("isSendingForm", true);
                                            intent.putExtra("formKey", myKey);
                                            intent.putExtra("childName", myChild);
                                            intent.putExtra("guardianEmail", myGuardianEmail);
                                            startActivity(intent);
                                        }
                                    }




                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                }
            }
        });
    }

}