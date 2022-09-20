package com.example.informsafety;

import static com.example.informsafety.EncryptDecrypt.decrypt;
import static com.example.informsafety.EncryptDecrypt.encrypt;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.annotation.Nullable;


public class ChildProtectionNoteFragment extends Fragment {

    AutoCompleteTextView child;
    EditText date, note;
    Button save;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference ref;
    String myKey;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_child_protection_note, container, false);

        // Get user and database refs
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://informsafetydb-default-rtdb.asia-southeast1.firebasedatabase.app/");
        ref = db.getReference();

        // Get references for form elements
        child = rootView.findViewById(R.id.child);
        date = rootView.findViewById(R.id.date);
        note = rootView.findViewById(R.id.note);
        save = rootView.findViewById(R.id.save);

        // Call button functions
        save.setOnClickListener(v -> ClickSave());


        // Autocomplete text + dropdown for Child
        ArrayList<String> childList = new ArrayList<>();
        ArrayAdapter childAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, childList);
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

        return rootView;
    }



    // When user clicks Save, add the entered information into Firebase Realtime Database
    private void ClickSave() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get UID of logged in user
                String myUID = mAuth.getCurrentUser().getUid();

                // Get text from form elements
                String myChild = child.getText().toString();
                String myDate = date.getText().toString();
                String myNote = note.getText().toString();

                // Create a HashMap of incident form contents
                HashMap<String, Object> map = new HashMap<>();
                map.put("userID", myUID);
                map.put("childName", encrypt(myChild));
                map.put("date", myDate);
                map.put("note", encrypt(myNote));

                // Insert to Realtime Database
                // If already created, update values instead
                if (myKey != null) {
                    ref.child("Child Protection").child(myKey).setValue(map);
                } else {
                    myKey = ref.child("Child Protection").push().getKey();
                    ref.child("Child Protection").child(myKey).setValue(map);
                }

                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();

            }
        });
    }
}