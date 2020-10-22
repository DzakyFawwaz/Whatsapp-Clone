package com.jaki.example.whatsappclone

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.jaki.example.whatsappclone.adapter.ChatAdapter
import com.jaki.example.whatsappclone.model.Chat
import com.jaki.example.whatsappclone.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_message_chat.*
import kotlinx.android.synthetic.main.activity_message_chat.toolbar_main

class MessageChatActivity : AppCompatActivity() {

    companion object {
        const val IS_SEEN_FALSE = "false"
    }

    private lateinit var receiverID: String
    private lateinit var senderID: String
    private lateinit var dbRef: DatabaseReference
    private lateinit var userRef: DatabaseReference
    private lateinit var storageRef: StorageReference
    private lateinit var chatsRef: DatabaseReference
    private lateinit var chatSenderRef: DatabaseReference
    private lateinit var chatReceiverRef: DatabaseReference
    private lateinit var userListener: ValueEventListener
    private lateinit var chatSenderListener: ValueEventListener
    private lateinit var chatListListener: ValueEventListener
    private lateinit var chatAdapter: ChatAdapter
    private val requestCodeActivity = 438
    private var firebaseUser: FirebaseUser? = null
    private var receiverImage = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_chat)
        setSupportActionBar(toolbar_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        receiverID = intent.getStringExtra("VISIT_ID") as String
        chatAdapter = ChatAdapter(receiverID)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        senderID = firebaseUser?.uid as String
        dbRef = FirebaseDatabase.getInstance().reference
        userRef = dbRef.child("User").child(senderID)
        storageRef = FirebaseStorage.getInstance().reference.child("Chat Images")

        // menentukan gambar dan username dari orang yang di chat
        userListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java) as User
                    user_name_chat.text = user.username
                    if (user.profile.isNotBlank()) {
                        receiverImage = user.profile
                        Picasso.get().load(user.profile)
                            .placeholder(R.drawable.blank).into(profile_chat)
                    }
                }
            }

        }
        // method menentukan id dari sender dan receiver   *dari: github mas fahru
        chatSenderListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    chatSenderRef.child("id").setValue(receiverID)
                }
                chatReceiverRef.child("id").setValue(senderID)
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        chatListListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val chatList = arrayListOf<Chat>()
                if (snapshot.exists()) {
                    if (snapshot.hasChildren()) {
                        snapshot.children.forEach {
                            val chat = it.getValue(Chat::class.java) as Chat
                            if (chat.receiver == senderID && chat.sender == receiverID
                                || chat.receiver == receiverID && chat.sender == senderID
                            ) {
                                chatList.add(chat)
                            }
                            chatAdapter.addChats(chatList, receiverImage)
                        }
                    } else {
                        chatAdapter.addChats(chatList, receiverImage)
                    }
                }
            }

        }

        // mendefiniskan id chat user dengan user lain
        firebaseUser.let {

            chatSenderRef = FirebaseDatabase.getInstance().reference
                .child("ChatList")
                .child(senderID)
                .child(receiverID)

            chatReceiverRef = FirebaseDatabase.getInstance().reference
                .child("ChatList")
                .child(receiverID)
                .child(senderID)
        }

        send_message.setOnClickListener {
            message_box.run {
                val message = this.text.toString()
                if (message.isNotBlank()) {
                    sendMessageToUser(message)
                }
                text.clear()
                clearFocus()
            }
        }

        choose_photo_chat.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Pick Image"), 438)
        }

        rv_chat.run {
            adapter = chatAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MessageChatActivity)
            addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                // Wait till recycler_view will update itself and then scroll to the end.
                post {
                    adapter?.itemCount?.takeIf { it > 0 }?.let {
                        scrollToPosition(it - 1)
                    }
                }
            }
        }

        val receiverRef = dbRef.child("Users").child(receiverID)
        receiverRef.addListenerForSingleValueEvent(userListener)

        chatsRef = FirebaseDatabase.getInstance().reference.child("Chats")
        chatsRef.addValueEventListener(chatListListener)

    }

    private fun sendMessageToUser(message: String) {
        val reference = FirebaseDatabase.getInstance().reference
        val messageKey = reference.push().key
        messageKey.let {
            val messageHashMap = HashMap<String, Any?>()
            messageHashMap["sender"] = senderID
            messageHashMap["message"] = message
            messageHashMap["receiver"] = receiverID
            messageHashMap["isseen"] = IS_SEEN_FALSE
            messageHashMap["url"] = ""
            messageHashMap["messageId"] = it

            dbRef.child("Chats").child(messageKey!!).setValue(messageHashMap)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        chatSenderRef.addListenerForSingleValueEvent(chatSenderListener)
                    }
                }

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeActivity && resultCode == Activity.RESULT_OK && data?.data !== null) {
            data.data?.let { fileUri ->
                val progressBar = ProgressDialog(this)
                progressBar.setMessage("image is uploading, please wait...")
                progressBar.show()


                val messageId = dbRef.push().key.toString()
                val filePath = storageRef.child("$messageId.jpg")
                val uploadTask = filePath.putFile(fileUri)
                uploadTask.continueWithTask {
                    if (!it.isSuccessful) {
                        it.exception?.let { error ->
                            throw error
                        }
                    }
                    return@continueWithTask filePath.downloadUrl
                }.addOnCompleteListener { taskUpload ->
                    if (taskUpload.isSuccessful) {
                        val downloadUrl = taskUpload.result.toString()
                        val chat = Chat(
                            sender = senderID,
                            receiver = receiverID,
                            message = "",
                            isseen = IS_SEEN_FALSE,
                            url = downloadUrl,
                            messageId = messageId
                        )
                        dbRef.child("Chats").child(messageId).setValue(chat)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    chatSenderRef.addListenerForSingleValueEvent(chatSenderListener)
                                }
                            }
                    }
                    progressBar.dismiss()
                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onDestroy() {
        super.onDestroy()
        chatsRef.removeEventListener(chatListListener)
    }

    override fun onStart() {
        super.onStart()
        userRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val usr = snapshot.getValue(User::class.java) as User
                    val userUpdate = usr.copy(status = "online")
                    userRef.setValue(userUpdate)
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )
    }

    override fun onStop() {
        super.onStop()
        userRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val usr = snapshot.getValue(User::class.java) as User
                    val userUpdate = usr.copy(status = "offline")
                    userRef.child("Users").child(senderID).setValue(userUpdate)
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )
    }
}


