package com.jaki.example.whatsappclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.jaki.example.whatsappclone.model.User
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_setting.toolbar_main

class SettingActivity : AppCompatActivity() {

    private lateinit var userRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setSupportActionBar(toolbar_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Setting and privacy"


        btn_save_act.setOnClickListener {
            saveData()
        }

        btn_delete_acc.setOnClickListener {
            deleteAccount()
        }
    }

    private fun saveData() {
        val email = change_email.text.toString()
        val password = change_password.text.toString()

        val userHashUp = HashMap<String, Any>()
        userHashUp["email"] = email
        userHashUp["password"] = password

        FirebaseAuth.getInstance().currentUser.let { currentUser ->
            val uidUser = currentUser!!.uid
            userRef = FirebaseDatabase.getInstance()
                .reference
                // child users berisi nama folder dari user yang ada di firebase realtime database
                // nama ini harus persis
                .child("User")
                .child(uidUser)

            val credential = EmailAuthProvider.getCredential(email, password)
            currentUser.reauthenticate(credential).addOnCompleteListener {
                currentUser.updateEmail(email)
                currentUser.updatePassword(password)
            }
        }

        userRef.updateChildren(userHashUp).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this, "User update success", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun deleteAccount() {
        val email = change_email.text.toString()
        val password = change_password.text.toString()

        FirebaseAuth.getInstance().currentUser.let { currentUser ->
            val uidUser = currentUser!!.uid
            userRef = FirebaseDatabase.getInstance()
                .reference
                // child users berisi nama folder dari user yang ada di firebase realtime database
                // nama ini harus persis
                .child("User")
                .child(uidUser)

            val credential = EmailAuthProvider.getCredential(email, password)
            // reauthenticate untuk login ulang dari sistem
            currentUser.reauthenticate(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) { // jika berhasil login
                    // hapus user saat ini yang ada di authenticate firebase
                    currentUser.delete()
                    // hapus user infonya juga yang ada di realtime database firebase
                    userRef.removeValue()
                    // hapus user selesai
                    // Logout dari firebase
                    FirebaseAuth.getInstance().signOut()
                    // intent menuju activity login
                    val intent = Intent(this, IntroActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    // finish (hapus) activity account setting activity
                    finish()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}