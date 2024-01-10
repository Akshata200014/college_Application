package com.example.collegeapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.collegeapplication.databinding.ActivityAdminRegistrationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminRegistrationActivity extends AppCompatActivity {
    ActivityAdminRegistrationBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding
        binding = ActivityAdminRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth and Firestore
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);

        // Button click listener to register admin
        binding.buttonAdminCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.editTextAdminUserId.getText().toString();
                String phone = binding.editTextAdminPhone.getText().toString();
                String email = binding.editTextAdminEmail.getText().toString().trim();
                String password = binding.editTextAdminPassword.getText().toString();

                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                // Start AdminLoginActivity after registration
                                startActivity(new Intent(AdminRegistrationActivity.this, AdminLoginActivity.class));
                                progressDialog.cancel();

                                // Store admin data in Firestore under 'admins' collection
                                firebaseFirestore.collection("admins")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .set(new User(name, phone, email))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(AdminRegistrationActivity.this, "Admin registered successfully.", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AdminRegistrationActivity.this, "Failed to register admin.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminRegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        });
            }
        });

        // Button click listener to navigate to AdminLoginActivity
        binding.buttonAdminGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminRegistrationActivity.this, AdminLoginActivity.class));
            }
        });

        // Eye button functionality
        binding.eyeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePasswordVisibility();
            }
        });
    }

    private void togglePasswordVisibility() {
        EditText editTextPassword = binding.editTextAdminPassword;
        int selection = editTextPassword.getSelectionEnd();
        if (passwordVisible) {
            // Hide password
            editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
            passwordVisible = false;
        } else {
            // Show password
            editTextPassword.setTransformationMethod(null);
            passwordVisible = true;
        }
        editTextPassword.setSelection(selection);
    }
}
