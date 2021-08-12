package com.example.tmdcontactsapp.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.`class`.Preferences.get
import com.example.tmdcontactsapp.`class`.Preferences.savePrefs
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.toolbar_items.*

class BottomNavigationViewActivity : AppCompatActivity() {

    var id: Int? = null
    lateinit var name: String
    lateinit var surname: String
    lateinit var email: String
    lateinit var address: String
    lateinit var birthDate: String
    lateinit var photo: String
    lateinit var tel: String
    lateinit var telBusiness: String
    lateinit var telHome: String
    lateinit var company: String
    lateinit var title: String
    lateinit var note: String
    val bundle = Bundle()
    lateinit var token: String
    private var userMail: String? = null


    private val navigationItemSelected =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.contactListFragment -> {

                    replaceFragment(
                        ContactsListFragment.newInstance()
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.groupsFragment -> {
                    replaceFragment(
                        GroupsFragment()
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.addPersonFragment -> {

                    replaceFragment(
                        AddPersonFragment.newInstance()
                    )

                    return@OnNavigationItemSelectedListener true
                }
                R.id.profileFragment -> {
                    replaceFragment(
                        ProfileFragment.newInstance(
                        )
                    )
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation_view)

        token = savePrefs()["token", "value"]
        userMail = savePrefs()["userMail", "value"]

        addIcon.setOnClickListener {
            Log.e("TOOLBAR", "name")
        }

        updateIcon.setOnClickListener {
            Log.e("TOOLBAR", "deneme")
        }

        logoutIcon.setOnClickListener {
            Log.e("TOOLBAR", "email")
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)

        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelected)
        replaceFragment(
            ContactsListFragment.newInstance(

            )

        )

    }

    fun replaceFragment(fragment: Fragment) {
        // val profileFragment: ProfileFragment = ProfileFragment.newInstance(nameProfile)
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.fragment, fragment)
        fragmentTransaction.commit()
    }
}

