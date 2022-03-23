package com.example.messenger.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.messenger.adapter.ViewPagerAdapter_Mian
import com.example.messenger.databinding.ActivityMainBinding
import com.example.messenger.ui.register.RegisterActivity
import com.google.android.material.tabs.TabLayoutMediator
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
            auth.signOut()
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        val userId = intent.getSerializableExtra("user_id")
        val mailId = intent.getSerializableExtra("email_id")

    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()


    }

}