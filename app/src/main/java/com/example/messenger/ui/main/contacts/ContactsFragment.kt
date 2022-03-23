package com.example.messenger.ui.main.contacts

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.messenger.R
import com.example.messenger.adapter.ContactsAdapter
import com.example.messenger.databinding.ContactsFragmentBinding
import com.example.messenger.databinding.ProfileFragmentBinding
import com.example.messenger.model.User
import com.example.messenger.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ContactsFragment : Fragment() {

    companion object {
        fun newInstance() = ContactsFragment()
    }

    private val binding: ContactsFragmentBinding by lazy {
        ContactsFragmentBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: ContactsViewModel
    private var auth: FirebaseAuth? = null
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    private var adapter = ContactsAdapter()
    private var users: MutableList<User> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContactsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firebaseUser = auth!!.currentUser

        reference = FirebaseDatabase.getInstance().getReference("Users")
        reference!!.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    val user = data.getValue(User::class.java)
                    if (user?.id != firebaseUser!!.uid){
                        users!!.add(user!!)
                        Log.d("user", "$users")
                        binding.apply {
                            recContacts.layoutManager = LinearLayoutManager(requireContext())
                            adapter = ContactsAdapter()
                            adapter.contactList = users!!
                            recContacts.adapter = adapter
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                toast(error.toString())
            }

        })


    }
}