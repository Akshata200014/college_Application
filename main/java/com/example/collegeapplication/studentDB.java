package com.example.collegeapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class studentDB extends AppCompatActivity {

    private static final int READ_REQUEST_CODE = 42;
    private static final int PERMISSION_REQUEST_CODE = 43;

    private Button selectButton;
    private Button uploadButton;
    private TextView selectedFileTextView;

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri selectedFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_db);

        selectButton = findViewById(R.id.selectButton);
        uploadButton = findViewById(R.id.uploadButton);
        selectedFileTextView = findViewById(R.id.selectedFileTextView);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestFilePermissions();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFileUri != null) {
                    uploadFile(selectedFileUri);
                } else {
                    Toast.makeText(studentDB.this, "No file selected.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestFilePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFilePicker();
            } else {
                Toast.makeText(this, "Permission denied. Cannot access files.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                selectedFileUri = data.getData();
                if (selectedFileUri != null) {
                    try {
                        displaySelectedFile(selectedFileUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void displaySelectedFile(Uri fileUri) throws IOException {
        String fileId = fileUri.getLastPathSegment();
        String fileUrl = "https://drive.google.com/file/d/" + fileId + "/view?usp=drivesdk";

        String fileName = getFileName(fileUri);
        selectedFileTextView.setText("Selected File: " + fileName);
        selectedFileTextView.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Selected file: " + fileName, Toast.LENGTH_SHORT).show();
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex != -1) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    private void uploadFile(Uri fileUri) {
        // Create a file reference
        String fileName = getFileName(fileUri);
        StorageReference fileRef = storageRef.child("uploads/" + fileName);

        // Start the upload
        fileRef.putFile(fileUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // File upload is successful
                        Toast.makeText(studentDB.this, "File uploaded successfully.", Toast.LENGTH_SHORT).show();

                        // You can now retrieve the download URL
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                // Handle the download URL as needed
                                String downloadUrl = downloadUri.toString();
                                // TODO: Do something with the download URL
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // File upload failed
                        Toast.makeText(studentDB.this, "File upload failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

