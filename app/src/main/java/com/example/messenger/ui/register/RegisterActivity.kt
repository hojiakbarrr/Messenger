package com.example.messenger.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.messenger.ui.main.MainActivity
import com.example.messenger.R
import com.example.messenger.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val tablayout=findViewById<TabLayout>(R.id.tab_layout)
        val viewPager2=findViewById<ViewPager2>(R.id.view_pager_2)
        val adapter= ViewPagerAdapter(supportFragmentManager,lifecycle)

        viewPager2.adapter = adapter

        TabLayoutMediator(tablayout,viewPager2){tab,position->
            when(position){
                0->{
                    tab.text = "ВОЙТИ"
                }
                1->{
//                    tab.setIcon(R.drawable.person)
                    tab.text = "РЕГИСТРАЦИЯ"
                }

            }
        }.attach()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()


    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser

        /**
         *
         */

        if (currentUser!= null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent).also {
                finish()
            }
        }
    }
}