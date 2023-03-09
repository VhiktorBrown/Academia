package com.theelitedevelopers.academia.modules.main.home.announcements;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.theelitedevelopers.academia.databinding.ActivityAnnouncementsBinding;
import com.theelitedevelopers.academia.modules.main.data.models.Announcement;
import com.theelitedevelopers.academia.modules.main.data.models.Assignment;
import com.theelitedevelopers.academia.modules.main.data.models.Chat;
import com.theelitedevelopers.academia.modules.main.home.announcements.adapters.AnnouncementListAdapter;

import java.util.ArrayList;

public class AnnouncementsActivity extends AppCompatActivity {
    ActivityAnnouncementsBinding binding;
    AnnouncementListAdapter adapter;
    ArrayList<Announcement> announcements = new ArrayList<>();
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnnouncementsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.announcementRecyclerView.setLayoutManager(layoutManager);
        binding.announcementRecyclerView.setHasFixedSize(true);

        //populateDummyAnnouncements();
        fetchAnnouncements();

        adapter = new AnnouncementListAdapter(this, announcements);
        binding.announcementRecyclerView.setAdapter(adapter);

        binding.goBack.setOnClickListener(view -> onBackPressed());
    }

    private void fetchAnnouncements(){
//        database.collection("announcements")
//                .orderBy("date", Query.Direction.DESCENDING)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        if(!task.getResult().isEmpty()){
//                            announcements.clear();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                announcements.add(document.toObject(Announcement.class));
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                            adapter.setList(announcements);
//                        }
//                    } else {
//                        Log.d(TAG, "Error getting documents: ", task.getException());
//                    }
//                });

        database.collection("announcements")
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if(!value.getDocuments().isEmpty()) {
                        announcements.clear();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            announcements.add(documentSnapshot.toObject(Announcement.class));
                        }
                        adapter.setList(announcements);
                    }
                });
    }

//    private void populateDummyAnnouncements(){
//        announcements.add(new Announcement("General Meeting", "There will be a general meeting of all CSC students in all levels on Monday 23rd of MNarch 2023 at Final Year classroom by 11:00am", "Kyrian Kosisochukwu", "09 March"));
//        announcements.add(new Announcement("General Meeting", "There will be a general meeting of all CSC students in all levels on Monday 23rd of MNarch 2023 at Final Year classroom by 11:00am", "Kyrian Kosisochukwu", "09 March"));
//        announcements.add(new Announcement("General Meeting", "There will be a general meeting of all CSC students in all levels on Monday 23rd of MNarch 2023 at Final Year classroom by 11:00am", "Kyrian Kosisochukwu", "09 March"));
//        announcements.add(new Announcement("General Meeting", "There will be a general meeting of all CSC students in all levels on Monday 23rd of MNarch 2023 at Final Year classroom by 11:00am", "Kyrian Kosisochukwu", "09 March"));
//        announcements.add(new Announcement("General Meeting", "There will be a general meeting of all CSC students in all levels on Monday 23rd of MNarch 2023 at Final Year classroom by 11:00am", "Kyrian Kosisochukwu", "09 March"));
//        announcements.add(new Announcement("General Meeting", "There will be a general meeting of all CSC students in all levels on Monday 23rd of MNarch 2023 at Final Year classroom by 11:00am", "Kyrian Kosisochukwu", "09 March"));
//    }
}