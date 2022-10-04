package com.example.informsafety;

import static com.example.informsafety.EncryptDecrypt.decrypt;
import static com.example.informsafety.EncryptDecrypt.encrypt;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

public class MinorFormActivity extends AppCompatActivity {

    AutoCompleteTextView child;
    Spinner teacherProvided, teacherChecked, location, treatment;
    EditText date, incidentTime, description, comments;
    Button save, send;
    FirebaseAuth mAuth;
    FirebaseHelper fbh;
    FirebaseDatabase db;
    DatabaseReference ref;
    String myKey;
    int timeHour, timeMinute;
    Bitmap bmp, scaledBmp;
    int pageWidth = 2100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Minor Incident Form");
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_minor_form);

        ActivityCompat.requestPermissions(MinorFormActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

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
        incidentTime = findViewById(R.id.time);
        description = findViewById(R.id.description);
        comments = findViewById(R.id.comments);
        save = findViewById(R.id.save);
        send = findViewById(R.id.send);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo_kindergarten);
        scaledBmp = Bitmap.createScaledBitmap(bmp, 2300, 946, false);

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
                        MinorFormActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        incidentTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        MinorFormActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
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

        // Dropdown for Location
        ArrayAdapter locationAdapter = new ArrayAdapter<String>(this, R.layout.list_item, locationList);
        location.setAdapter(locationAdapter);

        // Dropdown for Treatment
        ArrayAdapter treatmentAdapter = new ArrayAdapter<String>(this, R.layout.list_item, treatmentList);
        treatment.setAdapter(treatmentAdapter);


        // Dropdowns for Teachers
        ArrayList<String> teacherList = new ArrayList<>();
        ArrayAdapter teacherAdapter = new ArrayAdapter<String>(this, R.layout.list_item, teacherList);
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

//        DatabaseReference teacherRef = ref.child("Teacher");
//        teacherRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                teacherList.clear();
//                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
//                    teacherList.add(decrypt(snapshot.child("Name").getValue().toString()));
//                }
//                teacherAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });


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
//                        String qChildName = decrypt(snapshot.child("childName").getValue().toString());
//                        if (qChildName != null) {
//                            int spinnerPosition = childAdapter.getPosition(qChildName);
//                            child.setSelection(spinnerPosition);
//                        }

                        child.setText(decrypt(snapshot.child("childName").getValue().toString()));
                        date.setText(snapshot.child("incidentDate").getValue().toString());
                        incidentTime.setText(snapshot.child("incidentTime").getValue().toString());
                        description.setText(decrypt(snapshot.child("description").getValue().toString()));

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

                        comments.setText(decrypt(snapshot.child("comments").getValue().toString()));


                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });


        }

    }


    // Function to save a  Minor Incident form to Firebase
    private void saveMinorIncidentForm() {
        // Get UID of logged in user
        String myUID = mAuth.getCurrentUser().getUid();

        // Get text from form elements
        String myChild = child.getText().toString();
        String myDate = date.getText().toString();
        String myTime = incidentTime.getText().toString();
        String myDescription = description.getText().toString();
        String myLocation = location.getSelectedItem().toString();
        String myTreatment = treatment.getSelectedItem().toString();
        String myTeacherProvided = teacherProvided.getSelectedItem().toString();
        String myTeacherChecked = teacherChecked.getSelectedItem().toString();
        String myComments = comments.getText().toString();

        // Additional data for form status
        String formType = "Minor Incident";
        String mFormStatus = "Draft";
        String mPdfFilename = "";

        // Create a HashMap of incident form contents
        HashMap<String, Object> map = new HashMap<>();
        map.put("userID", myUID);
        map.put("childName", encrypt(myChild));
        map.put("incidentDate", myDate);
        map.put("incidentTime", myTime);
        map.put("description", encrypt(myDescription));
        map.put("location", myLocation);
        map.put("treatment", myTreatment);
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
                saveMinorIncidentForm();
                Toast.makeText(MinorFormActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // When user clicks Save, add the teacher's signature and send a notification to the Guardian
    private void ClickSend() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Save the form before sending
                saveMinorIncidentForm();

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
                                        Intent intent = new Intent(MinorFormActivity.this, PasscodeActivity.class);
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

                if (child.getText().toString().length() == 0 || date.getText().toString().length() == 0 ||
                        incidentTime.getText().toString().length() == 0 || description.getText().toString().length() == 0 ||
                        comments.getText().toString().length() == 0) {
                    Toast.makeText(MinorFormActivity.this, "Some Fields are empty!", Toast.LENGTH_SHORT).show();
                } else {

                    PdfDocument pdfDocument = new PdfDocument();
                    Paint paint = new Paint();
                    Paint titlePaint  = new Paint();

                    PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200, 2100, 1).create();
                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);

//                    Canvas canvas = page.getCanvas();

//                    canvas.drawBitmap(scaledBmp, 0, 0, paint);
//
//                    titlePaint.setTextAlign(Paint.Align.CENTER);
//                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//                    titlePaint.setTextSize(70);
//                    canvas.drawText("Minor accident, incident or injury form", pageWidth/2, 1000, titlePaint);

                    pdfDocument.finishPage(page);

                    String fileName = "stored.pdf";
                    String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                    String pathDir = baseDir + "/Android/data/com.example.informsafety";
                    File file = new File(pathDir + File.separator + fileName);

                    try {
                        pdfDocument.writeTo(new FileOutputStream(file));
                        Toast.makeText(MinorFormActivity.this, "Created", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MinorFormActivity.this, "Something Wrong!" + e, Toast.LENGTH_SHORT).show();
                    }

                    pdfDocument.close();
                }
            }
        });
    }



