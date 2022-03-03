package com.example.chatapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.logging.Handler

class LoginActivity : AppCompatActivity() {


    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: Button
    private lateinit var signupBtn: Button
    private lateinit var url: String



    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.

        val sharedPref =getApplicationContext().getSharedPreferences("Auth",Context.MODE_PRIVATE)


        val isLoggedin = sharedPref.getBoolean("isLoggedin", false)


        if(isLoggedin){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        email = findViewById(R.id.email_input)
        password = findViewById(R.id.password_input)
        loginBtn = findViewById(R.id.login_btn)
        signupBtn = findViewById(R.id.signup_btn)
        url = "http://172.16.1.52:8000/api/v1/auth/login"

        loginBtn.setOnClickListener {
            var enteredEmail = email.text.toString()
            var enteredPassword = password.text.toString()
            println(enteredPassword)

            if(enteredEmail == "" || enteredPassword =="" || enteredPassword.length<8 || !enteredEmail.contains("@", ignoreCase = true)){
                Toast.makeText(this@LoginActivity, "Please enter the required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            var jsonObject =  JSONObject();


            jsonObject.put("email", enteredEmail)
            jsonObject.put("password", enteredPassword)



            var client = OkHttpClient()

            var body = jsonObject.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()




            client.newCall(request).enqueue(object: Callback {
                override fun onResponse(call: Call, response: Response) {

                    val body = response.body?.string()

                    data class User(
                        val status: String,
                        val message: String,
                        val isLoggedin: Boolean,
                        val email: String,
                        val token: String
                    )

                    var gson = GsonBuilder().create()
                    var user = gson.fromJson(body, User::class.java)
                    println(user)

                    if (user.status == "success") {
                        val sharedPref = getSharedPreferences("Auth", Context.MODE_PRIVATE)

                        with(sharedPref.edit()) {
                            putString("email", user.email)
                            putString("token", user.token)
                            putBoolean("isLoggedin", user.isLoggedin)
                            commit()
                        }
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                    }else{
                        android.os.Handler(Looper.getMainLooper()).post {
                            Toast.makeText(this@LoginActivity, user.message, Toast.LENGTH_SHORT).show()
                        }

                    }
                }
                    override fun onFailure(call: Call, e: IOException) {
                        println("error")
                    }
                })



        }

        signupBtn.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
        }

    }
}