package com.theelitedevelopers.academia.modules.main.chat.fragments;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theelitedevelopers.academia.R;
import com.theelitedevelopers.academia.databinding.FragmentChatBinding;
import com.theelitedevelopers.academia.modules.main.chat.ChatActivity;
import com.theelitedevelopers.academia.modules.main.chat.adapters.ChatAdapter;
import com.theelitedevelopers.academia.modules.main.chat.adapters.ChatListAdapter;
import com.theelitedevelopers.academia.modules.main.data.models.Chat;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    FragmentChatBinding binding;
    ChatListAdapter adapter;
    ArrayList<Chat> chatArrayList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentChatBinding.inflate(inflater, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        binding.inboxRecyclerView.setLayoutManager(linearLayoutManager);

        binding.inboxRecyclerView.setHasFixedSize(true);
        adapter = new ChatListAdapter(requireContext(), setUpDummyArrayList());
        binding.inboxRecyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private ArrayList<Chat> setUpDummyArrayList(){
        chatArrayList.add(new Chat("Regina Johnsons", "Hey Gina, have you done your CSC 411 assignment? It's really giving me a hard time."));
        chatArrayList.add(new Chat("Chinonso Chukwudi", "I might come in to school pretty late today. I have a few emergencies to attend to."));
        chatArrayList.add(new Chat("Sophia Jennings", "Can I get your note on Rete Algorithm? It's important."));

        return chatArrayList;
    }
}