//  AutoCompleteTextView documentation here:
//  https://developer.android.com/reference/android/widget/AutoCompleteTextView
//  Additional function - show dropdown list when clicked (like a Spinner):
//  https://stackoverflow.com/questions/18983484/combine-spinner-and-autocompletetextview

package com.example.informsafety;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class TestAutoCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_auto_complete);

        // List for autocomplete test
        List<String> testList = new ArrayList<>();
        testList.add("aaa");
        testList.add("aab");
        testList.add("aac");
        testList.add("aba");
        testList.add("abb");
        testList.add("abc");
        testList.add("aca");
        testList.add("acb");
        testList.add("acc");
        testList.add("baa");
        testList.add("bab");
        testList.add("bac");
        testList.add("bba");
        testList.add("bbb");
        testList.add("bbc");
        testList.add("bca");
        testList.add("bcb");
        testList.add("bcc");
        testList.add("caa");
        testList.add("cab");
        testList.add("cac");
        testList.add("cba");
        testList.add("cbb");
        testList.add("cbc");
        testList.add("cca");
        testList.add("ccb");
        testList.add("ccc");

        // Set up Autocomplete object
        AutoCompleteTextView test = findViewById(R.id.test);
        ArrayAdapter testAdapter = new ArrayAdapter<String>(this, R.layout.list_item, testList);
//        ArrayAdapter testAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, testList);
        test.setAdapter(testAdapter);
        test.setThreshold(1);

        // Add a dropdown when clicked
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                test.showDropDown();
            }
        });

    }
}