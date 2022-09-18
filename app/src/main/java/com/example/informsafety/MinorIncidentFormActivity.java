package com.example.informsafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MinorIncidentFormActivity extends AppCompatActivity {

    FirebaseDatabase db = FirebaseDatabase.getInstance("https://informsafetydb-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference ref;

    AutoCompleteTextView childName;
    ListView listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minor_incident_form);

        ref = FirebaseDatabase.getInstance().getReference("Child");
        listName = (ListView)findViewById(R.id.listNames);
        childName = (AutoCompleteTextView)findViewById(R.id.childName);

        ValueEventListener event = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                populateSearch(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        ref.addListenerForSingleValueEvent(event);
    }

    private void populateSearch(DataSnapshot snapshot) {

        Log.d("Child", "Reading Data");
        ArrayList<String> names = new ArrayList<>();
        if(snapshot.exists()) {
            for(DataSnapshot ds:snapshot.getChildren()) {
                String name = ds.child("Name").getValue(String.class);
                names.add(name);
            }
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, names);
            childName.setAdapter(adapter);
            childName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String name = childName.getText().toString();
                    searchChild(name);
                }
            });

        } else {
            Log.d("Child", "No Data Found");

        }
    }

    private void searchChild(String name) {

    }
    class Child
    {
        String name;

        public Child(String name) {
            this.name = name;
        }

        public Child()
        {

        }

        public String getName() {
            return name;
        }
    }
}