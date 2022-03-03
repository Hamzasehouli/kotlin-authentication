package com.example.chatapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var logoutBtn: Button

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.

        val sharedPref =getApplicationContext().getSharedPreferences("Auth",Context.MODE_PRIVATE)
//        val defaultValue = resources.getInteger(R.integer.saved_high_score_default_key)
        val email = sharedPref.getString("email", "none")
        val token = sharedPref.getString("token", "none")
        val isLoggedin = sharedPref.getBoolean("isLoggedin", false)
        println(token)
        println(isLoggedin)
        println(email)

        if(!isLoggedin){
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logoutBtn = findViewById(
            R.id.logout_btn
        )
        logoutBtn.setOnClickListener {
            println("ddd")
            var sharedPref = getSharedPreferences("Auth",Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString("email", "")
                putString("token", "user.token")
                putBoolean("isLoggedin", false)
                commit()
            }
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

    }
}