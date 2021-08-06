package com.example.tmdcontactsapp.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.adapter.RecyclerViewAdapterGroups
import com.example.tmdcontactsapp.model.GroupsModel

class Groups : AppCompatActivity() {

    lateinit var groupsRecyclerView: RecyclerView
    val BASE_URL = "https://60c88166afc88600179f7389.mockapi.io/User/"
    var groupsContactModels: ArrayList<GroupsModel>? = null
    var groupsDisplayList: ArrayList<GroupsModel>? = null
    var groupsRecyclerViewAdapter: RecyclerViewAdapterGroups? = null

    lateinit var textToolbar: TextView
    lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, ContactsListFragment())
                .commit()
        }

        //Definition RecyclerView
        groupsRecyclerView = findViewById(R.id.groupsRecyclerView)
        val groupsLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        groupsRecyclerView.layoutManager = groupsLayoutManager

        //loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_title, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemView = item.itemId

        when (itemView) {
            R.id.addButton -> Toast.makeText(applicationContext, "Person Added", Toast.LENGTH_LONG)
                .show()
            R.id.addButton -> Toast.makeText(
                applicationContext,
                "Person Deleted",
                Toast.LENGTH_LONG
            ).show()
        }

        return false
    }
}