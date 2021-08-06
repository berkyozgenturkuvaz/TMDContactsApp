package com.example.tmdcontactsapp.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.`class`.SwipeGesture
import com.example.tmdcontactsapp.adapter.RecyclerViewAdapterGroups
import com.example.tmdcontactsapp.model.GroupsModel
import com.example.tmdcontactsapp.service.ContacsAPI
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GroupsFragment : Fragment() {

    lateinit var groupsRecyclerView: RecyclerView
    val BASE_URL = "http://tmdcontacts-api.dev.tmd"
    private var groupsContactModels: ArrayList<GroupsModel>? = null
    private var filteredList: ArrayList<GroupsModel>? = ArrayList()
    var groupsRecyclerViewAdapter: RecyclerViewAdapterGroups? = null
    lateinit var searchTextGroup: EditText
    private var userId: Int? = 0
    private var token: String? = null
    lateinit var addGroupButton: Button


    companion object {
        @JvmStatic
        fun newInstance(userId: Int, token: String) =
            GroupsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, userId)
                    putString(ARG_PARAM2, token)
                }
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            userId = it.getInt(ARG_PARAM1)
            token = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.activity_groups, container, false)
        val groupsToolbar = view.findViewById<Toolbar>(R.id.toolbarMenu)
        (requireActivity() as AppCompatActivity).setSupportActionBar(groupsToolbar)

        //Definition RecyclerView
        groupsRecyclerView = view.findViewById(R.id.groupsRecyclerView)
        val groupsLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        groupsRecyclerView.layoutManager = groupsLayoutManager

        arguments?.let {
            userId = it.getInt(ARG_PARAM1)
            token = it.getString(ARG_PARAM2)

        }

        addGroupButton = view.findViewById(R.id.addGroupButton)
        addGroupButton.setOnClickListener {
            addGroupButton()
        }

        loadData()



        searchTextGroup = view.findViewById(R.id.searchTextGroup)
        searchTextGroup.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })

        return view
    }

    //item.surname.lowercase().contains(text.lowercase())
    fun filter(text: String) {
        filteredList!!.clear()
        for (item: GroupsModel in groupsContactModels!!) {
            if (item.name.lowercase().contains(text.lowercase())) {
                filteredList!!.add(item)
            }
        }

        groupsRecyclerViewAdapter!!.filterList(filteredList!!)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.main_menu, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.deleteButton -> {
//                Toast.makeText(requireActivity(), "Delete", Toast.LENGTH_LONG).show()
//            }
//            R.id.addButton -> {
//                Toast.makeText(requireActivity(), "Add", Toast.LENGTH_LONG).show()
//                val intent2 = Intent(context, AddGroup::class.java)
//                intent2.putExtra("userId", userId)
//                intent2.putExtra("token", token)
//                startActivity(intent2)
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

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

                    CoroutineScope(Dispatchers.IO).launch {
                        // Do the POST request and get response
                        val response =
                            service.deleteGroup(
                                "Bearer " + token.toString(),
                                id = groupsContactModels!![viewHolder.absoluteAdapterPosition].id
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
                                    context,
                                    groupsContactModels!![viewHolder.adapterPosition].name + "isimli grup silindi.",
                                    Toast.LENGTH_LONG
                                ).show()
                                groupsContactModels!!.removeAt(viewHolder.adapterPosition)
                                groupsRecyclerViewAdapter!!.notifyDataSetChanged()
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

        val service = retrofit.create(ContacsAPI::class.java)
        val call = service.getDataGroups("Bearer " + token.toString(), userId = userId!!)

        call.enqueue(object : Callback<List<GroupsModel>> {
            override fun onResponse(
                call: Call<List<GroupsModel>>,
                response: Response<List<GroupsModel>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        groupsContactModels = ArrayList(it)
                        groupsContactModels.let { groupsmodel ->
                            groupsRecyclerViewAdapter = RecyclerViewAdapterGroups(groupsmodel!!)
                            groupsRecyclerViewAdapter?.setListenerGroups(object :
                                RecyclerViewAdapterGroups.ListenerGroups {
                                override fun onItemClick(groupsModel: GroupsModel) {
                                    Toast.makeText(
                                        context,
                                        groupsModel.name,
                                        Toast.LENGTH_LONG
                                    ).show()
                                    val intent = Intent(context, GroupDetails::class.java)
                                    intent.putExtra("groupId", groupsModel.id)
                                    intent.putExtra("userId", userId)
                                    intent.putExtra("groupName", groupsModel.name)
                                    intent.putExtra("token", token)
                                    startActivity(intent)
                                }
                            })

                            val touchHelper = ItemTouchHelper(swipeGesture)
                            touchHelper.attachToRecyclerView(groupsRecyclerView)
                            groupsRecyclerView.adapter = groupsRecyclerViewAdapter
                            groupsRecyclerView.adapter = groupsRecyclerViewAdapter
                        }

                    }

                }
            }

            override fun onFailure(call: Call<List<GroupsModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })

    }

    fun addGroupButton() {
        val intent2 = Intent(context, AddGroup::class.java)
        intent2.putExtra("userId", userId)
        intent2.putExtra("token", token)
        startActivity(intent2)
    }

}