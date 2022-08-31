package com.example.informsafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class LoginActivity extends AppCompatActivity {

    EditText email, password, reg_password,
            name, contact, reg_email, confirmPassword, resetEmail;

    FirebaseAuth mAuth;
    FirebaseHelper fbh;

    Button login, signUp, register, resetPassword, reset;
    TextInputLayout txtInLayoutUsername, txtInLayoutPassword;
    CheckBox rememberMe;

    ProgressBar progressBar;

    //    private DatabaseHelper databaseHelper;
//    long userID;
//    long teacherID;
//    long guardianID;
    com.example.informsafety.RegistrationForm registrationForm;
//    com.example.informsafety.UserModel userModel;
//    LoginForm loginForm;
//    com.example.informsafety.ResetPasswordForm resetPasswordForm;
//    ChangePasswordForm changePasswordForm;
//    ChangeUserDetailsForm changeUserDetailsForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("LOGIN");
        setContentView(R.layout.activity_login);

        signUp = findViewById(R.id.signUp);
        login = findViewById(R.id.loginBtn);
        txtInLayoutUsername = findViewById(R.id.txtInLayoutEmail);
        txtInLayoutPassword = findViewById(R.id.txtInLayoutPassword);
        rememberMe = findViewById(R.id.rememberMe);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        resetPassword = findViewById(R.id.resetPassword);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        fbh = new FirebaseHelper();

        ClickLogin();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSignUp();
            }
        });

        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickResetPassword();
            }
        });

    }

    private void ClickLogin() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mEmail = email.getText().toString();
                String mPassword = password.getText().toString();

                if (!mPassword.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                    mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(LoginActivity.this, "Logged In!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(LoginActivity.this, "Login Failed! Please Try Again!", Toast.LENGTH_LONG).show();
                                }
                            });
                } else if (email.getText().toString().trim().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, "Please fill out these fields",
                            Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.blue));
                    snackbar.show();
                    txtInLayoutUsername.setError("Email should not be empty");
                } else if (password.getText().toString().trim().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, "Please fill out these fields",
                            Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.blue));
                    snackbar.show();
                    txtInLayoutPassword.setError("Password should not be empty");
                } else {
                    txtInLayoutUsername.setError("Entered email or password is incorrect");
                    txtInLayoutPassword.setError("Entered email or password is incorrect");
                }

                if (rememberMe.isChecked()) {

                } else {

                }
            }
        });
    }

    private void ClickResetPassword() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_reset_password, null);
        dialog.setView(dialogView);

        resetEmail = dialogView.findViewById(R.id.resetEmail);
        reset = dialogView.findViewById(R.id.reset);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mEmail = resetEmail.getText().toString();

                if (mEmail.isEmpty()) {
                    resetEmail.setError("Please fill out this field");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                    resetEmail.setError("Please provide a valid email address!");
                } else {
                    mAuth.sendPasswordResetEmail(mEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Check your email to reset your password!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Something went wrong! Please Try again!", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
            }
        });


        dialog.show();
    }

    private void ClickSignUp() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_signup, null);
        dialog.setView(dialogView);

        name = dialogView.findViewById(R.id.name);
        contact = dialogView.findViewById(R.id.contact);
        reg_email = dialogView.findViewById(R.id.reg_email);
        reg_password = dialogView.findViewById(R.id.reg_password);
        confirmPassword = dialogView.findViewById(R.id.confirmPassword);
        register = dialogView.findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().trim().isEmpty()) {
                    name.setError("Please fill out this field");
                } else if (contact.getText().toString().trim().isEmpty()) {
                    contact.setError("Please fill out this field");
                } else if (reg_email.getText().toString().trim().isEmpty()) {
                    reg_email.setError("Please fill out this field");
                } else if (reg_password.getText().toString().trim().isEmpty()) {
                    reg_password.setError("Please fill out this field");
                } else if (confirmPassword.getText().toString().trim().isEmpty()) {
                    confirmPassword.setError("Please fill out this field");
                } else {
                    String mName = name.getText().toString();
                    String mContact = contact.getText().toString();
                    String mEmail = reg_email.getText().toString();
                    String mPassword = reg_password.getText().toString();
                    String mConfirmPassword = confirmPassword.getText().toString();

                    if (Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                        mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

//                                            FirebaseDatabase.getInstance().getReference("User")
//                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                                    .setValue(register);

                                            // Create User object and store to Realtime Database
                                            UserModel userModel = new UserModel(mName, mEmail, mContact);
                                            fbh.insertUser(userModel);

                                            Toast.makeText(LoginActivity.this, "Registration Completed", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Failed to register the user. Please Try Again!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else {
                        reg_email.setError("Please enter a valid email address!");
                    }
                }
            }
        });
        dialog.show();
    }
}

