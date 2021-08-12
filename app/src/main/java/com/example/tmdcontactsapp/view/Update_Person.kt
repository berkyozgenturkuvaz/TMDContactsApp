package com.example.tmdcontactsapp.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.`class`.Preferences.get
import com.example.tmdcontactsapp.`class`.Preferences.savePrefs
import com.example.tmdcontactsapp.`class`.RetrofitOperations
import com.example.tmdcontactsapp.model.UpdateContactModel
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream

class Update_Person : AppCompatActivity() {

    var selectedPicture: Uri? = null
    var selectedBitmap: Bitmap? = null
    var imageDataString: String? = null
    lateinit var photoUpdatePerson: ImageView
    lateinit var updatePersonNameText: EditText
    lateinit var updatePersonSurnameText: EditText
    lateinit var updatePersonEmailText: EditText
    lateinit var updatePersonAddressText: EditText
    lateinit var updatePersonBirthdayText: EditText
    lateinit var updatePersonCellphoneText: EditText
    lateinit var updatePersonWorkphoneText: EditText
    lateinit var updatePersonHomephoneText: EditText
    lateinit var updatePersonCompanyText: EditText
    lateinit var updatePersonTitleText: EditText
    lateinit var updatePersonNoteText: EditText
    lateinit var updatePersonUpdateButton: Button
    lateinit var toolbar: Toolbar


    var id: Int? = 0
    var userId: Int? = 0
    private var job: Job? = null

    lateinit var name: String
    lateinit var surname: String
    lateinit var email: String
    lateinit var address: String
    lateinit var birthdate: String
    lateinit var tel: String
    lateinit var telBusiness: String
    lateinit var telHome: String
    lateinit var company: String
    lateinit var title: String
    lateinit var note: String
    private var photo: String? = ""
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_person)

        init()
        loadDataContact()

    }

    fun init() {
        photoUpdatePerson = findViewById(R.id.photoUpdatePerson)
        updatePersonNameText = findViewById(R.id.updatePersonNameText)
        updatePersonSurnameText = findViewById(R.id.updatePersonSurnameText)
        updatePersonEmailText = findViewById(R.id.updatePersonEmailText)
        updatePersonAddressText = findViewById(R.id.updatePersonAddressText)
        updatePersonBirthdayText = findViewById(R.id.updatePersonBirthdayText)
        updatePersonCellphoneText = findViewById(R.id.updatePersonCellphoneText)
        updatePersonWorkphoneText = findViewById(R.id.updatePersonWorkphoneText)
        updatePersonHomephoneText = findViewById(R.id.updatePersonHomephoneText)
        updatePersonCompanyText = findViewById(R.id.updatePersonCompanyText)
        updatePersonTitleText = findViewById(R.id.updatePersonTitleText)
        updatePersonNoteText = findViewById(R.id.updatePersonNoteText)
        updatePersonUpdateButton = findViewById(R.id.updatePersonUpdateButton)
        toolbar = findViewById(R.id.toolbarUpdateContact)
        setSupportActionBar(toolbar)

        id = savePrefs()["contactsId", -1]
        token = savePrefs().get("token", "nullValue")
        userId = savePrefs()["userId", -1]
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_title, menu)
        return super.onCreateOptionsMenu(menu)
    }


    //Update Image Button Onclick Function
    fun choosePhotoUpdatePerson(view: View) {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            val intentToGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intentToGallery, 2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intentToGallery, 2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {

            selectedPicture = data.data

            try {

                if (selectedPicture != null) {

                    if (Build.VERSION.SDK_INT >= 28) {
                        val source =
                            ImageDecoder.createSource(this.contentResolver, selectedPicture!!)
                        selectedBitmap = ImageDecoder.decodeBitmap(source)
                        photoUpdatePerson.setImageBitmap(selectedBitmap)
                    } else {
                        selectedBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, selectedPicture)
                        photoUpdatePerson.setImageBitmap(selectedBitmap)
                    }
                }
            } catch (e: Exception) {

            }
        }

        super.onActivityResult(requestCode, resultCode, data)
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

                        updatePersonNameText.setText(profileModels?.name)
                        updatePersonSurnameText.setText(profileModels?.surname)
                        updatePersonEmailText.setText(profileModels?.email)
                        updatePersonAddressText.setText(profileModels?.address)
                        updatePersonBirthdayText.setText(profileModels?.birthDate)
                        updatePersonCellphoneText.setText(profileModels?.tel)
                        updatePersonWorkphoneText.setText(profileModels?.telBusiness)
                        updatePersonHomephoneText.setText(profileModels?.telHome)
                        updatePersonCompanyText.setText(profileModels?.company)
                        updatePersonTitleText.setText(profileModels?.title)
                        updatePersonNoteText.setText(profileModels?.note)


                        photo = profileModels?.photo.toString()

                        val imageBytes = Base64.decode(photo, Base64.DEFAULT)
                        val decodedImage =
                            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        photoUpdatePerson.setImageBitmap(decodedImage)

                    }

                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }

    //POST Function
    fun rawJSON() {

        job = CoroutineScope(Dispatchers.IO).launch {

            //IMAGE POST
            val byteArrayOutputStream = ByteArrayOutputStream()
            selectedBitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()

            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            imageDataString = encoded
            Log.d("Image :", encoded)

            val updateContactModel = UpdateContactModel(
                id!!,
                updatePersonNameText.text.toString(),
                updatePersonSurnameText.text.toString(),
                updatePersonEmailText.text.toString(),
                updatePersonAddressText.text.toString(),
                updatePersonBirthdayText.text.toString(),
                imageDataString!!,
                updatePersonCellphoneText.text.toString(),
                updatePersonWorkphoneText.text.toString(),
                updatePersonHomephoneText.text.toString(),
                updatePersonCompanyText.text.toString(),
                updatePersonTitleText.text.toString(),
                updatePersonNoteText.text.toString()
            )

            // Do the POST request and get response
            val response = RetrofitOperations.instance.updateContact(
                "Bearer " + token.toString(),
                updateContactModel = updateContactModel
            )

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        //       JsonParser.parseString(
                        response.body()
                            ?.string()
                    )
                    //  )

                    Log.d("Pretty Printed JSON :", prettyJson)
                    Log.d("Image :", encoded)

                    val intent = Intent(applicationContext, Detail_Person::class.java)
                    startActivity(intent)

                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }

    fun updatePersonButton(view: View) {
        rawJSON()
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}