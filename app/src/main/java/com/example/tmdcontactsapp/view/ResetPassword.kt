package com.example.tmdcontactsapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.`class`.Preferences.get
import com.example.tmdcontactsapp.`class`.Preferences.savePrefs
import com.example.tmdcontactsapp.`class`.Preferences.set
import com.example.tmdcontactsapp.`class`.RetrofitOperations
import com.example.tmdcontactsapp.model.LoginModel
import com.example.tmdcontactsapp.model.ResetPasswordModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*

class ResetPassword : AppCompatActivity() {

    private var emailForPass: String? = null
    private var password: String? = null
    lateinit var newPassword: EditText
    lateinit var confirmNewPassword: EditText
    private var token: String? = null
    private var expiration: String? = null
    private var job: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        init()

    }

    fun init() {
        emailForPass = savePrefs()["emailForPass", ""]

        newPassword = findViewById(R.id.newPasswordText)
        confirmNewPassword = findViewById(R.id.confirmNewPasswordText)
    }

    //POST Function
    fun rawJSON() {

        job = CoroutineScope(Dispatchers.IO).launch {

            password = newPassword.text.toString()
            val resetPasswordModel =
                ResetPasswordModel(emailForPass.toString(), password.toString())

            // Do the POST request and get response
            val response =
                RetrofitOperations.instance.resetPass(resetPasswordModel = resetPasswordModel)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        //          JsonParser.parseString(
                        response.body()
                            ?.string()
                        //       )
                    )


                    Log.d("Pretty Printed JSON :", prettyJson)
                    rawJSON2()
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                    Log.e("RETROFIT_ERROR", response.message().toString())
                    Log.e("RETROFIT_ERROR", response.body().toString())
                    Log.e("RETROFIT_ERROR", response.errorBody().toString())
                    Log.e("RETROFIT_ERROR", response.toString())
                }
            }
        }
    }

    //POST Function
    fun rawJSON2() {

        job = CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response

            val loginModel =
                LoginModel(emailForPass.toString(), password.toString())

            val response = RetrofitOperations.instance.login(loginModel = loginModel)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()!!.token

                        )
                    )

                    token = response.body()!!.token
                    expiration = response.body()!!.expiration

//                    sharedPreferences.edit().putString("token",token).apply()
//                    sharedPreferences.edit().putString("expiration",expiration).apply()

                    Log.d("Pretty Printed JSON :", prettyJson)
                    Log.d("Token :", token!!)
                    Log.d("Expiration Date :", expiration!!)

                    savePrefs()["expiration"] = expiration
                    savePrefs()["token"] = token
                    savePrefs()["userMail"] = emailForPass.toString()

                    val intent =
                        Intent(applicationContext, BottomNavigationViewActivity::class.java)
                    startActivity(intent)

                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }

    fun resetPassword(view: View) {
        if (newPassword.text.toString() == confirmNewPassword.text.toString()) {
            rawJSON()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}