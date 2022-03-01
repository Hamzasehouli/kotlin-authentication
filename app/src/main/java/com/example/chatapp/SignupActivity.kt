package com.example.chatapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: Button
    private lateinit var signupBtn: Button
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        name = findViewById(R.id.name_input)
        email = findViewById(R.id.email_input)
        password = findViewById(R.id.password_input)
        loginBtn = findViewById(R.id.login_btn)
        signupBtn = findViewById(R.id.signup_btn)

        signupBtn.setOnClickListener {
            var enteredName = name.text.toString()
            var enteredEmail = email.text.toString()
            var enteredPassword = email.text.toString()

            auth.createUserWithEmailAndPassword(enteredEmail, enteredPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        println("success")
                        startActivity(Intent(this, MainActivity::class.java))

                    } else {
                        println("fail")
                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                    }
                }

        }
        loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }

}