package com.theelitedevelopers.academia.modules.main.chat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.theelitedevelopers.academia.databinding.ChatInLayoutBinding
import com.theelitedevelopers.academia.databinding.ChatOutLayoutBinding
import com.theelitedevelopers.academia.modules.main.data.models.Chat
import com.theelitedevelopers.academia.R

class ChatAdapter(var context : Context, var messageList : ArrayList<Chat>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val RECEIVE = 0
    val SEND = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == 0){
            //inflate the Received Layout
            val binding : ChatInLayoutBinding = ChatInLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return ReceivedViewHolder(binding)
        }else {
            //inflate the Send Layout
            val binding : ChatOutLayoutBinding = ChatOutLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            return SentViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.javaClass == SentViewHolder::class.java){
            val viewHolder = holder as SentViewHolder

            //set the message
            holder.binding.inboxOutMessage.text = messageList[position].message

            holder.binding.inboxOutDate.text = messageList[position].date!!

        }else {
            val viewHolder = holder as ReceivedViewHolder
            Picasso.get()
                .load(messageList[position].image)
                .placeholder(R.drawable.academia_profile)
                .into(holder.binding.inboxImage)

            //set the message
            holder.binding.inboxInMessage.text = messageList[position].message

            holder.binding.inboxInDate.text = messageList[position].date!!

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if("1" == messageList[position].senderId){
            SEND
        }else {
            RECEIVE
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    fun setList(messageList: ArrayList<Chat>){
        this.messageList = messageList;
        notifyDataSetChanged()
    }

    class SentViewHolder(var binding : ChatOutLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    class ReceivedViewHolder(var binding : ChatInLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}