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
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.R.layout.activity_register
import com.example.tmdcontactsapp.`class`.RetrofitOperations
import com.example.tmdcontactsapp.model.RegisterModel
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream


class activity_register : AppCompatActivity() {

    private var job : Job? = null
    var selectedPicture: Uri? = null
    var selectedBitmap: Bitmap? = null
    lateinit var imageDataString: String
    lateinit var topTextRegister: TextView
    lateinit var photoRegister: ImageView
    lateinit var nameTextRegister: EditText
    lateinit var surnameTextRegister: EditText
    lateinit var emailTextRegister: EditText
    lateinit var passwordTextRegister: EditText
    lateinit var confirmPasswordTextRegister: EditText
    lateinit var addressTextRegister: EditText
    lateinit var birthdayTextRegister: EditText
    lateinit var cellPhoneTextRegister: EditText
    lateinit var workPhoneTextRegister: EditText
    lateinit var homePhoneTextRegister: EditText
    lateinit var companyTextRegister: EditText
    lateinit var titleTextRegister: EditText
    lateinit var noteTextRegister: EditText
    lateinit var registerButton: Button
    var Status: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_register)

        init()

    }

    //Initialize function
    fun init() {
        topTextRegister = findViewById(R.id.topTextRegister)
        photoRegister = findViewById(R.id.photoRegister)
        nameTextRegister = findViewById(R.id.nameTextRegister)
        surnameTextRegister = findViewById(R.id.surnameTextRegister)
        emailTextRegister = findViewById(R.id.emailTextRegister)
        passwordTextRegister = findViewById(R.id.passwordTextRegister)
        confirmPasswordTextRegister = findViewById(R.id.confirmPasswordTextRegister)
        addressTextRegister = findViewById(R.id.addressTextRegister)
        birthdayTextRegister = findViewById(R.id.birthdayTextRegister)
        cellPhoneTextRegister = findViewById(R.id.cellPhoneTextRegister)
        workPhoneTextRegister = findViewById(R.id.workPhoneTextRegister)
        homePhoneTextRegister = findViewById(R.id.homePhoneTextRegister)
        companyTextRegister = findViewById(R.id.companyTextRegister)
        titleTextRegister = findViewById(R.id.titleTextRegister)
        noteTextRegister = findViewById(R.id.noteTextRegister)
        registerButton = findViewById(R.id.registerButton)


        topTextRegister.setOnClickListener() {
            val intentLogin = Intent(this, MainActivity::class.java)
            startActivity(intentLogin)
        }
    }


    //Update Image Button Onclick Function
    fun choosePhotoRegister(view: View) {
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
                        photoRegister.setImageBitmap(selectedBitmap)
                    } else {
                        selectedBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, selectedPicture)
                        photoRegister.setImageBitmap(selectedBitmap)
                    }
                }
            } catch (e: Exception) {

            }

        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    //POST Function
    fun rawJSON() {

        //IMAGE POST
        val byteArrayOutputStream = ByteArrayOutputStream()
        selectedBitmap?.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()

        val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
        imageDataString = encoded
        Log.d("Image :", encoded)

        val registerModel = RegisterModel(
            nameTextRegister.text.toString(),
            surnameTextRegister.text.toString(),
            emailTextRegister.text.toString(),
            passwordTextRegister.text.toString(),
            addressTextRegister.text.toString(),
            birthdayTextRegister.text.toString(),
            imageDataString,
            cellPhoneTextRegister.text.toString(),
            workPhoneTextRegister.text.toString(),
            homePhoneTextRegister.text.toString(),
            companyTextRegister.text.toString(),
            titleTextRegister.text.toString(),
            noteTextRegister.text.toString(),
            Status
        )

        job = CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = RetrofitOperations.instance.createUser(registerModel = registerModel)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string()
                        )
                    )
                    Log.d("Pretty Printed JSON :", prettyJson)
                    Log.d("Image :", encoded)
                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
    }

    //Check with Sign Up Button
    fun signUp(view: View) {

        if (nameTextRegister.text.isEmpty().apply {}) {
            Toast.makeText(
                applicationContext,
                "Name field can not be empty!",
                Toast.LENGTH_LONG
            ).show()
        } else if (surnameTextRegister.text.isEmpty().apply {}) {
            Toast.makeText(
                applicationContext,
                "Surname field can not be empty!",
                Toast.LENGTH_LONG
            ).show()
        } else if (emailTextRegister.text.isEmpty().apply {}) {
            Toast.makeText(
                applicationContext,
                "Email field can not be empty!",
                Toast.LENGTH_LONG
            ).show()
        } else if (passwordTextRegister.text.isEmpty().apply {}) {
            Toast.makeText(
                applicationContext,
                "Password field can not be empty!",
                Toast.LENGTH_LONG
            ).show()
        } else if (confirmPasswordTextRegister.text.isEmpty().apply {}) {
            Toast.makeText(
                applicationContext,
                "Password again field can not be empty!",
                Toast.LENGTH_LONG
            ).show()
        } else if (cellPhoneTextRegister.text.isEmpty().apply {}) {
            Toast.makeText(
                applicationContext,
                "Cell phone number field can not be empty!",
                Toast.LENGTH_LONG
            ).show()
        } else if (passwordTextRegister.text.toString() != confirmPasswordTextRegister.text.toString()) {
            Toast.makeText(
                applicationContext,
                "Passwords must be same!",
                Toast.LENGTH_LONG
            ).show()
        } else {

            rawJSON()

            val intentHome = Intent(this, BottomNavigationViewActivity::class.java)
            startActivity(intentHome)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}