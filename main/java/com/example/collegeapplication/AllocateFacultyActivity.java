package com.example.collegeapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllocateFacultyActivity extends AppCompatActivity {

    private Spinner facultySpinner;
    private Button allocateButton;
    private TableLayout allocatedFacultyTable;
    private EditText subjectEditText;

    private List<String> facultyList;
    private ArrayAdapter<String> facultyAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference allocatedFacultyRef = db.collection("AllocatedFaculty");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocate_faculty);

        facultySpinner = findViewById(R.id.facultySpinner);
        allocateButton = findViewById(R.id.allocateButton);
        allocatedFacultyTable = findViewById(R.id.tableLayout);
        subjectEditText = findViewById(R.id.subjectEditText);

        facultyList = new ArrayList<>();
        facultyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, facultyList);
        facultySpinner.setAdapter(facultyAdapter);

        // Retrieve faculty data from Firestore
        retrieveFacultyData();

        allocateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedFaculty = facultySpinner.getSelectedItem().toString();
                String subject = subjectEditText.getText().toString();

                // Perform the faculty allocation logic
                if (!subject.isEmpty()) {
                    allocateFaculty(selectedFaculty, subject);
                } else {
                    Toast.makeText(AllocateFacultyActivity.this, "Please enter a subject.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void retrieveFacultyData() {
        // Assuming faculty data is stored under "/Faculties" collection in Firestore
        CollectionReference facultyCollectionRef = FirebaseFirestore.getInstance().collection("/Faculties");

        facultyCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> facultyNames = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String facultyName = documentSnapshot.getString("name");
                    String allocatedSubject = documentSnapshot.getString("allocatedSubject");

                    // If the allocatedSubject field is not null or empty, append it to the facultyName
                    if (allocatedSubject != null && !allocatedSubject.isEmpty()) {
                        facultyName += " - " + allocatedSubject;
                    }

                    facultyNames.add(facultyName);
                }

                // Update the facultyList and notify the adapter
                facultyList.clear();
                facultyList.addAll(facultyNames);
                facultyAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors
            }
        });
    }

    private void allocateFaculty(String facultyName, String subject) {
        // Perform the allocation logic
        addRowToTable(facultyName, subject);
        saveToFirestore(facultyName, subject);
        subjectEditText.setText("");
    }

    private void addRowToTable(String facultyName, String subject) {
        TableRow row = new TableRow(this);

        TextView facultyTextView = new TextView(this);
        facultyTextView.setText(facultyName);
        facultyTextView.setPadding(8, 8, 8, 8);
        row.addView(facultyTextView);

        TextView subjectTextView = new TextView(this);
        subjectTextView.setText(subject);
        subjectTextView.setPadding(8, 8, 8, 8);
        row.addView(subjectTextView);

        ImageButton deleteButton = new ImageButton(this);
        deleteButton.setImageResource(R.drawable.aaa); // Set your delete icon here
        deleteButton.setPadding(8, 8, 8, 8);
        row.addView(deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the row from the table
                allocatedFacultyTable.removeView(row);
                // Delete the allocated faculty and subject from Firestore
                deleteFromFirestore(facultyName, subject);
            }
        });

        allocatedFacultyTable.addView(row);
    }

    private void saveToFirestore(String facultyName, String subject) {
        // Save faculty and subject to Firestore
        Map<String, Object> facultyData = new HashMap<>();
        facultyData.put("facultyName", facultyName);
        facultyData.put("allocatedSubject", subject);

        allocatedFacultyRef.add(facultyData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Successfully saved to Firestore
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                    }
                });
    }

    private void deleteFromFirestore(String facultyName, String subject) {
        // Delete faculty and subject from Firestore
        allocatedFacultyRef
                .whereEqualTo("facultyName", facultyName)
                .whereEqualTo("allocatedSubject", subject)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            documentSnapshot.getReference().delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Successfully deleted from Firestore
                                            Toast.makeText(AllocateFacultyActivity.this, "Deleted from Firestore.", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Handle any errors
                                            Toast.makeText(AllocateFacultyActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Toast.makeText(AllocateFacultyActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
