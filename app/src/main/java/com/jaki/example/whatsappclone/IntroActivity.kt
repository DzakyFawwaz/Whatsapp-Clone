package com.jaki.example.whatsappclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jaki.example.whatsappclone.R
import kotlinx.android.synthetic.main.activity_intro.*
import kotlinx.android.synthetic.main.activity_register.*
import javax.security.auth.login.LoginException

class IntroActivity : AppCompatActivity() {

    private var firebase : FirebaseUser? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null && !currentUser.isAnonymous){
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }

        btn_agree.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
        }

//        btn_agree.setOnClickListener {
//            firebase = FirebaseAuth.getInstance().currentUser
//            if (firebase != null){
//                val intent = Intent(this, RegisterActivity::class.java)
//                startActivity(intent)
//                finish()
//            }else{
//                val intent = Intent(this, LoginActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//
//        }


    }

//    override fun onStart() {
//        super.onStart()
//
//        firebase = FirebaseAuth.getInstance().currentUser
//        if (firebase != null){
//            val intent = Intent(this, RegisterActivity::class.java)
//            startActivity(intent)
//        }
//    }
}