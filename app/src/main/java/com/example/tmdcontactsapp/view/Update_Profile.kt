package com.example.tmdcontactsapp.view

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.tmdcontactsapp.model.UpdateProfileModel
import com.example.tmdcontactsapp.model.UserImageModel
import com.example.tmdcontactsapp.service.ContacsAPI
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream

class Update_Profile : AppCompatActivity() {

    var selectedPicture: Uri? = null
    var selectedBitmap: Bitmap? = null
    var imageDataString: String? = null
    lateinit var photoUpdateProfile: ImageView
    lateinit var updateProfileNameText: EditText
    lateinit var updateProfileSurnameText: EditText
    lateinit var updateProfileEmailText: EditText
    lateinit var updateProfileAddressText: EditText
    lateinit var updateProfileBirthdayText: EditText
    lateinit var updateProfileCellphoneText: EditText
    lateinit var updateProfileWorkphoneText: EditText
    lateinit var updateProfileHomephoneText: EditText
    lateinit var updateProfileCompanyText: EditText
    lateinit var updateProfileTitleText: EditText
    lateinit var updateProfileNoteText: EditText
    lateinit var updateProfileUpdateButton: Button
    lateinit var toolbar: Toolbar
    var Status: Boolean = true


    var userId: Int? = 0
    private var name: String? = null
    private var surname: String? = null
    private var email: String? = null
    private var address: String? = null
    private var birthDate: String? = null
    private var tel: String? = null
    private var telBusiness: String? = null
    private var telHome: String? = null
    private var company: String? = null
    private var title: String? = null
    private var note: String? = null
    private var photo: String? = ""
    private var token : String? = null
    private val BASE_URL = "http://tmdcontacts-api.dev.tmd"
    lateinit var sharedPreferences : SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        photoUpdateProfile = findViewById(R.id.photoUpdateProfile)
        updateProfileNameText = findViewById(R.id.updateProfileNameText)
        updateProfileSurnameText = findViewById(R.id.updateProfileSurnameText)
        updateProfileEmailText = findViewById(R.id.updateProfileEmailText)
        updateProfileAddressText = findViewById(R.id.updateProfileAddressText)
        updateProfileBirthdayText = findViewById(R.id.updateProfileBirthdayText)
        updateProfileCellphoneText = findViewById(R.id.updateProfileCellphoneText)
        updateProfileWorkphoneText = findViewById(R.id.updateProfileWorkphoneText)
        updateProfileHomephoneText = findViewById(R.id.updateProfileHomephoneText)
        updateProfileCompanyText = findViewById(R.id.updateProfileCompanyText)
        updateProfileTitleText = findViewById(R.id.updateProfileTitleText)
        updateProfileNoteText = findViewById(R.id.updateProfileNoteText)
        updateProfileUpdateButton = findViewById(R.id.updateProfileButton)


        val intent = intent
        userId = intent.getIntExtra("userId", 0)
        name = intent.getStringExtra("name")
        surname = intent.getStringExtra("surname")
        email = intent.getStringExtra("email")
        address = intent.getStringExtra("address")
        birthDate = intent.getStringExtra("birthdate")
        tel = intent.getStringExtra("tel")
        telBusiness = intent.getStringExtra("telBusiness")
        telHome = intent.getStringExtra("telHome")
        company = intent.getStringExtra("company")
        title = intent.getStringExtra("title")
        note = intent.getStringExtra("note")
        token = intent.getStringExtra("token")

        updateProfileNameText.setText(name)
        updateProfileSurnameText.setText(surname)
        updateProfileEmailText.setText(email)
        updateProfileAddressText.setText(address)
        updateProfileBirthdayText.setText(birthDate)
        updateProfileCellphoneText.setText(tel)
        updateProfileWorkphoneText.setText(telBusiness)
        updateProfileHomephoneText.setText(telHome)
        updateProfileCompanyText.setText(company)
        updateProfileTitleText.setText(title)
        updateProfileNoteText.setText(note)

