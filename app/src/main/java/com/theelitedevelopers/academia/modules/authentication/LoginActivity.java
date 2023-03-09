package com.theelitedevelopers.academia.modules.authentication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.theelitedevelopers.academia.core.data.local.SharedPref;
import com.theelitedevelopers.academia.core.utils.AppUtils;
import com.theelitedevelopers.academia.core.utils.Constants;
import com.theelitedevelopers.academia.modules.add_announcements_assignments.AddAnnouncementsAssignmentsActivity;
import com.theelitedevelopers.academia.modules.authentication.data.models.Student;
import com.theelitedevelopers.academia.modules.main.MainActivity;
import com.theelitedevelopers.academia.databinding.ActivityLoginBinding;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    Student student = new Student();
    String firebaseToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginNowText.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

        if(!SharedPref.getInstance(getApplicationContext()).getBoolean(Constants.SUBSCRIBED)){
            subscribeToTopics();
        }
        binding.logInButton.setOnClickListener(v -> {

            String email = binding.email.getText().toString();
            String password = binding.password.getText().toString();

            if(email.length() > 0 && password.length() > 0){
                binding.progressBar.setVisibility(View.VISIBLE);

                Student student = new Student();
                student.setEmail(email);
                student.setPassword(password);

                loginStudent(student);
            }
        });

    }

    private void loginStudent(Student student){
        getToken();

        firebaseAuth.signInWithEmailAndPassword(student.getEmail(), student.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.progressBar.setVisibility(View.GONE);

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            //fetch Student details from Database
                            getStudentDetails(user.getUid());

                        } else {
                            binding.progressBar.setVisibility(View.GONE);

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getStudentDetails(String uid){
        database.collection("students")
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        student = task.getResult().getDocuments().get(0).toObject(Student.class);
                        if(student != null){
                            student.setId(task.getResult().getDocuments().get(0).getId());
                            updateStudentDetailsWithToken(student);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void updateStudentDetailsWithToken(Student student){
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
        studentMap.put("token", firebaseToken);

        database.collection("students")
                .document(student.getId())
                .set(studentMap)
                .addOnSuccessListener(documentReference ->{
                    AppUtils.Companion.saveDataToSharedPref(LoginActivity.this, student);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finishAffinity();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
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
                    Toast.makeText(LoginActivity.this, token[0], Toast.LENGTH_SHORT).show();
                });
    }

    private void subscribeToTopics(){
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.ASSIGNMENT_TOPIC).addOnSuccessListener(aVoid -> {
        });

        FirebaseMessaging.getInstance().subscribeToTopic(Constants.ANNOUNCEMENT_TOPIC).addOnSuccessListener(aVoid -> {
        });

        FirebaseMessaging.getInstance().subscribeToTopic(Constants.CHAT_TOPIC).addOnSuccessListener(aVoid -> {
            SharedPref.getInstance(getApplicationContext()).saveBoolean(Constants.SUBSCRIBED, true);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        }
    }
}