package com.example.collegeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ResetPassword extends AppCompatActivity {

    private EditText Email;
    private Button btnsend;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        Email = findViewById(R.id.editTextEmail);
        btnsend = findViewById(R.id.buttonsend);

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ResetPassword.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else {
                    resetPassword(email);
                }
            }
        });
    }

    private void resetPassword(String email) {
        progressDialog.setMessage("Sending reset password email...");
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(email)

                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPassword.this, "Reset password email sent", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(ResetPassword.this, "Failed to send reset password email: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}