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
import com.example.tmdcontactsapp.`class`.RetrofitOperations
import com.example.tmdcontactsapp.model.ChangePasswordModel
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*

class ChangePassword : AppCompatActivity() {

    private var email: String? = null
    lateinit var currentPassword: EditText
    lateinit var newPassword: EditText
    lateinit var confirmPassword: EditText
    private var token: String? = null
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        currentPassword = findViewById(R.id.currentPasswordText)
        newPassword = findViewById(R.id.newPasswordText)
        confirmPassword = findViewById(R.id.confirmPasswordText)

        token = savePrefs().get("token", "value")
        email = savePrefs().get("userMail", "value")

    }

    //POST Function
    fun rawJSON() {

        val changePasswordModel = ChangePasswordModel(email.toString(), newPassword.text.toString())

        job = CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = RetrofitOperations.instance.changePass(
                "Bearer " + token.toString(),
                changePasswordModel = changePasswordModel,
                currentPassword = currentPassword.text.toString()
            )

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
                    val intent =
                        Intent(applicationContext, BottomNavigationViewActivity::class.java)
                    startActivity(intent)

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

    fun changePassword(view: View) {
        if (newPassword.text.toString() == confirmPassword.text.toString()) {
            rawJSON()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}