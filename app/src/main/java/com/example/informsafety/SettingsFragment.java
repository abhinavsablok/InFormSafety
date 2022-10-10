package com.example.informsafety;

import static com.example.informsafety.EncryptDecrypt.decrypt;
import static com.example.informsafety.EncryptDecrypt.encrypt;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SettingsFragment extends Fragment implements AdapterView.OnItemClickListener {

    EditText updateEmail, updateName, updatePhone, currentPassword, newPassword, confirmPassword;
    Button update, updateSign;
    ListView childListView;
    ImageView viewSign;

    FirebaseHelper fbh;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference ref;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fbh = new FirebaseHelper();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://informsafetydb-default-rtdb.asia-southeast1.firebasedatabase.app/");
        ref = db.getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();



        String[] setting = {"Email", "Contact Information", "List of Children" , "Change Password", "Set Passcode", "View Signature", "Save Signature", "Logout"};

        ListView listView = (ListView)view.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, setting);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (i == 0) {    // Email
            ClickUpdateEmail();
        } if (i == 1) {  // Contact Information
            ClickUpdateContact();
        } if (i == 2) {  //  Password
            ClickChildInformation();
        }if (i == 3) {  //  Password
            ClickUpdatePassword();
        } if (i == 4) {  // Set Passcode
            ClickSetPasscode();
        } if (i == 5) {  // View Sign
            ClickViewSign();
        } if (i == 6) {  // Save Sign
            ClickSaveSign();
        } if (i == 7) {  // Logout
            ClickLogout();
        }
    }

    private void ClickViewSign() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_view_signature, null);
        dialog.setView(dialogView);

        updateSign = dialogView.findViewById(R.id.updateSign);
        viewSign = dialogView.findViewById(R.id.viewImage);

        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a reference to 'signatures/[UID].jpg'
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        StorageReference signaturesRef = storageRef.child("signatures/" + mAuth.getUid() + ".jpg");

        // Download the user's signature file
        File localFile = null;
        try {
            localFile = File.createTempFile("signature", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File finalLocalFile = localFile;
        signaturesRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap bitmap = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                viewSign.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getActivity(), "No signature found", Toast.LENGTH_SHORT).show();
            }
        });

        updateSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SignatureActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    private void ClickSaveSign() {

        Intent intent = new Intent(getActivity(), SignatureActivity.class);
        startActivity(intent);

    }


    // When the user clicks Email, pop up the Update Email dialog
    private void ClickUpdateEmail() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_update_email, null);
        dialog.setView(dialogView);

        updateEmail = dialogView.findViewById(R.id.updateEmail);
        update = dialogView.findViewById(R.id.update);

        // Populate text fields with current user info
        // Get UID of current user
        String myUID = mAuth.getCurrentUser().getUid();

        // Query the database for the current user's email
        DatabaseReference userRef = ref.child("User");
        Query myUserQuery = userRef.orderByKey().equalTo(myUID);
        myUserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    // Set form elements to show the saved values
                    updateEmail.setText(decrypt(snapshot.child("Email").getValue().toString()));
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        // When update clicked, check field contents then update if checks pass
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = updateEmail.getText().toString();

                if (mEmail.isEmpty()) {
                    updateEmail.setError("Please fill out this field");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                    updateEmail.setError("Please provide a valid email address!");
                } else {
                    // Update email in Firebase Auth
                    String myEmail = updateEmail.getText().toString();
                    user.updateEmail(myEmail);

                    // Write email to Realtime Database
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("Email", encrypt(myEmail));
                    ref.child("User").child(myUID).updateChildren(map);

                    Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();

    }



    // When the user clicks Contact Information, pop up the Update Contact Information dialog
    private void ClickUpdateContact() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_update_contact, null);
        dialog.setView(dialogView);

        updateName = dialogView.findViewById(R.id.updateName);
        updatePhone = dialogView.findViewById(R.id.updatePhone);
        update = dialogView.findViewById(R.id.update);

        // Populate text fields with current user info
        // Get UID of current user
        String myUID = mAuth.getCurrentUser().getUid();

        // Query the database for the current user's email
        DatabaseReference userRef = ref.child("User");
        Query myUserQuery = userRef.orderByKey().equalTo(myUID);
        myUserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    // Set form elements to show the saved values
                    updateName.setText(decrypt(snapshot.child("Name").getValue().toString()));
                    updatePhone.setText(decrypt(snapshot.child("Phone").getValue().toString()));
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        // When Add Child clicked, go to Add Child pop up
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (updateName.getText().toString().trim().isEmpty()) {
                    updateName.setError("Please fill out this field");
                } else if (updatePhone.getText().toString().trim().isEmpty()) {
                    updatePhone.setError("Please fill out this field");

                } else {
                    String mName = updateName.getText().toString();
                    String mPhone = updatePhone.getText().toString();

                    // Write name and phone to Realtime Database
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("Name", encrypt(mName));
                    map.put("Phone", encrypt(mPhone));
                    ref.child("User").child(myUID).updateChildren(map);

                    Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();

    }



    // When the user clicks Child Information, pop up the Child Information dialog
    private void ClickChildInformation() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_child_information, null);
        dialog.setView(dialogView);

        // Display a list of children
        childListView = dialogView.findViewById(R.id.childListView);
        ArrayList<String> childList = new ArrayList<>();
        ArrayList<String> childKeyList = new ArrayList<>();
        ArrayAdapter childAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, childList);
        childListView.setAdapter(childAdapter);
        DatabaseReference childRef = ref.child("Child");

        // Teacher: get a list of all children
        // Guardian: get a list of own children
        Query childQuery;

        // VERSION 1: Teacher perspective only - show list of all children
        childQuery = childRef.orderByKey();

        // VERSION 2: Only teachers can view the full list
