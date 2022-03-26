package com.example.messenger.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.messenger.R
import com.example.messenger.adapter.ViewPagerAdapter_Mian
import com.example.messenger.databinding.ActivityMainBinding
import com.example.messenger.model.User
import com.example.messenger.ui.register.RegisterActivity
import com.example.messenger.utils.toast
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    val auth = Firebase.auth
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar()
        setContentView(binding.root)
        actionBar?.hide()
        actionBar?.show()

        binding.apply {

            viewPagerMain.adapter = ViewPagerAdapter_Mian(supportFragmentManager,lifecycle)

            TabLayoutMediator(tabLayoutMain, viewPagerMain) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "ЧАТ"
                    }
                    1 -> {
                        tab.text = "КОНТАКТЫ"
                    }
                    2 -> {
                        tab.text = "ПРОФИЛЬ"
                    }
                }
            }.attach()
        }
        binding.logoutBtn.setOnClickListener {
            val userId = auth.currentUser?.uid as String
            FirebaseDatabase.getInstance().getReference("Users").child(userId).child("status").setValue("offline")
            auth.signOut()
            toast("Good bye")
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun toolbar() {
        firebaseUser = auth!!.currentUser

        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)
        reference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                binding.apply {
                    username.text = user!!.userName
                    if (user.imageUrl== "default") {
                        binding.profileImage.setImageResource(R.drawable.ic_person)
                    } else {
                        Picasso.get().load(user.imageUrl).into(binding.profileImage)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()


    }

}