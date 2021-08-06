package com.example.tmdcontactsapp.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.`class`.Preferences.get
import com.example.tmdcontactsapp.`class`.Preferences.savePrefs
import com.example.tmdcontactsapp.`class`.Preferences.set
import com.example.tmdcontactsapp.`class`.SwipeGesture
import com.example.tmdcontactsapp.adapter.RecViewAdapterGroupDetails
import com.example.tmdcontactsapp.model.DeleteGroupContactModel
import com.example.tmdcontactsapp.model.GroupDetailsModel
import com.example.tmdcontactsapp.service.ContacsAPI
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_group_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GroupDetails : AppCompatActivity() {

    lateinit var groupDetailsRecyclerView: RecyclerView
    val BASE_URL = "http://tmdcontacts-api.dev.tmd"
    private var filteredList: ArrayList<GroupDetailsModel>? = ArrayList()
    var groupDetailsModel: ArrayList<GroupDetailsModel>? = null
    var groupDetailsRecyclerViewAdapter: RecViewAdapterGroupDetails? = null
    lateinit var searchTextGroupDetails: EditText

    private var groupId: Int? = 0
    private var userId: Int? = 0
    private var contactId: Int? = 0
    private var groupName: String? = null
    private var token: String? = null

    lateinit var updateGroupButton: Button
    lateinit var addContactGroupButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_details)

        setSupportActionBar(toolbarGroupDetails)
        toolbarGroupDetails.setNavigationOnClickListener {
            Toast.makeText(applicationContext, "Navigation Menu Clicked", Toast.LENGTH_LONG).show()
        }


        token = savePrefs().get("token", "nullValue")
        groupId = savePrefs()["groupId", -1]
        userId = savePrefs()["userId", -1]
        groupName = savePrefs()["groupName"]


        //Definition RecyclerView
        groupDetailsRecyclerView = findViewById(R.id.groupDetailsRecyclerView)
        val groupDetailsLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        groupDetailsRecyclerView.layoutManager = groupDetailsLayoutManager

        loadData()

        updateGroupButton = findViewById(R.id.updateGroupButton)
        addContactGroupButton = findViewById(R.id.addGroupContactButton)

        searchTextGroupDetails = findViewById(R.id.searchTextGroupDetails)
        searchTextGroupDetails.addTextChangedListener(object : TextWatcher {
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
        for (item: GroupDetailsModel in groupDetailsModel!!) {
            if (item.name.lowercase().contains(text.lowercase()) || item.surname.lowercase()
                    .contains(text.lowercase())
            ) {
                filteredList!!.add(item)
            }
        }
        groupDetailsRecyclerViewAdapter!!.filterList(filteredList!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_group_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemView = item.itemId

        when (itemView) {
            R.id.addButtonGroupDetails -> {
//                Toast.makeText(applicationContext, "Person Added", Toast.LENGTH_LONG)
//                    .show()
                val intent2 = Intent(applicationContext, AddGroupContact::class.java)
                intent2.putExtra("id", userId)
                intent2.putExtra("groupId", groupId)
                intent2.putExtra("token", token)
                startActivity(intent2)
            }
            R.id.deleteButtonGroupDetails -> {
                Toast.makeText(
                    applicationContext,
                    "Person Deleted",
                    Toast.LENGTH_LONG
                ).show()
            }
            R.id.updateButtonGroupDetails -> {
                Toast.makeText(applicationContext, "Person Added", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(applicationContext, UpdateGroup::class.java)
                intent.putExtra("groupId", groupId)
                intent.putExtra("userId", userId)
                intent.putExtra("groupName", groupName)
                intent.putExtra("token", token)
                startActivity(intent)
            }
        }
        return false
    }


    val swipeGesture = object : SwipeGesture() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            when (direction) {
                ItemTouchHelper.LEFT -> {
                    //POST Function

                    // Create Retrofit
                    val retrofit = Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl("http://tmdcontacts-api.dev.tmd")
                        .build()

                    // Create Service
                    val service = retrofit.create(ContacsAPI::class.java)

                    val deleteGroupContactModel = DeleteGroupContactModel(
                        groupId!!,
                        groupDetailsModel!![viewHolder.absoluteAdapterPosition].id
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        // Do the POST request and get response
                        val response =
                            service.deleteGroupContact(
                                "Bearer " + token.toString(),
                                deleteGroupContactModel = deleteGroupContactModel
                            )

                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {

                                val gson = GsonBuilder().setPrettyPrinting().create()
                                val prettyJson = gson.toJson(
                                    JsonParser.parseString(
                                        response.body()
                                            ?.string()
                                    )
                                )

                                Toast.makeText(
                                    applicationContext,
                                    groupDetailsModel!![viewHolder.adapterPosition].name + "isimli kişi gruptan silindi.",
                                    Toast.LENGTH_LONG
                                ).show()
                                groupDetailsModel!!.removeAt(viewHolder.adapterPosition)
                                groupDetailsRecyclerViewAdapter!!.notifyDataSetChanged()
//                                recyclerViewAdapter!!.deleteItem(contactModels!![viewHolder.absoluteAdapterPosition].id)
//                                recyclerViewAdapter!!.notifyDataSetChanged()
                                Log.d("Pretty Printed JSON :", prettyJson)

                            } else {

                                Log.e("RETROFIT_ERROR", response.code().toString())

                            }
                        }
                    }

                }
            }

            super.onSwiped(viewHolder, direction)
        }
    }


    private fun loadData() {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        Log.e("GROUP ID: ", groupId.toString())

        val service = retrofit.create(ContacsAPI::class.java)
        val call = service.getDataGroupDetails("Bearer " + token.toString(), groupId = groupId!!)

        call.enqueue(object : Callback<List<GroupDetailsModel>> {
            override fun onResponse(
                call: Call<List<GroupDetailsModel>>,
                response: Response<List<GroupDetailsModel>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        groupDetailsModel = ArrayList(it)
                        groupDetailsModel.let { groupdetailsmodel ->
                            groupDetailsRecyclerViewAdapter =
                                RecViewAdapterGroupDetails(groupdetailsmodel!!)
                            groupDetailsRecyclerViewAdapter?.setListenerGroupDetails(object :
                                RecViewAdapterGroupDetails.ListenerGroupDetails {
                                override fun onItemClick(groupDetailsModel: GroupDetailsModel) {
                                    Toast.makeText(
                                        baseContext,
                                        groupDetailsModel.name,
                                        Toast.LENGTH_LONG
                                    ).show()

                                    savePrefs()["contactsId"] = groupDetailsModel.id

                                    val intent =
                                        Intent(applicationContext, Detail_Person::class.java)
                                    startActivity(intent)


                                }
                            })

                            val touchHelper = ItemTouchHelper(swipeGesture)
                            touchHelper.attachToRecyclerView(groupDetailsRecyclerView)
                            groupDetailsRecyclerView.adapter = groupDetailsRecyclerViewAdapter
                            groupDetailsRecyclerView.adapter = groupDetailsRecyclerViewAdapter

                        }
                    }

                }
            }

            override fun onFailure(call: Call<List<GroupDetailsModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })

    }

    fun updateGroup(view: View) {
        val intent = Intent(applicationContext, UpdateGroup::class.java)
        intent.putExtra("groupId", groupId)
        intent.putExtra("userId", userId)
        intent.putExtra("groupName", groupName)
        intent.putExtra("token", token)
        startActivity(intent)
    }

    fun addGroupContactButton(view: View) {
        val intent2 = Intent(applicationContext, AddGroupContact::class.java)
        intent2.putExtra("id", userId)
        intent2.putExtra("groupId", groupId)
        intent2.putExtra("token", token)
        startActivity(intent2)
    }

}