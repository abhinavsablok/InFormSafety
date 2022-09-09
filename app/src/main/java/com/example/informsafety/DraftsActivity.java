package com.example.informsafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class DraftsActivity extends AppCompatActivity {

    FirebaseHelper fbh = new FirebaseHelper();
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://informsafetydb-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference ref = db.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts);


        // Display a list of all draft forms in a ListView
        // Drafts have sentToGuardian = false
        ListView draftsListView = findViewById(R.id.draftsListView);
        ArrayList<String> draftsList = new ArrayList<>();
        ArrayList<String> draftsKeyList = new ArrayList<>();
        ArrayAdapter draftsAdapter = new ArrayAdapter<String>(this, R.layout.list_item, draftsList);
        draftsListView.setAdapter(draftsAdapter);
        DatabaseReference draftsRef = ref.child("Incident");
        Query draftsQuery = draftsRef.orderByChild("sentToGuardian").equalTo(false);

        draftsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                draftsList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    // Get each draft form in order and do something with it
                    String qKey = snapshot.getKey();
                    String qChildName = snapshot.child("childName").getValue().toString();
                    String qIncidentDate = snapshot.child("incidentDate").getValue().toString();
                    String qIncidentTime = snapshot.child("incidentTime").getValue().toString();

                    // Add selected form data into one field in the ListView
                    draftsList.add(qChildName + ", " + qIncidentDate + ", " + qIncidentTime);
                    draftsKeyList.add(qKey);

                }

                // Reverse the list to get the latest incident at the top
                Collections.reverse(draftsList);
                Collections.reverse(draftsKeyList);
                draftsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


        // Click a form on the drafts list to reopen the form
        draftsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> draftsListView, View view, int position, long id) {
                String myKey = draftsKeyList.get(position);
//                Toast.makeText(TestActivity.this,"id" + id + ", position" + position + ", view" + view.toString(),Toast.LENGTH_SHORT).show();
//                Toast.makeText(TestActivity.this,"You selected : " + item,Toast.LENGTH_SHORT).show();
//                Toast.makeText(TestActivity.this, myKey ,Toast.LENGTH_SHORT).show();

                // Query the database for the clicked record
                Query myDraftQuery = draftsRef.orderByKey().equalTo(myKey);
                myDraftQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            // Do something with the selected draft form
//                            Toast.makeText(DraftsActivity.this, snapshot.toString() ,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DraftsActivity.this, MinorFormActivity.class);
                            intent.putExtra("Contents", snapshot.toString());
                            startActivity(intent);
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