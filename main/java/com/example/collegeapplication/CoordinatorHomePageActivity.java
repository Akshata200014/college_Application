package com.example.collegeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

public class CoordinatorHomePageActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private Button att_btn;
    private Button tt_btn;
    private Button Assign_faculty;
    private TextView allocatedClassTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_home_page);

        att_btn = findViewById(R.id.button); // Initialize the button
        tt_btn = findViewById(R.id.button4); // Initialize the button
        Assign_faculty = findViewById(R.id.button3); // Initialize the button

        att_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFacultyAttendanceActivity();
            }
        });

        tt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openTimetableActivity(); }
        });
        Assign_faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openAllocateFacultyActivity(); }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        Button signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        Button studentDBButton = findViewById(R.id.StudentDB_btn);
        studentDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStudentDBActivity();
            }
        });
    }

    private void signOut() {
        firebaseAuth.signOut();
        Intent intent = new Intent(CoordinatorHomePageActivity.this, CoordinatorLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void openStudentDBActivity() {
        Intent intent = new Intent(CoordinatorHomePageActivity.this,studentDB.class);
        startActivity(intent);
    }
    public void openFacultyAttendanceActivity() {
        Intent intent = new Intent(this, FacultyAttendanceActivity.class);
        startActivity(intent);
    }

    public void openTimetableActivity() {
        Intent intent = new Intent(this, TimetableActivity.class);
        startActivity(intent);

    }
    public void openAllocateFacultyActivity() {
        Intent intent = new Intent(this, AllocateFacultyActivity.class);
        startActivity(intent);

    }
    private void displayAllocatedClass() {
        // Get the current coordinator's name from Firebase Auth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String coordinatorName = currentUser != null ? currentUser.getDisplayName() : "";

        // Check if the coordinator name is not empty
        if (!coordinatorName.isEmpty()) {
            // Assuming "AllocatedCoordinators" is the collection in Firestore that stores the allocations
            // The document ID in this collection will be the coordinator name
            DocumentReference coordinatorRef = FirebaseFirestore.getInstance()
                    .collection("AllocatedCoordinators")
                    .document(coordinatorName);

            coordinatorRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String allocatedClass = document.getString("className");
                            if (allocatedClass != null && !allocatedClass.isEmpty()) {
                                allocatedClassTextView.setText(allocatedClass);
                            } else {
                                allocatedClassTextView.setText("No Class Allocated");
                            }
                        } else {
                            allocatedClassTextView.setText("No Class Allocated");
                        }
                    } else {
                        Log.e("CoordinatorHomePage", "Error fetching allocated class: " + task.getException());
                        allocatedClassTextView.setText("Failed to fetch allocated class");
                    }
                }
            });
        } else {
            allocatedClassTextView.setText("No Class Allocated");
        }
    }

}



