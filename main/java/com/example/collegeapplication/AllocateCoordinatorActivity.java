package com.example.collegeapplication;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AllocateCoordinatorActivity extends AppCompatActivity {

    private Spinner classSpinner;
    private Spinner coordinatorSpinner;
    private Button allocateButton;

    private List<String> classList;
    private List<String> coordinatorList;
    private ArrayAdapter<String> classAdapter;
    private ArrayAdapter<String> coordinatorAdapter;

    private FirebaseFirestore db;
    private Set<String> allocatedCoordinators; // Use a Set to store unique allocation keys
    private Set<String> allocatedClasses; // Use a Set to store unique allocated classes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocate_coordinator);

        classSpinner = findViewById(R.id.classSpinner);
        coordinatorSpinner = findViewById(R.id.coordinatorSpinner);
        allocateButton = findViewById(R.id.allocateButton);

        classList = new ArrayList<>(); // Initialize an empty list
        coordinatorList = new ArrayList<>();
        classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classList);
        coordinatorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, coordinatorList);

        classSpinner.setAdapter(classAdapter);
        coordinatorSpinner.setAdapter(coordinatorAdapter);

        db = FirebaseFirestore.getInstance();
        allocatedCoordinators = new HashSet<>(); // Initialize the set
        allocatedClasses = new HashSet<>(); // Initialize the set

        // Retrieve coordinator data from Firestore
        retrieveCoordinatorData();

        // Retrieve class data from Firestore
        retrieveClassData();

        // Retrieve allocated coordinators from Firestore and store them in the allocatedCoordinators set
        retrieveAllocatedCoordinators();

        allocateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedClass = classSpinner.getSelectedItem().toString();
                String selectedCoordinator = coordinatorSpinner.getSelectedItem().toString();

                if (isAlreadyAllocated(selectedClass, selectedCoordinator)) {
                    Toast.makeText(AllocateCoordinatorActivity.this, "This class or coordinator is already allocated.", Toast.LENGTH_SHORT).show();
                } else {
                    String allocationKey = selectedCoordinator + "_" + selectedClass;
                    allocateCoordinator(selectedClass, selectedCoordinator);
                    addToTableAndFirestore(selectedCoordinator, selectedClass, allocationKey);
                }
            }
        });
    }

    private void retrieveCoordinatorData() {
        // Assuming coordinator data is stored under "Coordinators" collection in Firestore
        CollectionReference coordinatorCollectionRef = db.collection("Coordinators");

        coordinatorCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> coordinatorNames = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String coordinatorName = documentSnapshot.getString("name");
                    if (coordinatorName != null) {
                        coordinatorNames.add(coordinatorName);
                    }
                }

                // Update the coordinatorList and notify the adapter
                coordinatorList.clear();
                coordinatorList.addAll(coordinatorNames);
                coordinatorAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors
                Log.e("AllocateCoordinator", "Error retrieving coordinators: " + e.getMessage());
                Toast.makeText(AllocateCoordinatorActivity.this, "Failed to retrieve coordinators.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrieveClassData() {
        // Assuming class data is stored under "Classes" collection in Firestore
        CollectionReference classCollectionRef = db.collection("Classes");

        classCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> classNames = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String className = documentSnapshot.getString("name");
                    if (className != null) {
                        classNames.add(className);
                    } else {
                        // Log an error if the class name is null or empty
                        Log.e("AllocateCoordinator", "Class name is null or empty for document ID: " + documentSnapshot.getId());
                    }
                }

                // Update the classList and notify the adapter
                classList.clear();
                classList.addAll(classNames);
                classAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors
                Log.e("AllocateCoordinator", "Error retrieving classes: " + e.getMessage());
                Toast.makeText(AllocateCoordinatorActivity.this, "Failed to retrieve classes.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void retrieveAllocatedCoordinators() {
        db.collection("AllocatedCoordinators")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        allocatedCoordinators.clear(); // Clear the set before adding new keys
                        allocatedClasses.clear(); // Clear the set before adding new classes
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String coordinator = documentSnapshot.getString("coordinator");
                            String className = documentSnapshot.getString("className");
                            String allocationKey = coordinator + "_" + className;
                            allocatedCoordinators.add(allocationKey);
                            allocatedClasses.add(className);
                        }

                        // Update the table with the allocated coordinators
                        updateTableWithAllocatedCoordinators();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Log.e("AllocateCoordinator", "Error retrieving allocated coordinators: " + e.getMessage());
                        Toast.makeText(AllocateCoordinatorActivity.this, "Failed to retrieve allocated coordinators.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void allocateCoordinator(String className, String coordinatorName) {
        // Perform the allocation logic
        Toast.makeText(this, "Allocated " + coordinatorName + " to " + className, Toast.LENGTH_SHORT).show();
    }

    private void addToTableAndFirestore(String coordinatorName, String className, String allocationKey) {
        // Add coordinator and class to the table
        TableRow row = new TableRow(this);

        TextView coordinatorTextView = new TextView(this);
        coordinatorTextView.setText(coordinatorName);
        coordinatorTextView.setPadding(8, 8, 8, 8);
        row.addView(coordinatorTextView);

        TextView classTextView = new TextView(this);
        classTextView.setText(className);
        classTextView.setPadding(8, 8, 8, 8);
        row.addView(classTextView);

        TableLayout tableLayout = findViewById(R.id.tableLayout);
        tableLayout.addView(row);

        // Store coordinator and class in Firestore
        Map<String, Object> data = new HashMap<>();
        data.put("coordinator", coordinatorName);
        data.put("className", className);

        db.collection("AllocatedCoordinators").document(allocationKey).set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data added successfully
                        allocatedCoordinators.add(allocationKey); // Add the allocation key to the set
                        allocatedClasses.add(className); // Add the allocated class to the set
                        Toast.makeText(AllocateCoordinatorActivity.this, "Allocation successful!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Toast.makeText(AllocateCoordinatorActivity.this, "Failed to allocate: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateTableWithAllocatedCoordinators() {
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        tableLayout.removeAllViews(); // Clear the table before adding updated data

        // Add the header row
        TableRow headerRow = new TableRow(this);

        TextView coordinatorHeaderTextView = new TextView(this);
        coordinatorHeaderTextView.setText("Coordinator");
        coordinatorHeaderTextView.setPadding(8, 8, 8, 8);
        coordinatorHeaderTextView.setTextSize(18); // Set text size programmatically
        coordinatorHeaderTextView.setTypeface(null, Typeface.BOLD); // Set text style to bold
        headerRow.addView(coordinatorHeaderTextView);

        TextView classHeaderTextView = new TextView(this);
        classHeaderTextView.setText("Class");
        classHeaderTextView.setPadding(8, 8, 8, 8);
        classHeaderTextView.setTextSize(18); // Set text size programmatically
        classHeaderTextView.setTypeface(null, Typeface.BOLD); // Set text style to bold
        headerRow.addView(classHeaderTextView);

        tableLayout.addView(headerRow);

        // Add the updated allocated coordinators to the table
        for (String allocationKey : allocatedCoordinators) {
            String[] parts = allocationKey.split("_");
            String coordinatorName = parts[0];
            String className = parts[1];

            TableRow row = new TableRow(this);

            TextView coordinatorTextView = new TextView(this);
            coordinatorTextView.setText(coordinatorName);
            coordinatorTextView.setPadding(8, 8, 8, 8);
            coordinatorTextView.setTextSize(16); // Set text size programmatically
            row.addView(coordinatorTextView);

            TextView classTextView = new TextView(this);
            classTextView.setText(className);
            classTextView.setPadding(8, 8, 8, 8);
            classTextView.setTextSize(16); // Set text size programmatically
            row.addView(classTextView);

            tableLayout.addView(row);
        }
    }


    private boolean isAlreadyAllocated(String selectedClass, String selectedCoordinator) {
        // Check if the combination of selectedClass and selectedCoordinator is already present in the table
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        for (int i = 1; i < tableLayout.getChildCount(); i++) { // Start from 1 to skip the header row
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            TextView coordinatorTextView = (TextView) row.getChildAt(0);
            TextView classTextView = (TextView) row.getChildAt(1);
            String coordinator = coordinatorTextView.getText().toString();
            String className = classTextView.getText().toString();

            if (coordinator.equals(selectedCoordinator) || className.equals(selectedClass)) {
                return true; // Already allocated
            }
        }

        return false; // Not allocated yet
    }
}
