package com.example.informsafety;

import static com.example.informsafety.EncryptDecrypt.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("SERIOUS INCIDENT FORM");
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
                    }
                },year,month,day);
                datePickerDialog.show();
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
        yesNo.add("Yes");
        yesNo.add("No");


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


        // Autocomplete text + dropdown for Child
        ArrayList<String> childList = new ArrayList<>();
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
                        // Text/date/time fields
                        child.setText(decrypt(snapshot.child("childName").getValue().toString()));
                        date.setText(snapshot.child("incidentDate").getValue().toString());
//                        time.setText(snapshot.child("incidentTime").getValue().toString());
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
                saveSeriousIncidentForm();
                Toast.makeText(SeriousFormActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }



    // When user clicks Save, add the teacher's signature and send a notification to the Guardian
    private void ClickSend() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save the form before sending
                saveSeriousIncidentForm();

                // Get Guardian's ID by querying on Child's name
                String myChild = child.getText().toString();
                Query guardianQuery = ref.child("Child").orderByChild("Name").equalTo(encrypt(myChild));
                guardianQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                            // Get the associated key for Guardian
                            String myGuardianID = snapshot.child("ParentKey").getValue().toString();

                            // Query the database for guardian's email
                            DatabaseReference userRef = ref.child("User");
                            Query myUserQuery = userRef.orderByKey().equalTo(myGuardianID);
                            myUserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
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
        });
    }

}