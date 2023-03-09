package com.theelitedevelopers.academia.modules.authentication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.theelitedevelopers.academia.core.utils.AppUtils;
import com.theelitedevelopers.academia.databinding.ActivityRegisterBinding;
import com.theelitedevelopers.academia.modules.authentication.data.models.Student;
import com.theelitedevelopers.academia.modules.main.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    String firebaseToken;

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
                student.setRep(false);
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
            studentMap.put("rep", false);
            studentMap.put("phoneNumber", student.getPhoneNumber());
            studentMap.put("gender", student.getGender());
            studentMap.put("department", student.getDepartment());
            studentMap.put("dateOfBirth", student.getDateOfBirth());
            studentMap.put("uid", student.getUid());
            studentMap.put("photoUrl", student.getPhotoUrl());
            studentMap.put("token", firebaseToken);

            // Add a new document with a generated ID
            database.collection("students")
                    .add(studentMap)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            //Task<DocumentSnapshot> snapshot = documentReference.get();
                            student.setId(documentReference.getId());
                            AppUtils.Companion.saveDataToSharedPref(RegisterActivity.this, student);
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finishAffinity();

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

        private void getToken(){
        final String[] token = {""};
            FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    token[0] = task.getResult();
                    firebaseToken = token[0];
                    Log.d(TAG, token[0]);
                });

        }

    @Override
    protected void onStart() {
        super.onStart();
        getToken();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}