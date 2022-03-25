package com.example.messenger.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.messenger.model.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import com.example.messenger.R
import com.example.messenger.model.User


class ChatsAdapter(chatList: List<Chat>, context: Context, imageUrl: String) :
    RecyclerView.Adapter<ChatsAdapter.Holder>() {


    var chatList: List<Chat> = chatList
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    private val message_sender = 0
    private val message_receiver = 1
    private var context: Context? = context
    var imageUrl: String? = imageUrl
    var firebaseUser: FirebaseUser? = null

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var show_message: TextView
        var check_image_chat: ImageView? = null
        var profile_image_chat: CircleImageView? = null

        init {
            show_message = itemView.findViewById(R.id.show_message)
            profile_image_chat = itemView.findViewById(R.id.user_avatar)
            check_image_chat = itemView.findViewById(R.id.status_read)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View
        if (viewType == message_sender) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_sender_layout, parent, false)
        } else {
            view =
                LayoutInflater.from(context).inflate(R.layout.chat_receiver_layout, parent, false)
        }
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val chat = chatList[position]
        holder.show_message.setText(chat.message)
        if (imageUrl == "") {
            Log.d("Errorr33", "$imageUrl")
            holder.profile_image_chat?.setImageResource(R.drawable.ic_person)
        } else {
            try {
                Picasso.get().load(imageUrl).into(holder.profile_image_chat!!)
            } catch (e: Exception) {
            }
        }
        if (position == chatList.size - 1) {
            if (chat.IsSeen.equals("seen")) {
                holder.check_image_chat?.setImageResource(R.drawable.read)
            } else {
                holder.check_image_chat?.setImageResource(R.drawable.noread)
            }
        } else {
            holder.check_image_chat?.setImageResource(R.drawable.noread)
        }
    }

    override fun getItemCount(): Int = chatList.size


    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        return if (chatList[position].sender.equals(firebaseUser?.uid)) {
            message_sender
        } else {
            message_receiver
        }
    }
}