package com.jaki.example.whatsappclone

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.jaki.example.whatsappclone.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_setting.*

class EditProfileActivity : AppCompatActivity() {

    private var RequestCode = 438
    private var imageUri : Uri? = null
    private var storageRef : StorageReference? = null
    private lateinit var userRef : DatabaseReference
    private var firebaseUser : FirebaseUser? = null
    private var coverCheck: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        userRef = FirebaseDatabase.getInstance().reference.child("User").child(firebaseUser!!.uid)


        userRef.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val user = snapshot.getValue(User::class.java) as User

                    Picasso.get().load(user.profile).into(choose_profile)
                    Picasso.get().load(user.cover).into(choose_cover)

                    change_username_edt.hint = user.username
                    change_location_edt.hint = user.location
                    change_date_edt.hint = user.date
                    change_bio_edt.hint = user.bio
                }
            }
        })

        cancel_action.setOnClickListener {
            finish()
        }

        btn_save.setOnClickListener {
            saveData()
        }

        choose_profile.setOnClickListener {
            pickImage()
        }

        choose_cover.setOnClickListener {
            coverCheck = "cover"
            pickImage()
        }

        firebaseUser = FirebaseAuth.getInstance().currentUser
        userRef = FirebaseDatabase.getInstance().reference.child("User").child(firebaseUser!!.uid)
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")

    }

    private fun saveData() {
        val username = change_username_edt.text.toString()
        val location = change_location_edt.text.toString()
        val date = change_date_edt.text.toString()
        val bio = change_bio_edt.text.toString()

        val userHashUp = HashMap<String, Any>()
        userHashUp["location"] = location
        userHashUp["date"] = date
        userHashUp["bio"] = bio
        userHashUp["username"] = username

        userRef.updateChildren(userHashUp).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this, "User update success", Toast.LENGTH_SHORT)
                    .show()
                finish()
            }
        }
    }


    private fun pickImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, RequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode && resultCode == Activity.RESULT_OK && data?.data !=null){
            imageUri = data.data
            Toast.makeText(this,"uploading...", Toast.LENGTH_SHORT).show()
            uploadImageToDatabase()
        }
    }

    private fun uploadImageToDatabase() {
        val progressBar = ProgressDialog(this)
        progressBar.setMessage("image is uploading, please wait...")
        progressBar.show()

        if (imageUri != null){

            val fileRef = storageRef!!.child(System.currentTimeMillis().toString() + ".jpg")

            var uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(imageUri!!)

            uploadTask.continueWithTask(Continuation <UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }

                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val downloadUri = task.result
                    val url = downloadUri.toString()

                    if (coverCheck == "cover"){
                        val mapCoverImg = HashMap<String, Any>()
                        mapCoverImg["cover"] = url
                        userRef.updateChildren(mapCoverImg)
                        coverCheck = ""

                    }else{
                        val mapProfileImg = HashMap<String, Any>()
                        mapProfileImg["profile"] = url
                        userRef.updateChildren(mapProfileImg)
                        coverCheck = ""
                    }
                    progressBar.dismiss()
                }
            }

        }
    }
}