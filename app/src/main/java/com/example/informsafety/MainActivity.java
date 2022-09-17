package com.example.informsafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        handler.sendEmptyMessageDelayed(222, 400);

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 222 && mUser != null && mUser.isEmailVerified()) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else if (msg.what == 222){
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                Intent intent = new Intent(MainActivity.this, MinorFormActivity.class);
//                Intent intent = new Intent(MainActivity.this, IllnessFormActivity.class);
//                Intent intent = new Intent(MainActivity.this, SeriousFormActivity.class);
//                Intent intent = new Intent(MainActivity.this, DraftsActivity.class);
                Intent intent = new Intent(MainActivity.this, TestAutoCompleteActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
}