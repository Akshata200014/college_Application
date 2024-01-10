package com.example.collegeapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FacultyLoginActivity extends AppCompatActivity {
    private EditText editTextFacultyUsername;
    private EditText editTextFacultyPassword;
    private Button buttonFacultyLogin;
    private Button buttonFacultyRegister;
    private Button buttonForgotPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        editTextFacultyUsername = findViewById(R.id.editTextFacultyUsername);
        editTextFacultyPassword = findViewById(R.id.editTextFacultyPassword);
        buttonFacultyLogin = findViewById(R.id.buttonFacultyLogin);
        buttonFacultyRegister = findViewById(R.id.buttonFacultyRegister);
        buttonForgotPassword = findViewById(R.id.buttonForgotPassword);

        buttonFacultyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextFacultyUsername.getText().toString().trim();
                String password = editTextFacultyPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    editTextFacultyUsername.setError("Please enter a username");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    editTextFacultyPassword.setError("Please enter a password");
                    return;
                }

                progressDialog.setMessage("Logging in...");
                progressDialog.show();

                firebaseAuth.signInWithEmailAndPassword(username, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressDialog.dismiss();
                                Toast.makeText(FacultyLoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                openFacultyHomeActivity();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(FacultyLoginActivity.this, "Login Failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        buttonFacultyRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacultyRegistrationActivity();
            }
        });

        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResetPassword();
            }
        });
    }

    public void openFacultyRegistrationActivity() {
        Intent intent = new Intent(this, FacultyRegistrationActivity.class);
        startActivity(intent);
    }

    public void openFacultyHomeActivity() {
        Intent intent = new Intent(this, FacultyHomePageActivity.class);
        startActivity(intent);
    }

    public void openResetPassword() {
        Intent intent = new Intent(this, ResetPassword.class);
        startActivity(intent);
    }
}







/*package com.example.collegeapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.collegeapplication.databinding.ActivityAdminLoginBinding;
import com.example.collegeapplication.databinding.ActivityFacultyLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FacultyLoginActivity extends AppCompatActivity {
    ActivityFacultyLoginBinding binding;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    private Button login_btn;
    private Button register_btn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityFacultyLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);

        binding.buttonFacultyLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String empid=binding.editTextAdminUsername.getText().toString().trim();
                String password=binding.editTextAdminPassword.getText().toString().trim();
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(empid,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressDialog.cancel();
                                Toast.makeText(FacultyLoginActivity.this, "Login Succesfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FacultyLoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        binding.buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String empid=binding.editTextAdminUsername.getText().toString();
                progressDialog.setTitle("Sending Mail");
                progressDialog.show();
                firebaseAuth.sendPasswordResetEmail(empid)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.cancel();
                                Toast.makeText(FacultyLoginActivity.this, "Email Send", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(FacultyLoginActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        login_btn = findViewById(R.id.buttonFacultyLogin);// Initialize the button
        register_btn = findViewById(R.id.buttonFacultyRegister);// Initialize the button

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFacultyHomePageActivity();
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFacultyRegistrationActivity();
            }
        });
    }
    public void openFacultyHomePageActivity() {
        Intent intent = new Intent(this, FacutyHomePageActivity.class);
        startActivity(intent);
    }

    public void openFacultyRegistrationActivity() {
        Intent intent = new Intent(this, FacultyRegistrationActivity.class);
        startActivity(intent);
    }
}

 */