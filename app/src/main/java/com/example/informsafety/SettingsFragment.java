package com.example.informsafety;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import javax.annotation.Nullable;

public class SettingsFragment extends Fragment {

    EditText name, email, phone, newPassword, currentPassword;
    Button logout, update;
    FirebaseAuth mAuth;
    DatabaseReference ref;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_settings, container, false);

        // Get references for form elements
        logout = rootView.findViewById(R.id.logoutBtn);
        name = rootView.findViewById(R.id.name);
        email = rootView.findViewById(R.id.email);
        phone = rootView.findViewById(R.id.phone);
        newPassword = rootView.findViewById(R.id.newPassword);
        currentPassword = rootView.findViewById(R.id.currentPassword);
        update = rootView.findViewById(R.id.updateBtn);

        // Call button functions
        logout.setOnClickListener(v -> ClickLogout());
//        update.setOnClickListener(v -> ClickUpdate());

        // Populate text fields with current user info

        // Get UID of current user
        String myUID = mAuth.getCurrentUser().getUid();



        return rootView;
    }

    private void ClickLogout() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.getInstance().signOut();
        mAuth.signOut();
        Intent intent = new Intent(getActivity().getApplication(), LoginActivity.class);
        startActivity(intent);
    }
}