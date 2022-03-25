package com.example.messenger.model

data class Chat(
    var message: String? = null,
    var receiver: String? = null,
    var sender: String? = null,
    var senderIsSeen:  String? = null,
    var receiverIsSeen:  String? = null,
    var IsSeen:  String? = null,
)
