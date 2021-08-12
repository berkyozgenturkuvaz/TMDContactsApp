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
import com.example.tmdcontactsapp.`class`.RetrofitOperations
import com.example.tmdcontactsapp.model.AddGroupModel
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*

class AddGroup : AppCompatActivity() {

    lateinit var addGroupNameText: EditText
    lateinit var addGroupButton: Button
    private var userId: Int? = 0
    private var token: String? = null
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group)

        init()

    }

    fun init(){
        addGroupNameText = findViewById(R.id.addGroupNameText)
        addGroupButton = findViewById(R.id.addGroupButton)

        token = savePrefs()["token", "value"]
        userId = savePrefs()["userId", -1]
    }

    //POST Function
    fun rawJSON() {

        job = CoroutineScope(Dispatchers.IO).launch {

            val addGroupModel = AddGroupModel(addGroupNameText.text.toString(), userId!!)

            // Do the POST request and get response
            val response =
                RetrofitOperations.instance.addGroup(
                    "Bearer " + token.toString(),
                    addGroupModel = addGroupModel
                )

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

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}