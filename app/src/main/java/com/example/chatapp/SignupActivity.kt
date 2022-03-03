package com.example.chatapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.beust.klaxon.Klaxon
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class SignupActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: Button
    private lateinit var signupBtn: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var url: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        name = findViewById(R.id.name_input)
        email = findViewById(R.id.email_input)
        password = findViewById(R.id.password_input)
        loginBtn = findViewById(R.id.login_btn)
        signupBtn = findViewById(R.id.signup_btn)

        url = "http://172.16.1.52:8000/api/v1/auth/signup"



        signupBtn.setOnClickListener {
            var enteredName = name.text.toString()
            var enteredEmail = email.text.toString()
            var enteredPassword = password.text.toString()

            if(enteredEmail == "" || enteredPassword =="" || enteredName == ""){
                Toast.makeText(this@SignupActivity, "Please enter the required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val mediaType = "application/json; charset=utf-8".toMediaType()
            var jsonObject =  JSONObject();

                jsonObject.put("username", enteredName)
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
                        val message: String,val isLoggedin: Boolean,val email: String, val token: String
                    )
                    var gson = GsonBuilder().create()
                    var user = gson.fromJson(body, User::class.java)

                    if(user.status == "success"){
                        val sharedPref = getSharedPreferences("Auth",Context.MODE_PRIVATE)

                        with (sharedPref.edit()) {
                            putString("email", user.email)
                            putString("token", user.token)
                            putBoolean("isLoggedin", user.isLoggedin)
                            commit()
                        }
                        startActivity(Intent(this@SignupActivity, MainActivity::class.java))

                    }

                }

                override fun onFailure(call: Call, e: IOException) {
                    println(e)
                }
            })

            /////////////

//            var client = OkHttpClient()
//
//
//                val request = Request.Builder()
//                    .url("https://fakestoreapi.com/products").build()
//
//                        client.newCall(request).enqueue(object: Callback {
//                            override fun onResponse(call: Call, response: Response) {
//                                val body = response.body?.string()
//                                println(body)
//                            }
//
//                            override fun onFailure(call: Call, e: IOException) {
//                                println("fail")
//                    }
//                })





            ////////////



//            auth.createUserWithEmailAndPassword(enteredEmail, enteredPassword)
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        println("success")
//                        startActivity(Intent(this, MainActivity::class.java))
//
//                    } else {
//                        println("fail")
//                        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
//                    }
//                }

        }

        loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }

}