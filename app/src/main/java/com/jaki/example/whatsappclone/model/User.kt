package com.jaki.example.whatsappclone.model

data class User(
    var uid: String = "",
    var email: String = "",
    var password: String = "",
    var profile: String = "",
    var cover: String = "",
    var status: String = "",
    var username: String = "",
    var bio: String = "",
    var date: String = "",
    var location: String = ""
)


//class User {
//
//
//    var uid: String = "",
//    var email: String = "",
//    var password: String = "",
//    var profile: String = "",
//    var cover: String = "",
//    var status: String = "",
//    var username: String = ""
//
//    constructor(
//        uid: String,
//        email: String,
//        password: String,
//        profile: String,
//        cover: String,
//        status: String,
//        username: String
//    ) {
//        this.uid = uid
//        this.email = email
//        this.password = password
//        this.profile = profile
//        this.cover = cover
//        this.status = status
//        this.username = username
//    }
//
//    fun getUID(): String?{
//        return uid
//    }
//
//    fun setUID(uid: String){
//        this.uid = uid
//    }
//
//    fun getEmail: String?{
//        return email
//    }
//
//    fun setEmail(email: String){
//        this.email = email
//    }
//
//
//    fun getProfile(): String? {
//        return profile
//    }
//
//    fun setProfile(profile: String){
//        this.profile = profile
//    }
//
//    fun getUsername(): String?{
//        return username
//    }
//
//    fun setUsername(username: String){
//        this.username = username
//    }
//
//    fun getStatus(status: String){
//        return status
//    }
//
//    fun setStatus(status: String){
//        this.status = status
//    }
//
//    fun getCover(cover: String){
//        return cover
//    }
//    fun setCover(cover: String){
//        this.cover = cover
//    }
//}