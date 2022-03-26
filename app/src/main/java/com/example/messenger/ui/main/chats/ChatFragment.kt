package com.example.messenger.ui.main.chats

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messenger.R
import com.example.messenger.adapter.ContactsAdapter
import com.example.messenger.adapter.ItemClickListener
import com.example.messenger.databinding.ChatFragmentBinding
import com.example.messenger.databinding.ContactsFragmentBinding
import com.example.messenger.model.User
import com.example.messenger.ui.message.MessagesActivity
import com.example.messenger.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ChatFragment : Fragment(), ItemClickListener {

    companion object {
        fun newInstance() = ChatFragment()
    }

    private lateinit var viewModel: ChatViewModel
    private val binding: ChatFragmentBinding by lazy {
        ChatFragmentBinding.inflate(layoutInflater)
    }
    private var auth: FirebaseAuth? = null
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    private var adapter = ContactsAdapter(this)
    private var users: MutableList<User> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firebaseUser = auth!!.currentUser

        reference = FirebaseDatabase.getInstance().getReference("Users")
        reference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    val user = data.getValue(User::class.java)
                    if (user?.id != firebaseUser!!.uid && user!!.hasChat){
                        users!!.add(user!!)
                        Log.d("user", "$users")
                        binding.apply {
                            adapter = ContactsAdapter(this@ChatFragment)
                            recChats.layoutManager = LinearLayoutManager(requireContext())
                            adapter.contactList = users!!
                            recChats.adapter = adapter
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                toast(error.toString())
            }

        })
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(activity, MessagesActivity::class.java)
        intent.putExtra("chatUserId", users[position].id)
        startActivity(intent)
    }

}