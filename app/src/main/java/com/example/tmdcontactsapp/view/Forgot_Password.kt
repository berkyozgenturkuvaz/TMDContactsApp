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
import com.example.tmdcontactsapp.service.ContacsAPI
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Forgot_Password : AppCompatActivity() {

    lateinit var emailForgotText: EditText
    lateinit var resetForgotButton: Button
    private var emailForgotPass: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        emailForgotText = findViewById(R.id.emailForgotText)
        resetForgotButton = findViewById(R.id.resetForgotButton)

    }

    //POST Function
    fun rawJSON() {

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://tmdcontacts-api.dev.tmd")
            .build()

        // Create Service
        val service = retrofit.create(ContacsAPI::class.java)

        emailForgotPass = emailForgotText.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.forgotPass(email = emailForgotPass.toString())

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string()
                        )
                    )

                    Log.d("Pretty Printed JSON :", prettyJson)
                    val intent = Intent(applicationContext, VerificationCode::class.java)
                    intent.putExtra("verificationCode", prettyJson)
                    intent.putExtra("emailForPass", emailForgotPass.toString())
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
}