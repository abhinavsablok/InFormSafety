package com.example.informsafety;

import static com.example.informsafety.EncryptDecrypt.*;

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
        ArrayList<String> draftsFormTypeList = new ArrayList<>();
        ArrayAdapter draftsAdapter = new ArrayAdapter<String>(this, R.layout.list_item, draftsList);
        draftsListView.setAdapter(draftsAdapter);
        DatabaseReference draftsRef = ref.child("Incident");
        Query draftsQuery = draftsRef.orderByChild("sentToGuardian").equalTo(false);

        draftsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                draftsList.clear();

                // Get each draft form in order
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    // Read selected fields from the form
                    String qKey = snapshot.getKey();
                    String qFormType = snapshot.child("formType").getValue().toString();
                    String qChildName = decrypt(snapshot.child("childName").getValue().toString());
                    String qIncidentDate = snapshot.child("incidentDate").getValue().toString();
                    String qIncidentTime = snapshot.child("incidentTime").getValue().toString();

                    // Add selected form data into one field in the ListView
                    draftsList.add(qChildName + ", " + qFormType + ", " + qIncidentDate);

                    // Populate lookup lists to open the clicked form in the correct view
                    draftsKeyList.add(qKey);
                    draftsFormTypeList.add(qFormType);

                }

                // Reverse the list to get the latest incident at the top
                Collections.reverse(draftsList);
                Collections.reverse(draftsKeyList);
                Collections.reverse(draftsFormTypeList);
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
                // Get the unique key and form type for the clicked form
                String myKey = draftsKeyList.get(position);
                String myFormType = draftsFormTypeList.get(position);

//                Toast.makeText(TestActivity.this,"id" + id + ", position" + position + ", view" + view.toString(),Toast.LENGTH_SHORT).show();
//                Toast.makeText(TestActivity.this,"You selected : " + item,Toast.LENGTH_SHORT).show();
//                Toast.makeText(TestActivity.this, myKey ,Toast.LENGTH_SHORT).show();
//                Toast.makeText(DraftsActivity.this, myFormType ,Toast.LENGTH_SHORT).show();

                // Choose a form activity to open based on the Form Type of the clicked form
                // By default, return to current activity
                Intent intent = new Intent(DraftsActivity.this, DraftsActivity.class);
                if (myFormType.equals("Minor Incident")) {
                    intent = new Intent(DraftsActivity.this, MinorFormActivity.class);
                }
                else if (myFormType.equals("Illness")) {
                    intent = new Intent(DraftsActivity.this, IllnessFormActivity.class);
                }
                else if (myFormType.equals("Serious Incident")) {
                    intent = new Intent(DraftsActivity.this, SeriousFormActivity.class);
                }

                // Open the Form and pass the key to query form contents
                intent.putExtra("Key", myKey);
                startActivity(intent);


            }
        });
    }
}