        loadData()


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_title, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //Update Image Button Onclick Function
    fun choosePhotoUpdateProfile(view: View) {
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
                        photoUpdateProfile.setImageBitmap(selectedBitmap)
                    } else {
                        selectedBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, selectedPicture)
                        photoUpdateProfile.setImageBitmap(selectedBitmap)
                    }
                }
            } catch (e: Exception) {

            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    //Get User Image using with email
    private fun loadData() {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ContacsAPI::class.java)
        val call = email?.let { service.getUserImage("Bearer " + token.toString(), email = it) }

        call?.enqueue(object : Callback<UserImageModel> {
            override fun onResponse(
                call: Call<UserImageModel>,
                response: Response<UserImageModel>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val userImageModel = response.body()
                        photo = userImageModel?.photo

                        val imageBytes = Base64.decode(photo, Base64.DEFAULT)
                        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        photoUpdateProfile.setImageBitmap(decodedImage)

                    }

                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }

            override fun onFailure(call: Call<UserImageModel>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }

    //POST Function
    fun rawJSON() {

        //IMAGE POST
        val byteArrayOutputStream = ByteArrayOutputStream()
        selectedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()

        val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
        imageDataString = encoded
        Log.d("Image :", encoded)

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://tmdcontacts-api.dev.tmd")
            .build()

        // Create Service
        val service = retrofit.create(ContacsAPI::class.java)

        val updateProfileModel = UpdateProfileModel(
            userId!!,
            updateProfileNameText.text.toString(),
            updateProfileSurnameText.text.toString(),
            updateProfileEmailText.text.toString(),
            updateProfileAddressText.text.toString(),
            updateProfileBirthdayText.text.toString(),
            imageDataString!!,
            updateProfileCellphoneText.text.toString(),
            updateProfileWorkphoneText.text.toString(),
            updateProfileHomephoneText.text.toString(),
            updateProfileCompanyText.text.toString(),
            updateProfileTitleText.text.toString(),
            updateProfileNoteText.text.toString(),
            Status
        )
        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.updateProfile("Bearer " + token.toString() ,updateProfileModel = updateProfileModel)

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


                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())


                }
            }
        }
    }


    //check nullable variables from Update Button
    fun updateProfileButton(view: View) {
//        if( updateProfileNameText.text.isEmpty().apply{}){
//            Toast.makeText(
//                applicationContext,
//                "Name field can not be empty!",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//        else if( updateProfileSurnameText.text.isEmpty().apply{}){
//            Toast.makeText(
//                applicationContext,
//                "Surname field can not be empty!",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//        else if( updateProfileEmailText.text.isEmpty().apply{}){
//            Toast.makeText(
//                applicationContext,
//                "Email field can not be empty!",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//        else if( updateProfilePasswordText.text.isEmpty().apply{}){
//            Toast.makeText(
//                applicationContext,
//                "Password field can not be empty!",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//        else if( updateProfileConfirmPasswordText.text.isEmpty().apply{}){
//            Toast.makeText(
//                applicationContext,
//                "Password again field can not be empty!",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//        else if( updateProfileCellphoneText.text.isEmpty().apply{}){
//            Toast.makeText(
//                applicationContext,
//                "Cell phone number field can not be empty!",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//        else if( updateProfilePasswordText.text.toString() != updateProfileConfirmPasswordText.text.toString() ){
//            Toast.makeText(
//                applicationContext,
//                "Passwords must be same!",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//        else{
//            val intentHome = Intent(this, Profile::class.java)
//            startActivity(intentHome)
//        }
        rawJSON()
        //  val intentHome = Intent(applicationContext, BottomNavigationViewActivity::class.java)
        //startActivity(intentHome)
        //finish()

    }

    fun changePasswordButton(view: View) {
        val intent = Intent(applicationContext, ChangePassword::class.java)
        intent.putExtra("email", email.toString())
        intent.putExtra("token",token)
        startActivity(intent)
    }

}