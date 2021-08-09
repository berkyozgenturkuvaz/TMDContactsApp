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
import com.example.tmdcontactsapp.model.UpdateGroupModel
import com.example.tmdcontactsapp.service.ContacsAPI
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UpdateGroup : AppCompatActivity() {

    lateinit var updateGroupNameText: EditText
    lateinit var updateGroupButton: Button
    private var groupId: Int? = 0
    private var userId: Int? = 0
    private var groupName : String? = null
    private var token:String? = null

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

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://tmdcontacts-api.dev.tmd")
            .build()

        // Create Service
        val service = retrofit.create(ContacsAPI::class.java)

        val updateGroupModel =
            UpdateGroupModel(groupId!!, userId!!, updateGroupNameText.text.toString())
        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.updateGroup("Bearer " + token.toString() ,updateGroupModel = updateGroupModel)

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
}