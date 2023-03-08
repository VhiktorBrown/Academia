package com.theelitedevelopers.academia.modules.main.chat.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.theelitedevelopers.academia.R
import com.theelitedevelopers.academia.core.data.local.SharedPref
import com.theelitedevelopers.academia.core.utils.AppUtils
import com.theelitedevelopers.academia.core.utils.AppUtils.Companion.fromTimeStampToString
import com.theelitedevelopers.academia.core.utils.Constants
import com.theelitedevelopers.academia.databinding.ChatInLayoutBinding
import com.theelitedevelopers.academia.databinding.ChatOutLayoutBinding
import com.theelitedevelopers.academia.modules.main.data.models.Chat
import java.text.ParseException

class ChatAdapter(var context : Context, var messageList : ArrayList<Chat>, var receiverUid : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

            holder.binding.inboxOutDate.text = AppUtils.getSingleInboxDate(
                fromTimeStampToString(messageList[position].date!!.seconds)
            )

            /* Here, we want to check the next date
             * after this position. If it is the same
             * with this, we'll make this date GONE
             */if (position >= 0) {
                //initialize next position
                val nextPosition = position + 1
                if (nextPosition < messageList.size) {
                    if (messageList[position].uid
                            .equals(SharedPref.getInstance(context).getString(Constants.UID))
                            && messageList[nextPosition].uid
                            .equals(SharedPref.getInstance(context).getString(Constants.UID))
                    ) {
                        try {
                            if (AppUtils.convertToDateWithoutSeconds(
                                    fromTimeStampToString(
                                        messageList[position].date.seconds
                                    )
                                )?.equals(
                                    AppUtils.convertToDateWithoutSeconds(
                                        fromTimeStampToString(
                                            messageList[nextPosition].date.seconds
                                        )
                                    )
                                ) == true
                            ) {
                                holder.binding.inboxOutDate.visibility = View.GONE
                            } else {
                                holder.binding.inboxOutDate.visibility = View.VISIBLE
                            }
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                    } else {
                        holder.binding.inboxOutDate.visibility = View.VISIBLE
                    }
                }
            }

        }else {
            val viewHolder = holder as ReceivedViewHolder
            Picasso.get()
                .load(messageList[position].image)
                .placeholder(R.drawable.academia_profile)
                .into(holder.binding.inboxImage)

            //set the message
            holder.binding.inboxInMessage.text = messageList[position].message

            holder.binding.inboxInDate.text = AppUtils.getSingleInboxDate(
                fromTimeStampToString(messageList[position].date!!.seconds)
            )


            /* Here, we want to check the next date
             * after this position. If it is the same
             * with this, we'll make this date GONE
             */if (position >= 0) {
                //initialize next position
                val nextPosition = position + 1
                if (nextPosition < messageList.size) {
                    if (messageList[position].uid
                            .equals(receiverUid) && messageList[nextPosition].uid
                            .equals(receiverUid)
                    ) {
                        try {
                            if (AppUtils.convertToDateWithoutSeconds(
                                    fromTimeStampToString(
                                        messageList[position].date.seconds
                                    )
                                )?.equals(
                                    AppUtils.convertToDateWithoutSeconds(
                                        fromTimeStampToString(
                                            messageList[nextPosition].date.seconds
                                        )
                                    )
                                ) == true
                            ) {
                                holder.binding.inboxInDate.visibility = View.GONE
                            } else {
                                holder.binding.inboxInDate.visibility = View.VISIBLE
                            }
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                    } else {
                        holder.binding.inboxInDate.visibility = View.VISIBLE
                    }
                }
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(SharedPref.getInstance(context).getString(Constants.UID) == messageList[position].uid){
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