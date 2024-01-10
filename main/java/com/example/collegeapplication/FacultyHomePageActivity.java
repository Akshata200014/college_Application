package com.example.collegeapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FacultyHomePageActivity extends AppCompatActivity {

    private Spinner allocatedClassSpinner;
    private Button att_btn;
    private Button tt_btn;

    private List<String> allocatedClassList;
    private ArrayAdapter<String> allocatedClassAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference allocatedFacultyRef = db.collection("AllocatedFaculty");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_home_page);

        allocatedClassSpinner = findViewById(R.id.allocatedClassSpinner);
        att_btn = findViewById(R.id.button4);
        tt_btn = findViewById(R.id.button2);

        allocatedClassList = new ArrayList<>();
        allocatedClassAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allocatedClassList);
        allocatedClassSpinner.setAdapter(allocatedClassAdapter);

        // Fetch allocated class data from Firestore
        retrieveAllocatedClassData();

        att_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFacultyAttendanceActivity();
            }
        });

        tt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimetableActivity();
            }
        });
    }

    private void retrieveAllocatedClassData() {
        allocatedFacultyRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    allocatedClassList.clear();
                    for (DocumentSnapshot document : task.getResult()) {
                        String allocatedClass = document.getString("allocatedClass");
                        if (allocatedClass != null) {
                            allocatedClassList.add(allocatedClass);
                        }
                    }
                    allocatedClassAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(FacultyHomePageActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openFacultyAttendanceActivity() {
        Intent intent = new Intent(this, FacultyAttendanceActivity.class);
        startActivity(intent);
    }

    public void openTimetableActivity() {
        Intent intent = new Intent(this, TimetableActivity.class);
        startActivity(intent);
    }
}
