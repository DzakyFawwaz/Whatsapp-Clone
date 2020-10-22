package com.jaki.example.whatsappclone.model

data class Chat(
    var sender: String = "",
    var message: String = "",
    var receiver: String = "",
    var isseen: String = "",
    var url : String = "",
    var messageId: String = ""

)