package com.theelitedevelopers.academia.modules.main.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.theelitedevelopers.academia.core.utils.AppUtils;
import com.theelitedevelopers.academia.databinding.ActivityChatBinding;
import com.theelitedevelopers.academia.modules.main.chat.adapters.ChatAdapter;
import com.theelitedevelopers.academia.modules.main.data.models.Chat;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    ChatAdapter adapter;
    ArrayList<Chat> chatArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.inboxRecyclerView.setLayoutManager(layoutManager);
        binding.inboxRecyclerView.setHasFixedSize(true);

        adapter = new ChatAdapter(this, chatArrayList);
        binding.inboxRecyclerView.setAdapter(adapter);

        binding.send.setOnClickListener(v -> {
            if(binding.sendAMessageEditText.getText().length() > 0){
                populateDummyData(binding.sendAMessageEditText.getText().toString());
            }
            binding.sendAMessageEditText.getText().clear();
        });
    }


    public void populateDummyData(String message){
        String date  = AppUtils.Companion.convertDateFromOneFormatToAnother("EEE MMM d HH:mm:ss z yyyy", "EEE, d MMM yyyy HH:mm:ss", new Date().toString());
        assert date != null;
        chatArrayList.add(new Chat("1", message, AppUtils.Companion.getInboxDate(date)));
        chatArrayList.add(new Chat("2", message, AppUtils.Companion.getInboxDate(date)));

        adapter.setList(chatArrayList);
    }
}