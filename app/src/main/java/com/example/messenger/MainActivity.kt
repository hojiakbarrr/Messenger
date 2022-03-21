package com.example.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.messenger.databinding.ActivityMainBinding
import com.example.messenger.databinding.SignInFragmentBinding
import com.example.messenger.ui.register.RegisterActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
val auth = Firebase.auth
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()
        setSupportActionBar(binding.toolbar)

        binding.logout.setOnClickListener {
            auth.signOut()
            val intent =Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


}