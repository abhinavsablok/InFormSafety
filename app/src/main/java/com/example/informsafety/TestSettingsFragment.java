package com.example.informsafety;

import static com.example.informsafety.EncryptDecrypt.decrypt;
import static com.example.informsafety.EncryptDecrypt.encrypt;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import javax.annotation.Nullable;

public class TestSettingsFragment extends Fragment {

    EditText name, email, phone, newPassword, confirmNewPassword, currentPassword;
    Button logout, update;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference ref;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_testsettings, container, false);

        // Get user and database refs
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://informsafetydb-default-rtdb.asia-southeast1.firebasedatabase.app/");
        ref = db.getReference();


        // Get references for form elements
        logout = rootView.findViewById(R.id.logoutBtn);
        name = rootView.findViewById(R.id.name);
        email = rootView.findViewById(R.id.email);
        phone = rootView.findViewById(R.id.phone);
        newPassword = rootView.findViewById(R.id.newPassword);
        confirmNewPassword = rootView.findViewById(R.id.confirmNewPassword);
        currentPassword = rootView.findViewById(R.id.currentPassword);
        update = rootView.findViewById(R.id.updateBtn);

        // Call button functions
        logout.setOnClickListener(v -> ClickLogout());
        update.setOnClickListener(v -> ClickUpdate());


        // Populate text fields with current user info
        // Get UID of current user
        String myUID = mAuth.getCurrentUser().getUid();

        // Query the database for the clicked record
        DatabaseReference userRef = ref.child("User");
        Query myUserQuery = userRef.orderByKey().equalTo(myUID);
        myUserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    // Set form elements to show the saved values
                    name.setText(decrypt(snapshot.child("Name").getValue().toString()));
                    email.setText(decrypt(snapshot.child("Email").getValue().toString()));
                    phone.setText(decrypt(snapshot.child("Phone").getValue().toString()));
//                    currentPassword.setText(decrypt(snapshot.child("Password").getValue().toString()));

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });



        return rootView;
    }



    // When the user clicks Logout, return to home screen
    private void ClickLogout() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();
        mAuth.signOut();
        Intent intent = new Intent(getActivity().getApplication(), LoginActivity.class);
        startActivity(intent);
    }



    // When user clicks Update, change their information in Realtime Database
    private void ClickUpdate() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get UID of logged in user
                String myUID = mAuth.getCurrentUser().getUid();

                // Get text from form elements
                String myName = name.getText().toString();
                String myEmail = email.getText().toString();
                String myPhone = phone.getText().toString();
                String myNewPassword = newPassword.getText().toString();
                String myLocation = confirmNewPassword.getText().toString();
                String myTreatment = currentPassword.getText().toString();

                // Create a HashMap of user info
                HashMap<String, Object> map = new HashMap<>();
                map.put("Name", encrypt(myName));
                map.put("Email", encrypt(myEmail));
                map.put("Phone", encrypt(myPhone));
//                map.put("Password", encrypt(myNewPassword));


                // *** ADD CHECKS ON FORM BEFORE UPDATING ***
                // Re-Authenticate user

                // Update in Realtime Database
                ref.child("User").child(myUID).updateChildren(map);

                // If updating password, set in Firebase Auth
//                user.updatePassword(newPassword)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Log.d(TAG, "User password updated.");
//                                }
//                            }
//                        });


                Toast.makeText(getActivity(), "Updated", Toast.LENGTH_SHORT).show();

            }
        });
    }
}