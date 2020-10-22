package com.jaki.example.whatsappclone

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.jaki.example.whatsappclone.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.toolbar_main
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUser: DatabaseReference
    private var firebaseUserID: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(toolbar_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Log In"

        mAuth = FirebaseAuth.getInstance()

        btn_login.setOnClickListener {
            val progressBar = ProgressDialog(this)
            progressBar.setMessage("Please wait...")
            progressBar.show()
            loginUser()
            progressBar.dismiss()
        }
    }

    private fun loginUser() {
        val password: String = edt_password_login.text.toString()
        val username: String = edt_username_login.text.toString()

        if (username == "") {
            Toast.makeText(this, "Username can't be empty", Toast.LENGTH_SHORT).show()
        }else if (password == ""){
            Toast.makeText(this,"Password can't be empty", Toast.LENGTH_SHORT).show()
        }else{
            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this,"Error Message" + task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
