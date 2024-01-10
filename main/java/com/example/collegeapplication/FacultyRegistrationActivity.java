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

import com.example.collegeapplication.databinding.ActivityFacultyRegistrationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FacultyRegistrationActivity extends AppCompatActivity {
    ActivityFacultyRegistrationBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Firebase database connection
        binding = ActivityFacultyRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);
        binding.buttonFacultyCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.editTextFacultyUserId.getText().toString();
                String phone = binding.editTextFacultyPhone.getText().toString();
                String email = binding.editTextFacultyEmail.getText().toString().trim();
                String password = binding.editTextFacultyPassword.getText().toString();

                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startActivity(new Intent(FacultyRegistrationActivity.this, FacultyLoginActivity.class));
                                progressDialog.cancel();

                                firebaseFirestore.collection("Faculties")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .set(new User(name, phone, email));

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FacultyRegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        });
            }
        });

        binding.buttonFacultyGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FacultyRegistrationActivity.this, FacultyLoginActivity.class));
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
        EditText editTextPassword = binding.editTextFacultyPassword;
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
