package com.theelitedevelopers.academia.modules.main.chat;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.theelitedevelopers.academia.core.data.local.SharedPref;
import com.theelitedevelopers.academia.core.utils.Constants;
import com.theelitedevelopers.academia.databinding.ActivityStartNewChatBinding;
import com.theelitedevelopers.academia.modules.authentication.data.models.Student;
import com.theelitedevelopers.academia.modules.main.chat.adapters.ChatAdapter;
import com.theelitedevelopers.academia.modules.main.chat.adapters.StartNewChatAdapter;
import com.theelitedevelopers.academia.modules.main.data.models.Chat;

import java.util.ArrayList;

public class StartNewChatActivity extends AppCompatActivity {
    ActivityStartNewChatBinding binding;
    StartNewChatAdapter adapter;
    ArrayList<Student> students = new ArrayList<>();
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartNewChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
    }

    private void initViews(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.newChatRecyclerView.setLayoutManager(layoutManager);
        binding.newChatRecyclerView.setHasFixedSize(true);

        adapter = new StartNewChatAdapter(this, students);
        binding.newChatRecyclerView.setAdapter(adapter);

        binding.goBack.setOnClickListener(v -> onBackPressed());

        database.collection("students")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if(!task.getResult().isEmpty()){
                            students.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(!document.toObject(Student.class).getUid().equals(SharedPref.getInstance(getApplicationContext()).getString(Constants.UID))){
                                    students.add(document.toObject(Student.class));
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                            }
                            adapter.setList(students);
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}