package com.example.tmdcontactsapp.view

import android.content.Intent
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
import com.example.tmdcontactsapp.model.UpdateGroupModel
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*

class UpdateGroup : AppCompatActivity() {

    lateinit var updateGroupNameText: EditText
    lateinit var updateGroupButton: Button
    private var groupId: Int? = 0
    private var userId: Int? = 0
    private var groupName: String? = null
    private var token: String? = null
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_group)

        updateGroupNameText = findViewById(R.id.updateGroupNameText)
        updateGroupButton = findViewById(R.id.updateGroupButton)


        token = savePrefs().get("token", "nullValue")
        groupId = savePrefs()["groupId", -1]
        userId = savePrefs()["userId", -1]
        groupName = savePrefs()["groupName"]

        updateGroupNameText.setText(groupName)

    }

    //POST Function
    fun rawJSON() {

        val updateGroupModel =
            UpdateGroupModel(groupId!!, userId!!, updateGroupNameText.text.toString())

        job = CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = RetrofitOperations.instance.updateGroup(
                "Bearer " + token.toString(),
                updateGroupModel = updateGroupModel
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
                    val intent = Intent(baseContext, GroupsFragment::class.java)
                    startActivity(intent)


                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }

    fun updateGroup(view: View) {
        rawJSON()
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}