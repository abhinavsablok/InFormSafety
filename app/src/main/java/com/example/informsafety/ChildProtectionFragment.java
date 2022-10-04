package com.example.informsafety;

import static com.example.informsafety.EncryptDecrypt.decrypt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import javax.annotation.Nullable;

public class ChildProtectionFragment extends Fragment {

    Button addNote;
    ListView notesListView;
    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference ref;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_child_protection, container, false);

        // Get user and database refs
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance("https://informsafetydb-default-rtdb.asia-southeast1.firebasedatabase.app/");
        ref = db.getReference();
        
        // Get references for form elements
        addNote = rootView.findViewById(R.id.addNote);
        notesListView = rootView.findViewById(R.id.notesListView);

        // Call button functions
        addNote.setOnClickListener(v -> ClickAddNote());


        // Display a list of all notes in a ListView
        ArrayList<String> notesList = new ArrayList<>();
        ArrayList<String> notesKeyList = new ArrayList<>();
        ArrayAdapter notesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, notesList);
        notesListView.setAdapter(notesAdapter);
        DatabaseReference notesRef = ref.child("Child Protection");
        Query notesQuery = notesRef.orderByKey();

        notesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notesList.clear();
                notesKeyList.clear();

                // Get each draft form in order
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    // Read selected fields from the form
                    String qKey = snapshot.getKey();
                    String qChildName = decrypt(snapshot.child("childName").getValue().toString());
                    String qDate = snapshot.child("date").getValue().toString();

                    // Add selected form data into one field in the ListView
                    notesList.add(qChildName + ", " + qDate);

                    // Populate lookup lists to open the clicked form in the correct view
                    notesKeyList.add(qKey);
                }

                // Reverse the list to get the latest incident at the top
                Collections.reverse(notesList);
                Collections.reverse(notesKeyList);
                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


        // Click a form on the notes list to reopen the note
        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> draftsListView, View view, int position, long id) {
                // Get the unique key and form type for the clicked form
                String myKey = notesKeyList.get(position);

                // Open the note in Note Activity and pass the key to query note contents
                Intent intent = new Intent(getActivity(), ChildProtectionNoteActivity.class);
                intent.putExtra("Key", myKey);
                startActivity(intent);
            }
        });

        return rootView;
    }


    // When the user clicks Add Note, open Child Protection Note
    private void ClickAddNote() {
        Intent intent = new Intent(getActivity(), ChildProtectionNoteActivity.class);
        startActivity(intent);

    }
}