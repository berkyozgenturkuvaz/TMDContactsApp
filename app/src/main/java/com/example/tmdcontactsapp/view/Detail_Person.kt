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
import com.example.tmdcontactsapp.model.ProfileModel
import com.example.tmdcontactsapp.service.ContacsAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    private val BASE_URL = "http://tmdcontacts-api.dev.tmd"
    private var photo: String? = ""
    private var token: String? = null
    lateinit var toolbar: Toolbar
    var id: Int? = 0
    var userId: Int? = 0


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_person)

        init()

        token = savePrefs().get("token","nullValue")
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

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ContacsAPI::class.java)
        val call = id?.let { service.getContactData("Bearer " + token.toString(), contactId = it) }

        call?.enqueue(object : Callback<ProfileModel> {
            override fun onResponse(
                call: Call<ProfileModel>,
                response: Response<ProfileModel>
            ) {
                if (response.isSuccessful) {
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

            override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_title, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.updateContact -> {
                val intent = Intent(applicationContext, Update_Person::class.java)
//                intent.putExtra("id", id)
//                intent.putExtra("name", name)
//                intent.putExtra("surname", surname)
//                intent.putExtra("email", email)
//                intent.putExtra("address", address)
//                intent.putExtra("birthdate", birthdate)
//                intent.putExtra("tel", tel)
//                intent.putExtra("telBusiness", telBusiness)
//                intent.putExtra("telHome", telHome)
//                intent.putExtra("company", company)
//                intent.putExtra("title", title)
//                intent.putExtra("note", note)
//                intent.putExtra("token", token)
//                intent.putExtra("userId", userId)
//                intent.putExtra("contactId", idFromGroup)
//
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}