package com.example.collegeapplication;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class TimetableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.timetable_layout);

        // Declare and initialize the subject list
        String[] subjects = {"Java", "IOT", "DSA", "Python", "RDBMS"};

        // Find the Spinners and assign them to variables
        Spinner monday9amSpinner = findViewById(R.id.monday9amSpinner);
        Spinner tuesday9amSpinner = findViewById(R.id.tuesday9amSpinner);
        Spinner wednesday9amSpinner = findViewById(R.id.wednesday9amSpinner);
        Spinner thursday9amSpinner = findViewById(R.id.thursday9amSpinner);
        Spinner friday9amSpinner = findViewById(R.id.friday9amSpinner);

        // Create an ArrayAdapter and set it as the adapter for each Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        monday9amSpinner.setAdapter(adapter);
        tuesday9amSpinner.setAdapter(adapter);
        wednesday9amSpinner.setAdapter(adapter);
        thursday9amSpinner.setAdapter(adapter);
        friday9amSpinner.setAdapter(adapter);
    }
}
/*package com.example.collegeapplication;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class TimetableActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // Declare variables for the buttons and spinners
    private Button editButton, saveButton;
    private Spinner monday9amSpinner, thursday9amSpinner;
    private TextView monday9amText, thursday9amText;

    // Create a list to store the selected subjects
    private List<String> selectedSubjects;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable_layout);

        // Initialize the selectedSubjects list
        selectedSubjects = new ArrayList<>();

        // Get references to the buttons and spinners
        editButton = findViewById(R.id.editbutton);
        saveButton = findViewById(R.id.savebutton);
        monday9amSpinner = findViewById(R.id.monday9amSpinner);
        thursday9amSpinner = findViewById(R.id.thursday9amSpinner);
        monday9amText = findViewById(R.id.monday9amText);
        thursday9amText = findViewById(R.id.thursday9amText);

        // Set click listeners for the buttons
        editButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        // Set item selected listeners for the spinners
        monday9amSpinner.setOnItemSelectedListener(this);
        thursday9amSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == editButton) {
            // Activate the spinners for editing
            monday9amSpinner.setEnabled(true);
            thursday9amSpinner.setEnabled(true);
        } else if (v == saveButton) {
            // Save the selected subjects and display them
            String monday9amSubject = monday9amSpinner.getSelectedItem().toString();
            String thursday9amSubject = thursday9amSpinner.getSelectedItem().toString();

            selectedSubjects.clear();
            selectedSubjects.add(monday9amSubject);
            selectedSubjects.add(thursday9amSubject);

            displaySelectedSubjects();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Handle spinner item selection events
        Spinner spinner = (Spinner) parent;
        if (spinner == monday9amSpinner) {
            String selectedSubject = parent.getItemAtPosition(position).toString();
            monday9amText.setText(selectedSubject);
        } else if (spinner == thursday9amSpinner) {
            String selectedSubject = parent.getItemAtPosition(position).toString();
            thursday9amText.setText(selectedSubject);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Handle cases where no item is selected in the spinner
        // You can leave this empty for now or add your own implementation
    }

    private void displaySelectedSubjects() {
        // Display the selected subjects in the TextViews
        StringBuilder subjectsBuilder = new StringBuilder();
        for (String subject : selectedSubjects) {
            subjectsBuilder.append(subject).append("\n");
        }

        monday9amText.setText(subjectsBuilder.toString());
        thursday9amText.setText(subjectsBuilder.toString());
    }
}

 */


