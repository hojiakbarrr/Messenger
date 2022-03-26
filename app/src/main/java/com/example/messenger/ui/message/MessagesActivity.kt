package com.example.messenger.ui.message

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messenger.R
import com.example.messenger.adapter.ChatsAdapter
import com.example.messenger.databinding.ActivityMessagesBinding
import com.example.messenger.model.Chat
import com.example.messenger.model.User
import com.example.messenger.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class MessagesActivity : AppCompatActivity() {
    private val binding: ActivityMessagesBinding by lazy {
        ActivityMessagesBinding.inflate(layoutInflater)
    }
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    private lateinit var userId: String
    var chats: MutableList<Chat> = ArrayList()
    private lateinit var adapter: ChatsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        toolbarInfo()

    }

    private fun sendTomessage(sender: String, receiver: String, message: String) {
        val reference = FirebaseDatabase.getInstance().reference
        val map = HashMap<String, Any>()
        map["sender"] = sender
        map["receiver"] = receiver
        map["message"] = message
        map["receiverIsSeen"] = "delivered"
        map["IsSeen"] = "NoSee"
        reference.child("Chats").push().setValue(map)
        reference.child("Users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val user = data.getValue(User::class.java)
                    if (user!!.id.equals(sender) || user.id.equals(receiver)) {
                        if (!user.hasChat) {
                            reference.child("Users").child(sender).child("hasChat").setValue(true)
                            reference.child("Users").child(receiver).child("hasChat").setValue(true)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                toast(error.message)
            }

        })
    }


    private fun getmessage(sender: String?, receiver: String, imageUrl: String?) {
        val reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                chats.clear()
                for (data in snapshot.children) {
                    val chat = data.getValue(Chat::class.java)
                    if (chat!!.receiver.equals(receiver) && chat.sender.equals(sender) ||
                        chat.receiver.equals(sender) && chat.sender.equals(receiver)
                    )
//                        if (chat.sender.equals(receiver) && chat.receiver.equals(sender) && chat.receiverIsSeen.equals("delivered")) {
//                            chat.IsSeen = "seen"
//                            val map = HashMap<String, Any>()
//                            map["receiverIsSeen"] = "IsSeen"
//                            map["IsSeen"] = "IsSeen2"
//                            reference.child("Chats").push().setValue(map)
//                        } else {
//                            chat.IsSeen = "NoSee"
//                        }
                    chats.add(chat)
                    binding.apply {
                        chatRecyclerView.layoutManager = LinearLayoutManager(this@MessagesActivity)
                        adapter = ChatsAdapter(chats, this@MessagesActivity, imageUrl.toString())
                        Log.d("Errorr", "$chats")
                        adapter.chatList = chats
                        chatRecyclerView.smoothScrollToPosition(chats.size)
                        chatRecyclerView.adapter = adapter
                    }

                }


            }

            override fun onCancelled(error: DatabaseError) {
                toast(error.message)
            }

        })
    }


    private fun toolbarInfo() {
        userId = intent.getStringExtra("chatUserId") as String
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId)


        binding.apply {
            setSupportActionBar(toolbarChat)
            supportActionBar?.title = ""
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            toolbarChat.setNavigationOnClickListener {
                finish()
            }

            reference!!.addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user: User? = snapshot.getValue(User::class.java)
                    usernameChat.text = user?.userName
                    if (user?.imageUrl == "default") {
                        profileImage.setImageResource(R.drawable.ic_person)
                    } else {
                        Picasso.get().load(user?.imageUrl).into(profileImage)
                    }

                    /**
                     * вызов метода читать сообщения с firebase
                     */
                    getmessage(firebaseUser?.uid, userId, user?.imageUrl)
                    /**
                     *вызов метода отправить сообщения
                     */
                    binding.sendBtn.setOnClickListener {
                        val message = messageText.text
                        if (message.isNullOrEmpty()) {
                            toast("введите ссобщение")
                        } else {
                            sendTomessage(firebaseUser!!.uid, userId, message.toString())
                            messageText.text.clear()
                            getmessage(firebaseUser?.uid, userId, user?.imageUrl)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    toast(error.message)
                }
            })
        }
    }
}