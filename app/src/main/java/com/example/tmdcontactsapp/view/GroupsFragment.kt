package com.example.tmdcontactsapp.view

import android.annotation.SuppressLint
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
import com.example.tmdcontactsapp.`class`.Preferences.get
import com.example.tmdcontactsapp.`class`.Preferences.savePrefs
import com.example.tmdcontactsapp.`class`.Preferences.set
import com.example.tmdcontactsapp.`class`.RetrofitOperations
import com.example.tmdcontactsapp.`class`.SwipeGesture
import com.example.tmdcontactsapp.adapter.RecyclerViewAdapterGroups
import com.example.tmdcontactsapp.model.GroupsModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GroupsFragment : Fragment() {

    private var job: Job? = null
    lateinit var groupsRecyclerView: RecyclerView
    private var groupsContactModels: ArrayList<GroupsModel>? = null
    private var filteredList: ArrayList<GroupsModel>? = ArrayList()
    var groupsRecyclerViewAdapter: RecyclerViewAdapterGroups? = null
    lateinit var searchTextGroup: EditText
    private var userId: Int? = 0
    private var token: String? = null
    lateinit var addGroupButton: Button


    companion object {
        @JvmStatic
        fun newInstance() =
            GroupsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {

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

        token = context?.savePrefs()?.get("token", "value")
        userId = context?.savePrefs()?.get("userId", -1)

        //Definition RecyclerView
        groupsRecyclerView = view.findViewById(R.id.groupsRecyclerView)
        val groupsLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        groupsRecyclerView.layoutManager = groupsLayoutManager

        //Initiliaze Variable
        addGroupButton = view.findViewById(R.id.addGroupButton)
        addGroupButton.setOnClickListener {
            addGroupButton()
        }

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

        loadData()

        return view
    }


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

    val swipeGesture = object : SwipeGesture() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            when (direction) {
                ItemTouchHelper.LEFT -> {
                    //POST Function

                    job = CoroutineScope(Dispatchers.IO).launch {
                        // Do the POST request and get response
                        val response =
                            RetrofitOperations.instance.deleteGroup(
                                "Bearer " + token.toString(),
                                id = if (filteredList!!.isEmpty()) {
                                    groupsContactModels!![viewHolder.absoluteAdapterPosition].id
                                } else {
                                    filteredList!![viewHolder.absoluteAdapterPosition].id
                                }
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

                                if (filteredList!!.isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        groupsContactModels!![viewHolder.adapterPosition].name + " isimli grup silindi.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        filteredList!![viewHolder.adapterPosition].name + " isimli grup silindi.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                if (filteredList!!.isEmpty()) {
                                    groupsContactModels!!.removeAt(viewHolder.adapterPosition)
                                } else {
                                    filteredList!!.removeAt(viewHolder.adapterPosition)
                                }

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

        job = CoroutineScope(Dispatchers.IO).launch {

            val response = RetrofitOperations.instance.getDataGroups(
                "Bearer " + token.toString(),
                userId = userId!!
            )

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        groupsContactModels = ArrayList(it)
                        groupsContactModels.let { groupsmodel ->
                            groupsRecyclerViewAdapter = RecyclerViewAdapterGroups(groupsmodel!!)
                            groupsRecyclerViewAdapter?.setListenerGroups(object :
                                RecyclerViewAdapterGroups.ListenerGroups {
                                override fun onItemClick(groupsModel: GroupsModel) {
                                    Toast.makeText(context, groupsModel.name, Toast.LENGTH_LONG)
                                        .show()

                                    context?.savePrefs()?.set("groupId", groupsModel.id)
                                    context?.savePrefs()?.set("groupName", groupsModel.name)

                                    val intent = Intent(context, GroupDetails::class.java)
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
        }
    }

    fun addGroupButton() {
        val intent2 = Intent(context, AddGroup::class.java)
        startActivity(intent2)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}