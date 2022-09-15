package com.example.informsafety;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Array;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] form = {"Minor Incident Form", "Serious Incident Form", "Illness Form"};

        ListView listView = (ListView)view.findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, form);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (i == 0) {
            Intent intent = new Intent(getActivity().getApplication(), MinorFormActivity.class);
            startActivity(intent);
        } if (i == 1) {
            Intent intent = new Intent(getActivity().getApplication(), SeriousFormActivity.class);
            startActivity(intent);
        } if (i == 2) {
            Intent intent = new Intent(getActivity().getApplication(), IllnessFormActivity.class);
            startActivity(intent);
        }
    }
}