package com.example.tmdcontactsapp.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.`class`.Preferences.get
import com.example.tmdcontactsapp.`class`.Preferences.savePrefs
import com.example.tmdcontactsapp.adapter.RecyclerViewAdapter
import com.example.tmdcontactsapp.model.AddGroupContactModel
import com.example.tmdcontactsapp.model.ContactsModel
import com.example.tmdcontactsapp.service.ContacsAPI
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddGroupContact : AppCompatActivity() {

    private var contactId: Int? = 0
    private var groupId: Int? = null
    private var token: String? = null
    private var userId: Int? = 0

    lateinit var addGroupContactRecyclerView: RecyclerView
    private val BASE_URL = "http://tmdcontacts-api.dev.tmd"
    private var contactModels: ArrayList<ContactsModel>? = null
    private var filteredList: ArrayList<ContactsModel>? = ArrayList()
    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    lateinit var searchTextAddGroupContact: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_group_contact)


        token = savePrefs().get("token", "nullValue")
        groupId = savePrefs()["groupId", -1]
        userId = savePrefs()["userId", -1]

        loadData()


        //Definition RecyclerView
        addGroupContactRecyclerView = findViewById(R.id.addGroupContactRecyclerView)
        val layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        addGroupContactRecyclerView.layoutManager = layoutManager

        searchTextAddGroupContact = findViewById(R.id.searchTextAddGroupContact)
        searchTextAddGroupContact.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })

    }

    fun filter(text: String) {
        filteredList!!.clear()
        for (item: ContactsModel in contactModels!!) {
            if (item.name.lowercase().contains(text.lowercase()) || item.surname.lowercase()
                    .contains(text.lowercase())
            ) {
                filteredList!!.add(item)
            }
        }

        recyclerViewAdapter!!.filterList(filteredList!!)

    }

    private fun loadData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ContacsAPI::class.java)
        val call = userId?.let { service.getData("Bearer " + token.toString(), userId = it) }

        call?.enqueue(object : Callback<List<ContactsModel>> {
            override fun onResponse(
                call: Call<List<ContactsModel>>,
                response: Response<List<ContactsModel>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        contactModels = ArrayList(it)
                        contactModels?.let { contactsmodel ->
                            recyclerViewAdapter = RecyclerViewAdapter(contactsmodel)
                            recyclerViewAdapter?.setType(RecyclerViewAdapter.VIEW_TYPE_ONE)
                            recyclerViewAdapter?.setListener(object : RecyclerViewAdapter.Listener {
                                override fun onItemClick(contactsModel: ContactsModel) {
                                    Toast.makeText(
                                        applicationContext,
                                        "${contactsModel.id}",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    contactId = contactsModel.id
                                    rawJSON()

                                }

                            })
                            addGroupContactRecyclerView.adapter = recyclerViewAdapter
                        }

                    }


                }
            }

            override fun onFailure(call: Call<List<ContactsModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })

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

        val addGroupContactModel = AddGroupContactModel(groupId!!, contactId!!)
        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.addGroupContact(
                "Bearer " + token.toString(),
                addGroupContactModel = addGroupContactModel
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
                    val intent = Intent(applicationContext,GroupDetails::class.java)
                    startActivity(intent)

                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }


}