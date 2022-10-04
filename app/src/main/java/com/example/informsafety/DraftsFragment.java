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
import android.widget.AdapterView;
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


public class DraftsFragment extends Fragment {

    FirebaseHelper fbh = new FirebaseHelper();
    FirebaseDatabase db = FirebaseDatabase.getInstance("https://informsafetydb-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference ref = db.getReference();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_drafts, container, false);

        // Display a list of all draft forms in a ListView
        // Drafts have sentToGuardian = false
        ListView draftsListView = rootView.findViewById(R.id.draftsListView);
        ArrayList<String> draftsList = new ArrayList<>();
        ArrayList<String> draftsKeyList = new ArrayList<>();
        ArrayList<String> draftsFormTypeList = new ArrayList<>();
        ArrayAdapter draftsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, draftsList);
        draftsListView.setAdapter(draftsAdapter);
        DatabaseReference draftsRef = ref.child("Incident");
//        Query draftsQuery = draftsRef.orderByChild("sentToGuardian").equalTo(false);
        Query draftsQuery = draftsRef.orderByChild("formStatus").equalTo("Draft");


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
//                    String qIncidentDate = snapshot.child("incidentDate").getValue().toString();
//                    String qIncidentTime = snapshot.child("incidentTime").getValue().toString();

                    // Add selected form data into one field in the ListView
                    draftsList.add(qChildName + ", " + qFormType /*+ ", " + qIncidentDate*/);

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