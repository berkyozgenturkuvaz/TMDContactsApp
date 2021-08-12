package com.example.tmdcontactsapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.`class`.Preferences.savePrefs
import com.example.tmdcontactsapp.`class`.Preferences.set
import com.example.tmdcontactsapp.`class`.RetrofitOperations
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*

class Forgot_Password : AppCompatActivity() {

    lateinit var emailForgotText: EditText
    lateinit var resetForgotButton: Button
    private var emailForgotPass: String? = null
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        init()

    }

    fun init(){
        emailForgotText = findViewById(R.id.emailForgotText)
        resetForgotButton = findViewById(R.id.resetForgotButton)
    }

    //POST Function
    fun rawJSON() {

        emailForgotPass = emailForgotText.text.toString()

        job = CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response =
                RetrofitOperations.instance.forgotPass(email = emailForgotPass.toString())

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string()
                        )
                    )

                    savePrefs()["verificationCode"] = prettyJson
                    savePrefs()["emailForPass"] = emailForgotPass.toString()

                    Log.d("Pretty Printed JSON :", prettyJson)
                    val intent = Intent(applicationContext, VerificationCode::class.java)
                    startActivity(intent)

                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }

    fun resetForgotButton(view: View) {
        if (emailForgotText.text.isEmpty().apply {}) {
            Toast.makeText(
                applicationContext,
                "Email field can not be empty!",
                Toast.LENGTH_LONG
            ).show()
        } else {
            rawJSON()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}