package com.theelitedevelopers.academia.modules.main.chat.fragments;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.theelitedevelopers.academia.R;
import com.theelitedevelopers.academia.core.data.local.SharedPref;
import com.theelitedevelopers.academia.core.utils.Constants;
import com.theelitedevelopers.academia.databinding.FragmentChatBinding;
import com.theelitedevelopers.academia.modules.main.chat.ChatActivity;
import com.theelitedevelopers.academia.modules.main.chat.StartNewChatActivity;
import com.theelitedevelopers.academia.modules.main.chat.adapters.ChatAdapter;
import com.theelitedevelopers.academia.modules.main.chat.adapters.ChatListAdapter;
import com.theelitedevelopers.academia.modules.main.data.models.Assignment;
import com.theelitedevelopers.academia.modules.main.data.models.Chat;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    FragmentChatBinding binding;
    ChatListAdapter adapter;
    ArrayList<Chat> chatArrayList = new ArrayList<>();
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentChatBinding.inflate(inflater, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.inboxRecyclerView.setLayoutManager(linearLayoutManager);

        binding.inboxRecyclerView.setHasFixedSize(true);
        adapter = new ChatListAdapter(requireContext(), chatArrayList);
        binding.inboxRecyclerView.setAdapter(adapter);

        binding.newChat.setOnClickListener(v -> startActivity(new Intent(requireActivity(), StartNewChatActivity.class)));
        fetchChatHistory();

        return binding.getRoot();
    }

    private void fetchChatHistory(){
//        database.collection(SharedPref.getInstance(requireActivity()).getString(Constants.UID)+"history")
//                .orderBy("date", Query.Direction.DESCENDING)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        if(!task.getResult().isEmpty()){
//                            chatArrayList.clear();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                chatArrayList.add(document.toObject(Chat.class));
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                            adapter.setList(chatArrayList);
//                        }
//                    } else {
//                        Log.d(TAG, "Error getting documents: ", task.getException());
//                    }
//                });

        database.collection(SharedPref.getInstance(requireActivity()).getString(Constants.UID)+"history")
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    assert value != null;
                    if(!value.getDocuments().isEmpty()) {
                        binding.noDataLayout.setVisibility(View.GONE);
                        chatArrayList.clear();
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            chatArrayList.add(documentSnapshot.toObject(Chat.class));
                        }
                        adapter.setList(chatArrayList);
                    }else {
                        binding.noDataLayout.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchChatHistory();
    }

}