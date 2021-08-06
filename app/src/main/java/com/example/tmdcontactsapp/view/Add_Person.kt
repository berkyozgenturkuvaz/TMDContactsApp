package com.example.tmdcontactsapp.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tmdcontactsapp.R

class Add_Person : AppCompatActivity() {

    var selectedPicture: Uri? = null
    var selectedBitmap: Bitmap? = null
    lateinit var imageDataString: String
    lateinit var photoAddPerson: ImageView
    lateinit var addPersonNameText: EditText
    lateinit var addPersonSurnameText: EditText
    lateinit var addPersonEmailText: EditText
    lateinit var addPersonAddressText: EditText
    lateinit var addPersonBirthdayText: EditText
    lateinit var addPersonCellphoneText: EditText
    lateinit var addPersonWorkphoneText: EditText
    lateinit var addPersonHomephoneText: EditText
    lateinit var addPersonCompanyText: EditText
    lateinit var addPersonTitleText: EditText
    lateinit var addPersonNoteText: EditText
    lateinit var addPersonAddButton: Button
    lateinit var toolbar: Toolbar
    var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_person)

        photoAddPerson = findViewById(R.id.photoAddPerson)
        addPersonNameText = findViewById(R.id.addPersonNameText)
        addPersonSurnameText = findViewById(R.id.addPersonSurnameText)
        addPersonEmailText = findViewById(R.id.addPersonEmailText)
        addPersonAddressText = findViewById(R.id.addPersonAddressText)
        addPersonBirthdayText = findViewById(R.id.addPersonBirthdayText)
        addPersonCellphoneText = findViewById(R.id.addPersonCellphoneText)
        addPersonWorkphoneText = findViewById(R.id.addPersonWorkphoneText)
        addPersonHomephoneText = findViewById(R.id.addPersonHomephoneText)
        addPersonCompanyText = findViewById(R.id.addPersonCompanyText)
        addPersonTitleText = findViewById(R.id.addPersonTitleText)
        addPersonNoteText = findViewById(R.id.addPersonNoteText)
        addPersonAddButton = findViewById(R.id.addPersonAddButton)
        toolbar = findViewById(R.id.toolbarMenu)
        setSupportActionBar(toolbar)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_title, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Update Image Button Onclick Function
    fun choosePhotoAddPerson(view: View) {
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
                        photoAddPerson.setImageBitmap(selectedBitmap)
                    } else {
                        selectedBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, selectedPicture)
                        photoAddPerson.setImageBitmap(selectedBitmap)
                    }
                }
            } catch (e: Exception) {

            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    //POST Function
//    fun rawJSON() {
//
//        //IMAGE POST
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        selectedBitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
//
//        val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
//        imageDataString = encoded
//        Log.d("Image :", encoded)
//
//        // Create Retrofit
//        val retrofit = Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl("http://tmdcontacts-api.dev.tmd/api/Contacts/Add/")
//            .build()
//
//        // Create Service
//        val service = retrofit.create(ContacsAPI::class.java)
//
//        val addContactModel = AddContactModel(
//            addPersonNameText.text.toString(),
//            addPersonSurnameText.text.toString(),
//            addPersonEmailText.text.toString(),
//            addPersonAddressText.text.toString(),
//            addPersonBirthdayText.text.toString(),
//            imageDataString,
//            addPersonCellphoneText.text.toString(),
//            addPersonWorkphoneText.text.toString(),
//            addPersonHomephoneText.text.toString(),
//            addPersonCompanyText.text.toString(),
//            addPersonTitleText.text.toString(),
//            addPersonNoteText.text.toString(),
//            userId
//        )
//        CoroutineScope(Dispatchers.IO).launch {
//            // Do the POST request and get response
//            val response = service.addContact(addContactModel = addContactModel)
//
//            withContext(Dispatchers.Main) {
//                if (response.isSuccessful) {
//
//                    val gson = GsonBuilder().setPrettyPrinting().create()
//                    val prettyJson = gson.toJson(
//                        JsonParser.parseString(
//                            response.body()
//                                ?.string()
//                        )
//                    )
//
//                    Log.d("Pretty Printed JSON :", prettyJson)
//                    Log.d("Image :", encoded)
//
//                } else {
//
//                    Log.e("RETROFIT_ERROR", response.code().toString())
//                }
//            }
//        }
//    }

    //Check nullable variables from ADD Button
    fun addPersonButton(view: View) {
        if (addPersonNameText.text.isEmpty().apply {}) {
            Toast.makeText(
                applicationContext,
                "Name field can not be empty!",
                Toast.LENGTH_LONG
            ).show()
        } else if (addPersonSurnameText.text.isEmpty().apply {}) {
            Toast.makeText(
                applicationContext,
                "Surname field can not be empty!",
                Toast.LENGTH_LONG
            ).show()
        } else if (addPersonEmailText.text.isEmpty().apply {}) {
            Toast.makeText(
                applicationContext,
                "Email field can not be empty!",
                Toast.LENGTH_LONG
            ).show()
        } else if (addPersonCellphoneText.text.isEmpty().apply {}) {
            Toast.makeText(
                applicationContext,
                "Cell phone number field can not be empty!",
                Toast.LENGTH_LONG
            ).show()
        } else {
            //rawJSON()
            val intentHome = Intent(this, BottomNavigationViewActivity::class.java)
            startActivity(intentHome)
        }
    }

}