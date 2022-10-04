package com.example.informsafety;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.grpc.Context;

public class SignatureActivity extends AppCompatActivity {

//    FirebaseHelper fbh = new FirebaseHelper();
//    FirebaseDatabase db = FirebaseDatabase.getInstance("https://informsafetydb-default-rtdb.asia-southeast1.firebasedatabase.app/");
//    DatabaseReference ref = db.getReference("Signature");
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Signature");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    Uri uri;


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

//        signImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent,2);
//            }
//        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (uri!=null) {
//                    UploadImage(uri);
                    Toast.makeText(SignatureActivity.this, "Your new signature has been saved!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignatureActivity.this, "Something went wrong! Please try again.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

//    private void UploadImage(Uri uri) {
//        StorageReference file = storageReference.child(System.currentTimeMillis()+ "." +getFileLink(uri));
//        file.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        SignatureModel model =  new SignatureModel(uri.toString());
//                        String sModel = reference.push().getKey();
//                        reference.child(sModel).setValue(model);
//                    }
//                });
//            }
//        });
//    }
//
//    private String getFileLink(Uri uri) {
//        ContentResolver contentResolver = getContentResolver();
//        MimeTypeMap map = MimeTypeMap.getSingleton();
//        return map.getExtensionFromMimeType(contentResolver.getType(uri));
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 2 && requestCode == RESULT_OK && data != null) {
//            uri = data.getData();
//            signImage.setImageURI(uri);
//        }
//    }
}