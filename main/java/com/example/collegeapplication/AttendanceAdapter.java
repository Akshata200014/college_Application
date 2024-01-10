package com.example.collegeapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends BaseAdapter {

    private Context context;
    private List<Student> students;
    private LayoutInflater inflater;
    private DatabaseReference studentsRef;

    public AttendanceAdapter(Context context) {
        this.context = context;
        students = new ArrayList<>();
        inflater = LayoutInflater.from(context);

        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        studentsRef = database.getReference("Students");

        // Read data from Firebase Realtime Database
        studentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the current student list
                students.clear();

                // Loop through the dataSnapshot to get all students
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    // Get student data from dataSnapshot
                    String rollNo = studentSnapshot.child("/Students/0/ROLL NO").getValue(String.class);
                    String name = studentSnapshot.child("/Students/0/STUDENTNAME").getValue(String.class);
                    Boolean isAttendedValue = studentSnapshot.child("isAttended").getValue(Boolean.class);

                    // Add other fields like "STUDENTCLASS", "STUDENTEMAIL", "MOBILENO" as needed

                    // Create a Student object and add it to the list
                    Student student = new Student(rollNo, name);

                    // Check if the 'isAttendedValue' is not null before accessing it
                    if (isAttendedValue != null) {
                        student.setAttended(isAttendedValue);
                    } else {
                        // Handle the case when 'isAttendedValue' is null (set default value if needed)
                        student.setAttended(false); // Assuming the default value is 'false'
                    }

                    // Add other fields to the Student object as needed
                    students.add(student);
                }

                // Notify the adapter that the data has changed
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database read error if needed
            }
        });
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_student, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.rollNoTextView = convertView.findViewById(R.id.rollNoTextView);
            viewHolder.nameTextView = convertView.findViewById(R.id.nameTextView);
            viewHolder.checkBox = convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Updated: Since keys start from "0", use "String.valueOf(position)" as the key
        String key = String.valueOf(position);
        Student student = students.get(position);
        viewHolder.rollNoTextView.setText(student.getRollNo());
        viewHolder.nameTextView.setText(student.getName());

        // Set the checkbox state based on the student's attendance
        viewHolder.checkBox.setChecked(student.isAttended());

        // Set a listener to update the student's attendance when the checkbox is clicked
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                student.setAttended(checkBox.isChecked());

                // Update the attendance data in Firebase Realtime Database with the key
                studentsRef.child(key).child("isAttended").setValue(checkBox.isChecked());
            }
        });

        return convertView;
    }

    // Method to retrieve the attendance data
    public List<Student> getAttendanceData() {
        return students;
    }

    // View holder pattern to optimize the list view performance
    private static class ViewHolder {
        TextView rollNoTextView;
        TextView nameTextView;
        CheckBox checkBox;
    }
}

