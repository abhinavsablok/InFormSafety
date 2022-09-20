package com.example.informsafety;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PasscodeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView instruction;
    View view_01,view_02,view_03,view_04;
    Button btn_01,btn_02,btn_03,btn_04,btn_05,btn_06,btn_07,btn_08,btn_09,btn_clear,btn_00;

    ArrayList<String> numbers_list = new ArrayList<>();
    String passCode="";
    String checkPasscode = "";
    String num_01,num_02,num_03,num_04;

    // Is the user here to create a passcode?
    boolean isCreatingPasscode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);
        initializeComponents();

        // Get the required passcode task from the previous Activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            isCreatingPasscode = extras.getBoolean("isCreatingPasscode");
        }

        // Set instruction text based on task
        if (isCreatingPasscode) {
            instruction.setText("Select a four-digit Passcode");
        }
        else {
            instruction.setText("Enter Passcode");
        }
    }

    private void initializeComponents() {
        instruction=findViewById(R.id.instruction);

        view_01=findViewById(R.id.view_01);
        view_02=findViewById(R.id.view_02);
        view_03=findViewById(R.id.view_03);
        view_04=findViewById(R.id.view_04);

        btn_01=findViewById(R.id.btn_o1);
        btn_02=findViewById(R.id.btn_o2);
        btn_03=findViewById(R.id.btn_o3);
        btn_04=findViewById(R.id.btn_o4);
        btn_05=findViewById(R.id.btn_o5);
        btn_06=findViewById(R.id.btn_o6);
        btn_07=findViewById(R.id.btn_o7);
        btn_08=findViewById(R.id.btn_o8);
        btn_09=findViewById(R.id.btn_o9);
        btn_00=findViewById(R.id.btn_00);
        btn_clear=findViewById(R.id.btn_clear);

        btn_01.setOnClickListener(this);
        btn_02.setOnClickListener(this);
        btn_03.setOnClickListener(this);
        btn_04.setOnClickListener(this);
        btn_05.setOnClickListener(this);
        btn_06.setOnClickListener(this);
        btn_07.setOnClickListener(this);
        btn_08.setOnClickListener(this);
        btn_09.setOnClickListener(this);
        btn_00.setOnClickListener(this);
        btn_clear.setOnClickListener(this);

    }


    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_o1:
                numbers_list.add("1");
                passNumber(numbers_list);
                break;
            case R.id.btn_o2:
                numbers_list.add("2");
                passNumber(numbers_list);
                break;
            case R.id.btn_o3:
                numbers_list.add("3");
                passNumber(numbers_list);
                break;
            case R.id.btn_o4:
                numbers_list.add("4");
                passNumber(numbers_list);
                break;
            case R.id.btn_o5:
                numbers_list.add("5");
                passNumber(numbers_list);
                break;
            case R.id.btn_o6:
                numbers_list.add("6");
                passNumber(numbers_list);
                break;
            case R.id.btn_o7:
                numbers_list.add("7");
                passNumber(numbers_list);
                break;
            case R.id.btn_o8:
                numbers_list.add("8");
                passNumber(numbers_list);
                break;
            case R.id.btn_o9:
                numbers_list.add("9");
                passNumber(numbers_list);
                break;
            case R.id.btn_00:
                numbers_list.add("0");
                passNumber(numbers_list);
                break;
            case R.id.btn_clear:
                numbers_list.clear();
                passNumber(numbers_list);
                break;
        }

    }
    private void  passNumber (ArrayList<String> numbers_list){

        if ( numbers_list.size() == 0) {
            view_01.setBackgroundResource(R.drawable.bg_view_grey_oval);
            view_02.setBackgroundResource(R.drawable.bg_view_grey_oval);
            view_03.setBackgroundResource(R.drawable.bg_view_grey_oval);
            view_04.setBackgroundResource(R.drawable.bg_view_grey_oval);
        }else {
            switch (numbers_list.size()){
                case 1:
                    num_01=numbers_list.get(0);
                    view_01.setBackgroundResource(R.drawable.bg_view_blue_oval);
                    break;
                case 2:
                    num_02=numbers_list.get(1);
                    view_02.setBackgroundResource(R.drawable.bg_view_blue_oval);
                    break;
                case 3:
                    num_03=numbers_list.get(2);
                    view_03.setBackgroundResource(R.drawable.bg_view_blue_oval);
                    break;
                case 4:
                    num_04=numbers_list.get(3);
                    view_04.setBackgroundResource(R.drawable.bg_view_blue_oval);
                    passCode = num_01 + num_02 + num_03 + num_04;

                    // Resolve passcode as required for the current action
                    matchPasscode();
                    break;
            }
        }
    }

    // Resolve passcode as required for the current action
    private void matchPasscode(){
        if (isCreatingPasscode) {
            if (checkPasscode.equals("")) {
                // Set passcode - first entry
                checkPasscode = passCode;
                numbers_list.clear();
                passNumber(numbers_list);
                instruction.setText("Confirm Passcode");
            }
            else {
                // Set passcode - second entry
                if (checkPasscode.equals(passCode)){
                    savePassCode(passCode);
                    finish();
                } else {
                    numbers_list.clear();
                    passNumber(numbers_list);
                    Toast.makeText(this,"Passcode doesn't match, please try again",Toast.LENGTH_SHORT).show();
                }
            }

        }
        else {
            // Enter correct passcode to continue
            if (getPassCode().equals(passCode)){
                finish();
            } else {
                numbers_list.clear();
                passNumber(numbers_list);
                Toast.makeText(this,"Passcode doesn't match, please try again",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private SharedPreferences.Editor savePassCode(String passCode){
        SharedPreferences preferences= getSharedPreferences("passcode_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferences.edit();
        editor.putString("passcode",passCode);
        editor.commit();
        return editor;
    }

    private String getPassCode(){
        SharedPreferences preferences=getSharedPreferences("passcode_pref",Context.MODE_PRIVATE);
        return preferences.getString("passcode","");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}