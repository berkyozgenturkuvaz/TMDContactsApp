package com.example.tmdcontactsapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity_Home : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, ContactsListFragment())
                .commit()

        }
    }

}