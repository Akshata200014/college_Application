package com.example.collegeapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
public class MainActivity extends AppCompatActivity {
    private ImageButton button1;
    private ImageButton button2;
    private ImageButton button3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.coordinator); // Initialize the button
        button2 = findViewById(R.id.admin);
        button3 = findViewById(R.id.faculty);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCoordinatorLoginActivity();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAdminLoginActivity();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFacultyLoginActivity();
            }
        });
    }

    public void openCoordinatorLoginActivity() {
        Intent intent = new Intent(this, CoordinatorLoginActivity.class);
        startActivity(intent);
    }

    public void openAdminLoginActivity() {
        Intent intent = new Intent(this, AdminLoginActivity.class);
        startActivity(intent);
    }

    public void openFacultyLoginActivity() {
        Intent intent = new Intent(this, FacultyLoginActivity.class);
        startActivity(intent);
    }
}
