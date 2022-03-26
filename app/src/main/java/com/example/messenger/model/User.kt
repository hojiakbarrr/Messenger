package com.example.messenger.model

data class User(
    var id:String? = null,
    var email:String? = null,
    var userName:String? = null,
    var imageUrl:String = "default",
    var hasChat:Boolean = false,
    var status:String? = "offline",


)
