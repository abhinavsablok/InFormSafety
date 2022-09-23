package com.example.informsafety;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

public class SignatureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        SignaturePad sign = findViewById(R.id.sign);
        Button checkSign = findViewById(R.id.checkSign);
        ImageView signImage = findViewById(R.id.signImage);
        Button confirm = findViewById(R.id.confirm);

        checkSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = sign.getSignatureBitmap();
                signImage.setImageBitmap(bitmap);
                sign.clear();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignatureActivity.this, "Your new signature has been saved!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}