package com.example.collegeapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.collegeapplication.databinding.ActivityCoordinatorRegistrationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class CoordinatorRegistrationActivity extends AppCompatActivity {
    ActivityCoordinatorRegistrationBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Firebase database connection
        binding = ActivityCoordinatorRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(this);

        // Set the input filter for phone number to allow only 10 digits
        binding.editTextCoordinatorPhone.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(10),
                new DigitsInputFilter()
        });

        binding.buttonCoordinatorCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.editTextCoordinatorUserId.getText().toString();
                String phone = binding.editTextCoordinatorPhone.getText().toString();
                String email = binding.editTextCoordinatorEmail.getText().toString().trim();
                String password = binding.editTextCoordinatorPassword.getText().toString();

                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startActivity(new Intent(CoordinatorRegistrationActivity.this, CoordinatorLoginActivity.class));
                                progressDialog.cancel();

                                firebaseFirestore.collection("Users")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .set(new User(name, phone, email))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(CoordinatorRegistrationActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(CoordinatorRegistrationActivity.this, "Failed to create account!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CoordinatorRegistrationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();

                            }
                        });
            }
        });

        binding.buttonCoordinatorGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CoordinatorRegistrationActivity.this, CoordinatorLoginActivity.class));
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
        EditText editTextPassword = binding.editTextCoordinatorPassword;
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

    // Input filter to allow only digits for phone number
    private class DigitsInputFilter implements InputFilter {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    }
}
