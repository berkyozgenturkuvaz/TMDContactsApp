package com.example.tmdcontactsapp.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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

                    replaceFragment(ContactsListFragment.newInstance()
                    )
                    return@OnNavigationItemSelectedListener true
                }
                R.id.groupsFragment -> {
                    replaceFragment(GroupsFragment()
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


//        val intent = intent
//        id = intent.getIntExtra("id", 0)
//        name = intent.getStringExtra("Name").toString()
//        surname = intent.getStringExtra("Surname").toString()
//        email = intent.getStringExtra("Email").toString()
//        address = intent.getStringExtra("Address").toString()
//        birthDate = intent.getStringExtra("BirthDate").toString()
//        tel = intent.getStringExtra("Tel").toString()
//        telBusiness = intent.getStringExtra("TelBusiness").toString()
//        telHome = intent.getStringExtra("TelHome").toString()
//        company = intent.getStringExtra("Company").toString()
//        title = intent.getStringExtra("Title").toString()
//        note = intent.getStringExtra("Note").toString()
//        userMail = intent.getStringExtra("userMail").toString()

//        val intent = intent
//        userMail = intent.getStringExtra("userMail").toString()


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

//        loadData()
//

//      var gson = Gson()
//      var jsonString = gson.toJson(userName)
//      var profileModel  = Gson().fromJson(userName, ProfileModel::class.java)

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

    /*//Get Profile Data using with email
    private fun loadData() {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ContacsAPI::class.java)
        val call =
            userMail?.let { service.getProfileData("Bearer " + token.toString(), email = it) }

        call?.enqueue(object : Callback<ProfileModel> {
            override fun onResponse(
                call: Call<ProfileModel>,
                response: Response<ProfileModel>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val profileModels = response.body()

                        // savePrefs()["userName"] = profileModels.toString()
                        // val stringModel = profileModels.toString()
                        // Log.e("String Model: ", stringModel)

                        //  var gson = Gson()
                        //  var jsonString = gson.toJson(stringModel)
                        //
                        //  var profilemodel = Gson().fromJson(jsonString, ProfileModel::class.java)
                        //  Log.e("String Model: ", profilemodel.name)


//                        val intent =
//                            Intent(applicationContext, BottomNavigationViewActivity::class.java)
//                        intent.putExtra("id", profileModels?.id)
//                        intent.putExtra("Name", profileModels?.name)
//                        intent.putExtra("Surname", profileModels?.surname)
//                        intent.putExtra("Email", profileModels?.email)
//                        intent.putExtra("Address", profileModels?.address)
//                        intent.putExtra("BirthDate", profileModels?.birthDate)
//                        intent.putExtra("Tel", profileModels?.tel)
//                        intent.putExtra("TelBusiness", profileModels?.telBusiness)
//                        intent.putExtra("TelHome", profileModels?.telHome)
//                        intent.putExtra("Company", profileModels?.company)
//                        intent.putExtra("Title", profileModels?.title)
//                        intent.putExtra("Note", profileModels?.note)
//                        intent.putExtra("token", token)
//                        startActivity(intent)

                    }


                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }*/



}

