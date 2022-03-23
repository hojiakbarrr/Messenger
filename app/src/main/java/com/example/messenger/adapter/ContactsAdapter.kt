package com.example.messenger.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.messenger.R
import com.example.messenger.databinding.ItemUserBinding
import com.example.messenger.model.User
import com.squareup.picasso.Picasso

class ContactsAdapter (): RecyclerView.Adapter<ContactsAdapter.ViewHolder>(){

    var contactList: List<User> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }


    inner class ViewHolder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root){
        fun bind (user: User){
            binding.apply {
                if (user.imageUrl.isNullOrEmpty()){
                    itemProfileImage.setImageResource(R.drawable.ic_person)
                }else{
                    Picasso.get().load(user.imageUrl).into(itemProfileImage)
                }
                itemProfileName.text = user.userName
                if (user.status == "online"){
                    itemStatus.setImageResource(R.drawable.online_circle)
                }else{
                    itemStatus.setImageResource(R.drawable.offline_circle)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        val binding = ItemUserBinding.bind(view)
        return ViewHolder(binding)    }

    override fun onBindViewHolder(holder: ContactsAdapter.ViewHolder, position: Int) {
        holder.bind(contactList[position])

    }

    override fun getItemCount(): Int = contactList.size
}