//    public void createPDF(View view) {
//
//        PdfDocument pdfDocument = new PdfDocument();
//        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(210, 297, 1).create();
//        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
//
//        Paint paint = new Paint();
//        String myChild = child.getText().toString();
//        String myDate = date.getText().toString();
//        String myTime = incidentTime.getText().toString();
//        String myDescription = description.getText().toString();
//        String myLocation = location.getSelectedItem().toString();
//        String myTreatment = treatment.getSelectedItem().toString();
//        String myTeacherProvided = teacherProvided.getSelectedItem().toString();
//        String myTeacherChecked = teacherChecked.getSelectedItem().toString();
//        String myComments = comments.getText().toString();
//
//        String[] array = {myChild, myDate, myTime, myDescription, myLocation, myTreatment, myTeacherProvided, myTeacherChecked, myComments};
//
//        // Additional data for form status
//        String formType = "Minor Incident";
//        String mFormStatus = "Draft";
//        String mPdfFilename = "PdfFile";
//
//        // Create a HashMap of incident form contents
//        HashMap<String, Object> map = new HashMap<>();
//        map.put("childName", encrypt(myChild));
//        map.put("incidentDate", myDate);
//        map.put("incidentTime", myTime);
//        map.put("description", myDescription);
//        map.put("location", myLocation);
//        map.put("treatment", myTreatment);
//        map.put("teacherProvided", myTeacherProvided);
//        map.put("teacherChecked", myTeacherChecked);
//        map.put("comments", myComments);
//        map.put("formType", formType);
//        map.put("formStatus", mFormStatus);
//        map.put("pdfFilename", mPdfFilename);
//
//        int x=10, y=25;
//
//        page.getCanvas().drawText(String.valueOf(array), x, y, paint);
//        pdfDocument.finishPage(page);
//
//        String filePath = Environment.getExternalStorageDirectory().getPath() + "/myPDFFile.pdf";
//        File file = new File(filePath);
//
//        try {
//            pdfDocument.writeTo(new FileOutputStream(file));
//        } catch (Exception e) {
//            e.printStackTrace();
//            child.setError("Pdf File Error!");
//        }
//
//        pdfDocument.close();
//    }

}