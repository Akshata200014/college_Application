package com.example.collegeapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.collegeapplication.databinding.ActivityAdminLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminLoginActivity extends AppCompatActivity {
    ActivityAdminLoginBinding binding;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    ProgressDialog progressDialog;
    private Button register_btn;
    private Button login_btn;
    private Button forgotPassword;
    private EditText textInputUsername;
    private EditText getTextInputPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        binding.buttonAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String empid = binding.editTextAdminUsername.getText().toString().trim();
                String password = binding.editTextAdminPassword.getText().toString().trim();

                if (TextUtils.isEmpty(empid)) {
                    binding.editTextAdminUsername.setError("Please enter an empid");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    binding.editTextAdminPassword.setError("Please enter a password");
                    return;
                }

                progressDialog.show();

                // Check if empid exists in the "admins" node in Firestore
                firebaseFirestore.collection("/admins")
                        .document(empid)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    // Admin empid exists, proceed with login
                                    firebaseAuth.signInWithEmailAndPassword(empid, password)
                                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                @Override
                                                public void onSuccess(AuthResult authResult) {
                                                    progressDialog.cancel();
                                                    Toast.makeText(AdminLoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                                    openAdminHomeActivity();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.cancel();
                                                    Toast.makeText(AdminLoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    progressDialog.cancel();
                                    Toast.makeText(AdminLoginActivity.this, "Invalid empid", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(AdminLoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        register_btn = binding.buttonAdminRegister;
        forgotPassword = binding.buttonforgotPassword;
        textInputUsername = binding.editTextAdminUsername;

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdminRegistrationActivity();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openResetPassword();
            }
        });
    }

    public void openAdminRegistrationActivity() {
        Intent intent = new Intent(this, AdminRegistrationActivity.class);
        startActivity(intent);
    }

    public void openAdminHomeActivity() {
        Intent intent = new Intent(this, AdminHomePageActivity.class);
        startActivity(intent);
    }

    public void openResetPassword() {
        Intent intent = new Intent(this, ResetPassword.class);
        startActivity(intent);
    }
}
