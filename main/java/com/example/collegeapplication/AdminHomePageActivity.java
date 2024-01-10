package com.example.collegeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;


public class AdminHomePageActivity extends AppCompatActivity {
    private Button ass_coo;
    private Button att_btn;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);
        att_btn = findViewById(R.id.button); // Initialize the button

        ass_coo = findViewById(R.id.AssignCoordinator); // Initialize the button

        ass_coo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAllocateCoordinator();
            }
        });
        att_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFacultyAttendanceActivity();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        Button signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        Spinner courseSpinner = findViewById(R.id.course_spinner);
        Spinner divisionSpinner = findViewById(R.id.division_spinner);

        // Create an ArrayAdapter for the course spinner
        ArrayAdapter<CharSequence> courseAdapter = ArrayAdapter.createFromResource(this,
                R.array.course_options, android.R.layout.simple_spinner_item);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseAdapter);

        // Create an empty ArrayAdapter for the division spinner
        ArrayAdapter<CharSequence> divisionAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        divisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        divisionSpinner.setAdapter(divisionAdapter);

        // Set the OnItemSelectedListener for the course spinner
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Update the division spinner based on the selected course
                String selectedCourse = parent.getItemAtPosition(position).toString();
                int divisionOptionsArrayId = 0;

                if (selectedCourse.equals("MCA")) {
                    divisionOptionsArrayId = R.array.division_options_mca;
                } else if (selectedCourse.equals("MBA")) {
                    divisionOptionsArrayId = R.array.division_options_mba;
                }

                // Update the division spinner adapter
                divisionAdapter.clear();
                divisionAdapter.addAll(getResources().getStringArray(divisionOptionsArrayId));
                divisionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
    public void openAllocateCoordinator() {
        Intent intent = new Intent(this, AllocateCoordinatorActivity.class);
        startActivity(intent);
    }
    private void signOut() {
        firebaseAuth.signOut();
        Intent intent = new Intent(AdminHomePageActivity.this, AdminLoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    public void openFacultyAttendanceActivity() {
        Intent intent = new Intent(this, FacultyAttendanceActivity.class);
        startActivity(intent);
    }
}