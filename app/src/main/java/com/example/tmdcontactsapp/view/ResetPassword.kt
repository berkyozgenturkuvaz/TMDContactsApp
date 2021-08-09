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
import com.example.tmdcontactsapp.model.LoginModel
import com.example.tmdcontactsapp.model.ResetPasswordModel
import com.example.tmdcontactsapp.service.ContacsAPI
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResetPassword : AppCompatActivity() {

    private var emailForPass: String? = null
    private var password: String? = null
    lateinit var newPassword: EditText
    lateinit var confirmNewPassword: EditText
    private var token: String? = null
    private var expiration: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        emailForPass = savePrefs()["emailForPass", ""]

        newPassword = findViewById(R.id.newPasswordText)
        confirmNewPassword = findViewById(R.id.confirmNewPasswordText)

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

        password = newPassword.text.toString()
        val resetPasswordModel = ResetPasswordModel(emailForPass.toString(),password.toString())
        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.resetPass(resetPasswordModel = resetPasswordModel)

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

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://tmdcontacts-api.dev.tmd")
            .build()

        // Create Service
        val service = retrofit.create(ContacsAPI::class.java)

        val loginModel =
            LoginModel(emailForPass.toString(), password.toString())
        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.login(loginModel = loginModel)

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

                    val intent = Intent(applicationContext, BottomNavigationViewActivity::class.java)
                    intent.putExtra("userMail", emailForPass)
                    intent.putExtra("token",token)
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
}