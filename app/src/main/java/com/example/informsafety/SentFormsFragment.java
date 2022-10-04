package com.example.informsafety;

import static com.example.informsafety.EncryptDecrypt.decrypt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import javax.annotation.Nullable;


public class SentFormsFragment extends Fragment {

    FirebaseHelper fbh = new FirebaseHelper();
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://informsafetydb-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference ref = db.getReference();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_sent_forms, container, false);

        // Display a list of all draft forms in a ListView
        ListView sentFormsListView = rootView.findViewById(R.id.sentFormsListView);
        ArrayList<String> sentFormsList = new ArrayList<>();
        ArrayList<String> sentFormsKeyList = new ArrayList<>();
        ArrayList<String> sentFormsFormTypeList = new ArrayList<>();
        ArrayAdapter sentFormsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, sentFormsList);
        sentFormsListView.setAdapter(sentFormsAdapter);
        DatabaseReference sentFormsRef = ref.child("Incident");
        Query sentFormsQuery = sentFormsRef.orderByChild("formStatus").equalTo("Sent");

        sentFormsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sentFormsList.clear();

                // Get each draft form in order
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    // Read selected fields from the form
                    String qKey = snapshot.getKey();
                    String qFormType = snapshot.child("formType").getValue().toString();
                    String qChildName = decrypt(snapshot.child("childName").getValue().toString());
                    String qIncidentDate = snapshot.child("incidentDate").getValue().toString();
                    String qIncidentTime = snapshot.child("incidentTime").getValue().toString();

                    // Add selected form data into one field in the ListView
                    sentFormsList.add(qChildName + ", " + qFormType + ", " + qIncidentDate);

                    // Populate lookup lists to open the clicked form in the correct view
                    sentFormsKeyList.add(qKey);
                    sentFormsFormTypeList.add(qFormType);

                }

                // Reverse the list to get the latest incident at the top
                Collections.reverse(sentFormsList);
                Collections.reverse(sentFormsKeyList);
                Collections.reverse(sentFormsFormTypeList);
                sentFormsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


        // Click a form on the sentForms list to reopen the form
        sentFormsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> sentFormsListView, View view, int position, long id) {
                // Get the unique key and form type for the clicked form
                String myKey = sentFormsKeyList.get(position);
                String myFormType = sentFormsFormTypeList.get(position);

                // Choose a form activity to open based on the Form Type of the clicked form
                // By default, return to current activity
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                if (myFormType.equals("Minor Incident")) {
                    intent = new Intent(getActivity(), MinorFormActivity.class);
                }
                else if (myFormType.equals("Illness")) {
                    intent = new Intent(getActivity(), IllnessFormActivity.class);
                }
                else if (myFormType.equals("Serious Incident")) {
                    intent = new Intent(getActivity(), SeriousFormActivity.class);
                }
                else {
                    Toast.makeText(getActivity(), "Unable to determine form type" ,Toast.LENGTH_SHORT).show();
                }

                // Open the Form and pass the key to query form contents
                intent.putExtra("Key", myKey);
                startActivity(intent);
            }
        });

        return rootView;
    }
}