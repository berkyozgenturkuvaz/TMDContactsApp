package com.example.tmdcontactsapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.`class`.Preferences.get
import com.example.tmdcontactsapp.`class`.Preferences.savePrefs
import com.example.tmdcontactsapp.`class`.RetrofitOperations
import kotlinx.coroutines.*

class Detail_Person : AppCompatActivity() {

    lateinit var detailPersonImageView: ImageView
    lateinit var detailPersonNameText: TextView
    lateinit var detailPersonSurnameText: TextView
    lateinit var detailPersonEmailText: TextView
    lateinit var detailPersonAddressText: TextView
    lateinit var detailPersonBirthdayText: TextView
    lateinit var detailPersonCellphoneText: TextView
    lateinit var detailPersonWorkphoneText: TextView
    lateinit var detailPersonHomephoneText: TextView
    lateinit var detailPersonCompanyText: TextView
    lateinit var detailPersonTitleText: TextView
    lateinit var detailPersonNoteText: TextView
    lateinit var detailPersonGroupText: TextView

    private var photo: String? = ""
    private var token: String? = null
    lateinit var toolbar: Toolbar
    var id: Int? = 0
    var userId: Int? = 0
    private var job: Job? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_person)

        init()

        token = savePrefs().get("token", "nullValue")
        id = savePrefs()["contactsId", -1]
        userId = savePrefs()["userId", -1]


        loadDataContact()

    }

    fun init() {
        detailPersonImageView = findViewById(R.id.detailPersonImageView)
        detailPersonNameText = findViewById(R.id.detailPersonNameText)
        detailPersonSurnameText = findViewById(R.id.detailPersonSurnameText)
        detailPersonEmailText = findViewById(R.id.detailPersonEmailText)
        detailPersonAddressText = findViewById(R.id.detailPersonAddressText)
        detailPersonBirthdayText = findViewById(R.id.detailPersonBirthdayText)
        detailPersonCellphoneText = findViewById(R.id.detailPersonCellphoneText)
        detailPersonWorkphoneText = findViewById(R.id.detailPersonWorkphoneText)
        detailPersonHomephoneText = findViewById(R.id.detailPersonHomephoneText)
        detailPersonCompanyText = findViewById(R.id.detailPersonCompanyText)
        detailPersonTitleText = findViewById(R.id.detailPersonTitleText)
        detailPersonNoteText = findViewById(R.id.detailPersonNoteText)
//        detailPersonGroupText = findViewById(R.id.detailPersonGroupText)
        toolbar = findViewById(R.id.toolbarDetailPerson)
        setSupportActionBar(toolbar)

    }

    //Get Profile Data using with email
    private fun loadDataContact() {

        job = CoroutineScope(Dispatchers.IO).launch {

            val response = id?.let {
                RetrofitOperations.instance.getContactData(
                    "Bearer " + token.toString(),
                    contactId = it
                )
            }

            withContext(Dispatchers.Main) {
                if (response?.isSuccessful!!) {
                    response.body()?.let {
                        val profileModels = response.body()

                        detailPersonNameText.text = profileModels?.name.toString()
                        detailPersonSurnameText.text = profileModels?.surname.toString()
                        detailPersonEmailText.text = profileModels?.email.toString()
                        detailPersonAddressText.text = profileModels?.address.toString()
                        detailPersonBirthdayText.text = profileModels?.birthDate.toString()
                        detailPersonCellphoneText.text = profileModels?.tel.toString()
                        detailPersonWorkphoneText.text = profileModels?.telBusiness.toString()
                        detailPersonHomephoneText.text = profileModels?.telHome.toString()
                        detailPersonCompanyText.text = profileModels?.company.toString()
                        detailPersonTitleText.text = profileModels?.title.toString()
                        detailPersonNoteText.text = profileModels?.note.toString()
                        photo = profileModels?.photo.toString()

                        val imageBytes = Base64.decode(photo, Base64.DEFAULT)
                        val decodedImage =
                            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        detailPersonImageView.setImageBitmap(decodedImage)

                    }

                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_title, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.updateContact -> {
                val intent = Intent(applicationContext, Update_Person::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}