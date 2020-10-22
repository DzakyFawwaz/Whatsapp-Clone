package com.jaki.example.whatsappclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {


    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUser: DatabaseReference
    private var firebaseUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_next.setOnClickListener {
            registerUser()
        }

        btn_login_text.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser() {

        mAuth = FirebaseAuth.getInstance()

        val emailAddress: String = edt_email.text.toString()
        val password: String = edt_password.text.toString()
        val username: String = edt_username.text.toString()

        if (emailAddress == "") {
            Toast.makeText(this, "Email can't be empty", Toast.LENGTH_SHORT).show()
        }else if (password == ""){
            Toast.makeText(this,"Password can't be empty", Toast.LENGTH_SHORT).show()
        }else if (username == ""){
            Toast.makeText(this,"Username can't be empty", Toast.LENGTH_SHORT).show()
        }else{
            mAuth.createUserWithEmailAndPassword(emailAddress, password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    firebaseUserID = mAuth.currentUser!!.uid
                    refUser = FirebaseDatabase.getInstance().reference.child("User").child(firebaseUserID)

                    val userHashUp = HashMap<String, Any>()
                    userHashUp["uid"] = firebaseUserID
                    userHashUp["profile"] = ""
                    userHashUp["cover"] = ""
                    userHashUp["status"] = "offline"
                    userHashUp["username"] = username
                    userHashUp["email"] = emailAddress
                    userHashUp["password"] = password
                    userHashUp["date"] = ""
                    userHashUp["date"] = ""
                    userHashUp["bio"] = ""

                    refUser.updateChildren(userHashUp).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }

                    }

                }else{
                    Toast.makeText(this,"Error Message" + task.exception?.message.toString(), Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}