//        if (fbh.isTeacherEmail(user.getEmail())) {
//            childQuery = childRef.orderByKey();
//        }
//        else {
//            childQuery = childRef.orderByChild("ParentKey").equalTo(user.getUid());
//        }

        childQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                childList.clear();

                // Get each child in order
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    // Read selected fields from the form
                    String qKey = snapshot.getKey();
                    String qName = decrypt(snapshot.child("Name").getValue().toString());

                    // Add selected form data into one field in the ListView
                    childList.add(qName);

                    // Populate lookup lists to open the clicked form in the correct view
                    childKeyList.add(qKey);

                }
                childAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


        dialog.show();
    }



    // When the user clicks Password, pop up the Change Password dialog
    private void ClickUpdatePassword() {

        // Pop up the Reset Password dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_update_password, null);
        dialog.setView(dialogView);

        currentPassword = dialogView.findViewById(R.id.currentPassword);
        newPassword = dialogView.findViewById(R.id.newPassword);
        confirmPassword = dialogView.findViewById(R.id.confirmPassword);
        update = dialogView.findViewById(R.id.update);


        // When update clicked, check field contents then update if checks pass
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentPassword.getText().toString().trim().isEmpty()) {
                    currentPassword.setError("Please fill out this field");
                } else if (newPassword.getText().toString().trim().isEmpty()) {
                    newPassword.setError("Please fill out this field");
                } else if (confirmPassword.getText().toString().trim().isEmpty()) {
                    confirmPassword.setError("Please fill out this field");
                } else if (!confirmPassword.getText().toString().equals(newPassword.getText().toString())) {
                    confirmPassword.setError("Confirm Password must match New Password");
                } else {
                    // Get auth credentials from the user for re-authentication.
                    String mCurrentPassword = currentPassword.getText().toString();
                    String mNewPassword = newPassword.getText().toString();
                    String mConfirmPassword = confirmPassword.getText().toString();
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), mCurrentPassword);

                    // Attempt to reauthenticate with current password
                    user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(mNewPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getActivity(), "Password Updated", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }
        });

        dialog.show();
    }


    // When the user clicks Set Passcode, go to Passcode activity
    private void ClickSetPasscode() {
        Intent intent = new Intent(getActivity(), PasscodeActivity.class);
        intent.putExtra("isCreatingPasscode", true);
        startActivity(intent);
    }


    // When the user clicks Logout, return to home screen
    private void ClickLogout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to logout from your account. Your login details will not be saved.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        FirebaseAuth.getInstance().signOut();
                        mAuth.signOut();
                        Toast.makeText(getActivity(), "Logged Out", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity().getApplication(), LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.show();
    }
}