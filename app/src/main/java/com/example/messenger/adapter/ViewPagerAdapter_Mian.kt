package com.example.messenger.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.messenger.ui.main.chats.ChatFragment
import com.example.messenger.ui.main.contacts.ContactsFragment
import com.example.messenger.ui.main.profile.ProfileFragment
import com.example.messenger.ui.register.sign_in.SIgnInFragment
import com.example.messenger.ui.register.sign_up.SignUpFragment

class ViewPagerAdapter_Mian (fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                ChatFragment()
            }
            1->{
                ContactsFragment()
            }

            else->{
                ProfileFragment()
            }
        }
    }


}