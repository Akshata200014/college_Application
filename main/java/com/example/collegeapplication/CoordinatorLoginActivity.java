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

import com.example.collegeapplication.databinding.ActivityCoordinatorLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CoordinatorLoginActivity extends AppCompatActivity {
    ActivityCoordinatorLoginBinding binding;
    FirebaseAuth firebaseAuth;
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
        binding = ActivityCoordinatorLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        binding.buttonCoordinatorLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String empid = binding.editTextCoordinatorUsername.getText().toString().trim();
                String password = binding.editTextCoordinatorPassword.getText().toString().trim();

                if (TextUtils.isEmpty(empid)) {
                    binding.editTextCoordinatorUsername.setError("Please enter an empid");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    binding.editTextCoordinatorPassword.setError("Please enter a password");
                    return;
                }

                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(empid, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressDialog.cancel();
                                Toast.makeText(CoordinatorLoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                openCoordinatorHomeActivity();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(CoordinatorLoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        binding.buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String empid = binding.editTextCoordinatorUsername.getText().toString().trim();

                if (TextUtils.isEmpty(empid)) {
                    binding.editTextCoordinatorUsername.setError("Please enter an EmailID");
                    return;
                }

                progressDialog.show();
                firebaseAuth.sendPasswordResetEmail(empid)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.cancel();
                                Toast.makeText(CoordinatorLoginActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(CoordinatorLoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        register_btn = binding.buttonCoordinatorRegister;
        forgotPassword = binding.buttonForgotPassword;
        textInputUsername = binding.editTextCoordinatorUsername;


        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCoordinatorRegistrationActivity();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openResetPassword();
            }
        });
    }

    public void openCoordinatorRegistrationActivity() {
        Intent intent = new Intent(this, CoordinatorRegistrationActivity.class);
        startActivity(intent);
    }

    public void openCoordinatorHomeActivity() {
        Intent intent = new Intent(this, CoordinatorHomePageActivity.class);
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.collegeapplication.databinding.ActivityCoordinatorLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CoordinatorLoginActivity extends AppCompatActivity {
    ActivityCoordinatorLoginBinding binding;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    private Button register_btn;
    private Button login_btn;
    //private Button forgotPassword;
    private EditText textInputUsername;
    private EditText getTextInputPassword;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoordinatorLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        binding.buttonCoordinatorLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String empid = binding.editTextCoordinatorUsername.getText().toString().trim();
                String password = binding.editTextCoordinatorPassword.getText().toString().trim();
                progressDialog.show();
                firebaseAuth.signInWithEmailAndPassword(empid, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                progressDialog.cancel();
                                Toast.makeText(CoordinatorLoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CoordinatorLoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                openCoordinatorHomeActivity();
                            }
                        });
            }
        });

        binding.buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String empid = binding.editTextCoordinatorUsername.getText().toString();
                /*progressDialog.setTitle("Sending Mail");
                progressDialog.show();
                firebaseAuth.sendPasswordResetEmail(empid)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                progressDialog.cancel();
                                Toast.makeText(CoordinatorLoginActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.cancel();
                                Toast.makeText(CoordinatorLoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        login_btn = findViewById(R.id.buttonCoordinatorLogin); // Initialize the button
        register_btn = findViewById(R.id.buttonCoordinatorRegister); // Initialize the button
        textInputUsername = findViewById(R.id.editTextCoordinatorUsername) ;

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCoordinatorRegistrationActivity();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCoordinatorHomeActivity();
            }
        });

    }

    public void openCoordinatorRegistrationActivity() {
        Intent intent = new Intent(this, CoordinatorRegistrationActivity.class);
        startActivity(intent);
    }

    public void openCoordinatorHomeActivity() {
        Intent intent = new Intent(this, CoordinatorHomePageActivity.class);
        startActivity(intent);
    }

}


 */