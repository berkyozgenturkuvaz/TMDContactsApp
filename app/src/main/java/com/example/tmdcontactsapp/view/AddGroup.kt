package com.example.tmdcontactsapp.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.`class`.Preferences.get
import com.example.tmdcontactsapp.`class`.Preferences.savePrefs
import com.example.tmdcontactsapp.model.AddGroupModel
import com.example.tmdcontactsapp.service.ContacsAPI
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddGroup : AppCompatActivity() {

    lateinit var addGroupNameText: EditText
    lateinit var addGroupButton: Button
    private var userId: Int? = 0
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)

        addGroupNameText = findViewById(R.id.addGroupNameText)
        addGroupButton = findViewById(R.id.addGroupButton)

        token = savePrefs()["token", "value"]
        userId = savePrefs()["userId", -1]
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

        val addGroupModel = AddGroupModel(addGroupNameText.text.toString(), userId!!)
        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response =
                service.addGroup("Bearer " + token.toString(), addGroupModel = addGroupModel)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        //     JsonParser.parseString(
                        response.body()
                            ?.string()
                        //      )
                    )

                    Log.d("Pretty Printed JSON :", prettyJson)

                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }

    fun createGroup(view: View) {
        rawJSON()
    }

}