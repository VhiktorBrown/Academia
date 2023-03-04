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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.theelitedevelopers.academia.core.data.local.SharedPref;
import com.theelitedevelopers.academia.core.utils.Constants;
import com.theelitedevelopers.academia.modules.add_announcements_assignments.AddAnnouncementsAssignmentsActivity;
import com.theelitedevelopers.academia.modules.authentication.data.models.Student;
import com.theelitedevelopers.academia.modules.main.MainActivity;
import com.theelitedevelopers.academia.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    Student student = new Student();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginNowText.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

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
                            saveDataToSharedPref(student);
                            //If student is the course rep, then open a different Activity
                            if(student.getRep()){
                                startActivity(new Intent(LoginActivity.this, AddAnnouncementsAssignmentsActivity.class));
                                finishAffinity();
                            } else {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finishAffinity();
                            }
                            finishAffinity();
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            if(SharedPref.getInstance(getApplicationContext()).getBoolean(Constants.REP)){
                startActivity(new Intent(this, AddAnnouncementsAssignmentsActivity.class));
                finishAffinity();
            }else {
                startActivity(new Intent(this, MainActivity.class));
                finishAffinity();
            }
        }
    }
}