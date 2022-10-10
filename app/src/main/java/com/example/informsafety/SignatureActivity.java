package com.example.informsafety;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.grpc.Context;

public class SignatureActivity extends AppCompatActivity {

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Signature");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        // Create a storage reference from our app
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a reference to 'signatures/[UID].jpg'
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        StorageReference signaturesRef = storageRef.child("signatures/" + mAuth.getUid() + ".jpg");


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
                Bitmap bm=((BitmapDrawable)signImage.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();

                UploadTask uploadTask = signaturesRef.putBytes(imageBytes);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(SignatureActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(SignatureActivity.this, "Your new signature has been saved!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}