//    private void ClickSignUp() {
//
//        register = findViewById(R.id.register);
//        name = findViewById(R.id.name);
//        contact = findViewById(R.id.contact);
//        email = findViewById(R.id.email);
//        reg_password = findViewById(R.id.reg_password);
//        confirmPassword = findViewById(R.id.confirmPassword);
//        reg_email = findViewById(R.id.reg_email);
//
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.activity_signup, null);
//        dialog.setView(dialogView);
//
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (reg_password.getText().toString().trim().isEmpty()) {
//                    reg_password.setError("Please fill out this field");
//                } else if (name.getText().toString().trim().isEmpty()) {
//
//                    name.setError("Please fill out this field");
//                } else if (contact.getText().toString().trim().isEmpty()) {
//                    contact.setError("Please fill out this field");
//                } else if (reg_email.getText().toString().trim().isEmpty()) {
//                    reg_email.setError("Please fill out this field");
//                } else if (confirmPassword.getText().toString().trim().isEmpty()) {
//                    confirmPassword.setError("Please fill out this field");
//                } else {
//                    String mName = name.getText().toString();
//                    String mContact = contact.getText().toString();
//                    String mEmail = reg_email.getText().toString();
//                    String mPassword = reg_password.getText().toString();
//                    String mConfirmPassword = confirmPassword.getText().toString();
//
//
//
//                    if (Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
//                        mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
//                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                        if (task.isSuccessful()) {
//                                            User user = new User(mName, mContact, mEmail, mPassword, mConfirmPassword);
//                                            FirebaseDatabase.getInstance().getReference("Users")
//                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                                    .setValue(user);
//                                            Toast.makeText(LoginActivity.this, "Registration Completed", Toast.LENGTH_LONG).show();
//                                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//                                        } else {
//                                            Toast.makeText(LoginActivity.this, "Failed to register the user. Please Try Again!", Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                });
//                    } else {
//                        reg_email.setError("Please enter a valid email address!");
//                    }
//                }
//            }
//        });
//        dialog.show();
//
//
//        // Import database functions
//        DatabaseHelper databaseHelper = new DatabaseHelper(LoginActivity.this);
//
//        // FOR TESTING ONLY: Empty the database tables and start over
//        databaseHelper.deleteRecords();
//    }

//      Test Registration
//        RegistrationForm registrationForm = new RegistrationForm("Teacher 1", "teacher1@huttkindergartens.org.nz", "0210727600", "password01", "password01");
//        registerUser(registrationForm);

//    private void ClickRegister(RegistrationForm registrationForm) {
//
//        if (registrationForm.getPassword().equals(registrationForm.getConfirmPassword())) {
//
//            // If user has an ECE Centre email address, register them as a Teacher
//            // Otherwise register them as a Guardian (default value 0).
//            if (registrationForm.getEmail().contains("@huttkindergartens.org.nz")) {
//                registrationForm.setTeacher(1);
//            }
//
//            // Insert to Teacher/Guardian table according to isTeacher flag
//            if (registrationForm.isTeacher() == 1) {
//                teacherID = databaseHelper.insertTeacher(registrationForm);
//                registrationForm.setTeacherID((int) teacherID);
//            }
//            else {
//                guardianID = databaseHelper.insertGuardian(registrationForm);
//                registrationForm.setGuardianID((int) guardianID);
//            }
//
//            // Insert to User table
//            userID = databaseHelper.insertUser(registrationForm);
//            Toast.makeText(LoginActivity.this, "Registered " + registrationForm.getName(), Toast.LENGTH_SHORT).show();
//
//            // Log the user into their new account
//            loginForm = new LoginForm(registrationForm.getEmail(), registrationForm.getPassword());
//            logInUser(loginForm);
//            Toast.makeText(LoginActivity.this, "Logged in as " + userModel.getName(), Toast.LENGTH_SHORT).show();
//
//        }
//        else {
//            Toast.makeText(LoginActivity.this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
//        }
//    }


//    public void registerUser(RegistrationForm registrationForm) {
//
//        // Check that password and confirm password match
//        if (registrationForm.getPassword().equals(registrationForm.getConfirmPassword())) {
//
//            // If user has an ECE Centre email address, register them as a Teacher
//            // Otherwise register them as a Guardian (default value 0).
//            if (registrationForm.getEmail().contains("@huttkindergartens.org.nz")) {
//                registrationForm.setTeacher(1);
//            }
//
//            // Insert to Teacher/Guardian table according to isTeacher flag
//            if (registrationForm.isTeacher() == 1) {
//                teacherID = databaseHelper.insertTeacher(registrationForm);
//                registrationForm.setTeacherID((int) teacherID);
//            }
//            else {
//                guardianID = databaseHelper.insertGuardian(registrationForm);
//                registrationForm.setGuardianID((int) guardianID);
//            }
//
//            // Insert to User table
//            userID = databaseHelper.insertUser(registrationForm);
////            Toast.makeText(MainActivity.this, "Registered " + registrationForm.getName(), Toast.LENGTH_SHORT).show();
//
//            // Log the user into their new account
//            loginForm = new LoginForm(registrationForm.getEmail(), registrationForm.getPassword());
//            logInUser(loginForm);
//            Toast.makeText(LoginActivity.this, "Logged in as " + userModel.getName(), Toast.LENGTH_SHORT).show();
//
//        }
//        else {
//            Toast.makeText(LoginActivity.this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
//        }
//
//    }

//    public void logInUser(LoginForm loginForm) {
//
//        // Encrypt email to match to database
//        //String encEmail = encrypt(loginForm.getEmail());
//
//        // Get user ID matching the provided email
//        int id = databaseHelper.getUserIDFromEmail(loginForm.getEmail());
//
//        // Get and decrypt password matching the provided id
//        String correctPassword = databaseHelper.selectPassword(id);
//
//        // If correct password given, generate a UserModel with all of the user's data
//        if (loginForm.getPassword().equals(correctPassword)) {
//
//            userModel = databaseHelper.selectUser(id);
//
////            Toast.makeText(MainActivity.this, "Logged in as " + userModel.getName(), Toast.LENGTH_SHORT).show();
//
//        } else {
//            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//}