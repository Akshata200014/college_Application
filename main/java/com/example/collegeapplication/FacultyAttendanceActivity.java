package com.example.collegeapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FacultyAttendanceActivity extends AppCompatActivity {

    private ListView studentListView;
    private Button saveButton;
    private AttendanceAdapter adapter;
    private DatabaseReference studentRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_attendance);

        studentListView = findViewById(R.id.studentListView);
        saveButton = findViewById(R.id.saveButton);

        // Initialize Firebase Realtime Database reference
        studentRef = FirebaseDatabase.getInstance().getReference("/Students");

        studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Student> students = new ArrayList<>();
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    String rollNo = studentSnapshot.getKey();
                    String name = studentSnapshot.child("name").getValue(String.class);
                    students.add(new Student(rollNo, name));
                }

                // Create and set the adapter
               // adapter = new AttendanceAdapter(FacultyAttendanceActivity.this, students);
                studentListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database read error
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Student> attendedStudents = adapter.getAttendanceData();

                // TODO: Process the attendedStudents list (e.g., display in next page)

                String message = "Attendance saved for " + attendedStudents.size() + " students.";
                Toast.makeText(FacultyAttendanceActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
