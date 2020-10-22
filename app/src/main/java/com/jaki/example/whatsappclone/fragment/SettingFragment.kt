package com.jaki.example.whatsappclone.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.jaki.example.whatsappclone.EditProfileActivity
import com.jaki.example.whatsappclone.R
import com.jaki.example.whatsappclone.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*


class SettingFragment : Fragment() {

    private lateinit var userRef : DatabaseReference
    private var firebaseUser : FirebaseUser? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_setting, container, false)

//        val instagram = view.findViewById<ImageView>(R.id.instagram_setting)
//        val twitter = view.findViewById<ImageView>(R.id.twitter_setting)
//        val fb = view.findViewById<ImageView>(R.id.facebook_setting)
//        val choose = view.findViewById<ImageView>(R.id.choose_image)

       view.btn_edit_profile.setOnClickListener {
           val intent = Intent(view.context, EditProfileActivity::class.java)
           startActivity(intent)
       }



        firebaseUser = FirebaseAuth.getInstance().currentUser
        userRef = FirebaseDatabase.getInstance().reference.child("User").child(firebaseUser!!.uid)

        userRef.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue(User::class.java) as User

                    if (context != null) {

                        username_setting.text = user.username
                        bio_setting.text = user.bio
                        location_setting.text = user.location
                        date_setting.text = user.date

                        Picasso.get().load(user.profile).into(profile_setting)
                        Picasso.get().load(user.cover).into(cover_setting)

                    }
                }
            }
        })
        return view
    }
}