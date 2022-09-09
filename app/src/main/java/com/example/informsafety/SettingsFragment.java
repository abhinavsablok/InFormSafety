package com.example.informsafety;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import javax.annotation.Nullable;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_settings, container, false);
        Button logout = rootView.findViewById(R.id.logoutBtn);
        logout.setOnClickListener(v -> ClickLogout());
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