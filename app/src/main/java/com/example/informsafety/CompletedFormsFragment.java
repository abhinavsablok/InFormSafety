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


public class CompletedFormsFragment extends Fragment {

    FirebaseHelper fbh = new FirebaseHelper();
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://informsafetydb-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference ref = db.getReference();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_completed_forms, container, false);

        // Display a list of all draft forms in a ListView
        ListView completedFormsListView = rootView.findViewById(R.id.completedFormsListView);
        ArrayList<String> completedFormsList = new ArrayList<>();
        ArrayList<String> completedFormsKeyList = new ArrayList<>();
        ArrayList<String> completedFormsFormTypeList = new ArrayList<>();
        ArrayAdapter completedFormsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, completedFormsList);
        completedFormsListView.setAdapter(completedFormsAdapter);
        DatabaseReference completedFormsRef = ref.child("Incident");
        Query completedFormsQuery = completedFormsRef.orderByChild("formStatus").equalTo("Completed");

        completedFormsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                completedFormsList.clear();

                // Get each draft form in order
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    // Read selected fields from the form
                    String qKey = snapshot.getKey();
                    String qFormType = snapshot.child("formType").getValue().toString();
                    String qChildName = decrypt(snapshot.child("childName").getValue().toString());
                    String qIncidentDate = snapshot.child("incidentDate").getValue().toString();
                    String qIncidentTime = snapshot.child("incidentTime").getValue().toString();

                    // Add selected form data into one field in the ListView
                    completedFormsList.add(qChildName + ", " + qFormType + ", " + qIncidentDate);

                    // Populate lookup lists to open the clicked form in the correct view
                    completedFormsKeyList.add(qKey);
                    completedFormsFormTypeList.add(qFormType);

                }

                // Reverse the list to get the latest incident at the top
                Collections.reverse(completedFormsList);
                Collections.reverse(completedFormsKeyList);
                Collections.reverse(completedFormsFormTypeList);
                completedFormsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


        // Click a form on the completedForms list to reopen the form
        completedFormsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> completedFormsListView, View view, int position, long id) {
                // Get the unique key and form type for the clicked form
                String myKey = completedFormsKeyList.get(position);
                String myFormType = completedFormsFormTypeList.get(position);

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