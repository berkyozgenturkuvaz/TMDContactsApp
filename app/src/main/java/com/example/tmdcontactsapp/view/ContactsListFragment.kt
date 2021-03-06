package com.example.tmdcontactsapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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
import com.example.tmdcontactsapp.adapter.RecyclerViewAdapter
import com.example.tmdcontactsapp.model.ContactsModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*

private const val ARG_PARAM5 = "param5"
private const val ARG_PARAM6 = "param6"


class ContactsListFragment : Fragment() {

    private var contactModels: ArrayList<ContactsModel>? = null
    private var filteredList: ArrayList<ContactsModel>? = ArrayList()
    lateinit var homeRecyclerView: RecyclerView
    private var recyclerViewAdapter: RecyclerViewAdapter? = null
    private var job: Job? = null

    lateinit var searchText: EditText
    private var userId: Int? = 0
    private var name: String? = null
    private var surname: String? = null
    private var photo: String? = ""

    lateinit var homeNameText: TextView
    lateinit var homeSurnameText: TextView
    lateinit var contactImage: ImageView

    private var userMail: String? = null
    private var token: String? = null


    companion object {
        @JvmStatic
        fun newInstance() =
            ContactsListFragment().apply {
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
        val view: View = inflater.inflate(R.layout.activity_main_home, container, false)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbarMenu)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        token = context?.savePrefs()?.get("token", "value")
        userMail = context?.savePrefs()?.get("userMail", "value")

        //Definition RecyclerView
        homeRecyclerView = view.findViewById(R.id.homeRecyclerView)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        homeRecyclerView.layoutManager = layoutManager

        homeNameText = view.findViewById(R.id.homeNameText)
        homeSurnameText = view.findViewById(R.id.homeSurnameText)
        contactImage = view.findViewById(R.id.contactImage)

        loadData()

        searchText = view.findViewById(R.id.searchText)
        searchText.addTextChangedListener(object : TextWatcher {
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

        return view
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


    override fun onStart() {
        super.onStart()
        homeNameText.text = name.toString()
        homeSurnameText.text = surname.toString()

        val imageBytes = Base64.decode(photo, 0)
        val decodedImage =
            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        contactImage.setImageBitmap(decodedImage)
    }


    //Get Profile Data using with email
    private fun loadData() {

        job = CoroutineScope(Dispatchers.IO).launch {

            val response = RetrofitOperations.instance.getProfileData(
                "Bearer " + token.toString(),
                email = userMail.toString()
            )

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val profileModels = response.body()
                        userId = profileModels?.id
                        name = profileModels?.name
                        surname = profileModels?.surname
                        photo = profileModels?.photo

                        context?.savePrefs()?.set("userId", profileModels?.id)

                        loadDataContact()
                        onStart()

                    }
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }

        }
    }

    //Delete Item From RecList
    val swipeGesture = object : SwipeGesture() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            when (direction) {
                ItemTouchHelper.LEFT -> {
                    //POST Function

                    job = CoroutineScope(Dispatchers.IO).launch {
                        // Do the POST request and get response
                        val response =
                            RetrofitOperations.instance.deleteContact(
                                "Bearer " + token.toString(),
                                id = if (filteredList!!.isEmpty()) {
                                    contactModels!![viewHolder.absoluteAdapterPosition].id
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
                                        contactModels!![viewHolder.adapterPosition].name + " isimli kullan??c?? silindi.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        filteredList!![viewHolder.adapterPosition].name + " isimli kullan??c?? silindi.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }


                                if (filteredList!!.isEmpty()) {
                                    contactModels!!.removeAt(viewHolder.adapterPosition)
                                } else {
                                    filteredList!!.removeAt(viewHolder.adapterPosition)
                                }

                                recyclerViewAdapter!!.notifyDataSetChanged()
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

    private fun loadDataContact() {

        job = CoroutineScope(Dispatchers.IO).launch {
            val response = userId?.let {
                RetrofitOperations.instance.getData(
                    "Bearer " + token.toString(),
                    userId = it
                )
            }

            withContext(Dispatchers.Main) {

                if (response?.isSuccessful!!) {
                    response.body()?.let {
                        contactModels = ArrayList(it)
                        contactModels?.let { contactsmodel ->
                            recyclerViewAdapter = RecyclerViewAdapter(contactsmodel)
                            recyclerViewAdapter?.setType(RecyclerViewAdapter.VIEW_TYPE_ONE)
                            recyclerViewAdapter?.setListener(object :
                                RecyclerViewAdapter.Listener {
                                override fun onItemClick(contactsModel: ContactsModel) {
                                    Toast.makeText(
                                        context,
                                        contactsModel.name,
                                        Toast.LENGTH_LONG
                                    ).show()

                                    context?.savePrefs()?.set("contactsId", contactsModel.id)

                                    val intent = Intent(context, Detail_Person::class.java)
                                    startActivity(intent)
                                }

                            })
                            val touchHelper = ItemTouchHelper(swipeGesture)
                            touchHelper.attachToRecyclerView(homeRecyclerView)
                            homeRecyclerView.adapter = recyclerViewAdapter
                        }
                    }
                }

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}