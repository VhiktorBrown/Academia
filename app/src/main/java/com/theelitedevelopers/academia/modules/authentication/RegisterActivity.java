package com.theelitedevelopers.academia.modules.authentication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theelitedevelopers.academia.core.data.local.SharedPref;
import com.theelitedevelopers.academia.core.utils.Constants;
import com.theelitedevelopers.academia.databinding.ActivityRegisterBinding;
import com.theelitedevelopers.academia.modules.authentication.data.models.Student;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.logInButton.setOnClickListener(v -> {
            String fullName = binding.name.getText().toString();
            String email = binding.email.getText().toString();
            String regNumber = binding.regNumber.getText().toString();
            String password = binding.password.getText().toString();
            String hostel = binding.hostel.getText().toString();

            if(fullName.length() > 1 && email.length() > 5 && regNumber.length() > 9 &&
            password.length() > 5 && hostel.length() > 5) {
                Student student = new Student();
                student.setFullName(fullName);
                student.setEmail(email);
                student.setRegNumber(regNumber);
                student.setPassword(password);
                student.setHostel(hostel);
                student.setDepartment("Computer Science");
                student.setDateOfBirth("Mon, 04 Feb 2002 17:55:01");
                student.setGender("Male");
                student.setPhoneNumber("0807635263");
                student.setLevel("400 Level");
                student.setPhotoUrl("");

                binding.progressBar.setVisibility(View.VISIBLE);
                createStudent(student);
            }
        });
    }

    private void createStudent(Student student){
        firebaseAuth.createUserWithEmailAndPassword(student.getEmail(), student.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            binding.progressBar.setVisibility(View.GONE);

                            Toast.makeText(RegisterActivity.this, "Student successfully registered", Toast.LENGTH_SHORT).show();
                            saveStudentDetailsToDB(student, user);
                        } else {
                            binding.progressBar.setVisibility(View.GONE);

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }

        private void saveStudentDetailsToDB(Student student, FirebaseUser firebaseUser){
            student.setUid(firebaseUser.getUid());

            Map<String, Object> studentMap = new HashMap<>();
            studentMap.put("fullName", student.getFullName());
            studentMap.put("email", student.getEmail());
            studentMap.put("regNumber", student.getRegNumber());
            studentMap.put("password", student.getPassword());
            studentMap.put("hostel", student.getHostel());
            studentMap.put("level", student.getLevel());
            studentMap.put("phoneNumber", student.getPhoneNumber());
            studentMap.put("gender", student.getGender());
            studentMap.put("department", student.getDepartment());
            studentMap.put("dateOfBirth", student.getDateOfBirth());
            studentMap.put("uid", student.getUid());
            studentMap.put("photoUrl", student.getPhotoUrl());

            // Add a new document with a generated ID
            database.collection("students")
                    .add(studentMap)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            //Task<DocumentSnapshot> snapshot = documentReference.get();
                            student.setId(documentReference.getId());
                            saveDataToSharedPref(student);

                            Toast.makeText(RegisterActivity.this, "Student data saved to DB", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }

        private void saveDataToSharedPref(Student student){
            SharedPref.getInstance(this).saveString(Constants.ID, student.getId());
            SharedPref.getInstance(this).saveString(Constants.NAME, student.getFullName());
            SharedPref.getInstance(this).saveString(Constants.EMAIL, student.getEmail());
            SharedPref.getInstance(this).saveString(Constants.REG_NUMBER, student.getRegNumber());
            SharedPref.getInstance(this).saveString(Constants.DATE_OF_BIRTH, student.getDateOfBirth());
            SharedPref.getInstance(this).saveString(Constants.DEPARTMENT, student.getDepartment());
            SharedPref.getInstance(this).saveString(Constants.HOSTEL, student.getHostel());
            SharedPref.getInstance(this).saveString(Constants.LEVEL, student.getLevel());
            SharedPref.getInstance(this).saveBoolean(Constants.REP, student.getRep());
            SharedPref.getInstance(this).saveString(Constants.PHONE_NUMBER, student.getPhoneNumber());
            SharedPref.getInstance(this).saveString(Constants.GENDER, student.getGender());
            SharedPref.getInstance(this).saveString(Constants.UID, student.getUid());
        }
    }