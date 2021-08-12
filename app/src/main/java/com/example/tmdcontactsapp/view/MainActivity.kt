package com.example.tmdcontactsapp.view
// Created by Berkay Ozgen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.`class`.Preferences.savePrefs
import com.example.tmdcontactsapp.`class`.Preferences.set
import com.example.tmdcontactsapp.`class`.RetrofitOperations
import com.example.tmdcontactsapp.model.LoginModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    lateinit var topTextLogin: TextView
    lateinit var emailTextLogin: EditText
    lateinit var passwordTextLogin: EditText
    lateinit var loginButton: Button
    lateinit var forgotPassword: TextView
    lateinit var toRegisterText: TextView

    lateinit var USER_MAIL: String
    private var token: String? = null
    private var expiration: String? = null
    private var job: Job? = null

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        topTextLogin = findViewById(R.id.topTextLogin)
        emailTextLogin = findViewById(R.id.emailTextLogin)
        passwordTextLogin = findViewById(R.id.passwordTextLogin)
        loginButton = findViewById(R.id.loginButton)
        forgotPassword = findViewById(R.id.forgotPassword)
        toRegisterText = findViewById(R.id.toRegisterText)


        topTextLogin.setOnClickListener() {
            val intentTopToRegister = Intent(this, activity_register::class.java)
            startActivity(intentTopToRegister)
        }

        forgotPassword.setOnClickListener() {
            val intentResetPassword = Intent(this, Forgot_Password::class.java)
            startActivity(intentResetPassword)
        }

        toRegisterText.setOnClickListener() {
            val intentRegister = Intent(this, activity_register::class.java)
            startActivity(intentRegister)
        }
    }

    //POST Function
    fun rawJSON() {

        val loginModel =
            LoginModel(emailTextLogin.text.toString(), passwordTextLogin.text.toString())

        job = CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
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

//                  Log.d("Pretty Printed JSON :", prettyJson)
                    Log.d("Token :", token!!)
                    Log.d("Expiration Date :", expiration!!)

                    savePrefs()["token"] = response.body()?.token
                    savePrefs()["expiration"] = response.body()?.expiration


                    val intent =
                        Intent(applicationContext, BottomNavigationViewActivity::class.java)
                    startActivity(intent)

//                    loadData()
                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }

    /*//Get Profile Data using with email
    private fun loadData() {
        USER_MAIL = emailTextLogin.text.toString()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ContacsAPI::class.java)
        val call = service.getProfileData("Bearer " + token.toString(), email = USER_MAIL)

        call.enqueue(object : Callback<ProfileModel> {
            override fun onResponse(
                call: Call<ProfileModel>,
                response: Response<ProfileModel>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val profileModels = response.body()


//                        val exProfile = ProfileModel(10,"Berkay","","","","","","","","","","","",
//                        "")

//                        val stringModel = profileModels.toString()
                           savePrefs()["userName"] = profileModels?.name


//                        Log.e("String Model: ", exProfile.toString())

//                        var gson = Gson()
//                        var jsonString = gson.toJson(stringModel)

//                        var profilemodel = Gson().fromJson(jsonString, ProfileModel::class.java)
//                        Log.e("String Model: ", profilemodel.name)

                        val intent =
                            Intent(applicationContext, BottomNavigationViewActivity::class.java)
                        intent.putExtra("id", profileModels?.id)
                        intent.putExtra("Name", profileModels?.name)
                        intent.putExtra("Surname", profileModels?.surname)
                        intent.putExtra("Email", profileModels?.email)
                        intent.putExtra("Address", profileModels?.address)
                        intent.putExtra("BirthDate", profileModels?.birthDate)
                        intent.putExtra("Tel", profileModels?.tel)
                        intent.putExtra("TelBusiness", profileModels?.telBusiness)
                        intent.putExtra("TelHome", profileModels?.telHome)
                        intent.putExtra("Company", profileModels?.company)
                        intent.putExtra("Title", profileModels?.title)
                        intent.putExtra("Note", profileModels?.note)
                        intent.putExtra("token", token)
                        startActivity(intent)

                    }

                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }*/

    fun signIn(view: View) {

        //check emailtext & password null or notNull
        if (emailTextLogin.text.isEmpty().apply {}) {
            Toast.makeText(
                applicationContext,
                "Please enter email or phone number",
                Toast.LENGTH_LONG
            ).show()
        } else if (passwordTextLogin.text.isEmpty().apply {}) {
            Toast.makeText(applicationContext, "Please enter your password", Toast.LENGTH_LONG)
                .show()
        } else {
            USER_MAIL = emailTextLogin.text.toString()
            savePrefs()["userMail"] = USER_MAIL
            rawJSON()


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()

    }
}

