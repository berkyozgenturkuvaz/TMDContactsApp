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
import com.example.tmdcontactsapp.model.ProfileModel
import com.example.tmdcontactsapp.model.UpdateProfileModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
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
    private var photo: String? = ""
    private var token: String? = null
    private var profilemodel: String? = null
    private var job: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        init()

        token = savePrefs().get("token", "nullValue")
        userId = savePrefs()["userId", -1]

        profilemodel = savePrefs()["userProfile", ""]
        val profilemodel: ProfileModel = Gson().fromJson(profilemodel, ProfileModel::class.java)
        Log.e("String Model: ", profilemodel.name.toString())

        updateProfileNameText.setText(profilemodel.name)
        updateProfileSurnameText.setText(profilemodel.surname)
        updateProfileEmailText.setText(profilemodel.email)
        updateProfileAddressText.setText(profilemodel.address)
        updateProfileBirthdayText.setText(profilemodel.birthDate)
        updateProfileCellphoneText.setText(profilemodel.tel)
        updateProfileWorkphoneText.setText(profilemodel.telBusiness)
        updateProfileHomephoneText.setText(profilemodel.telHome)
        updateProfileCompanyText.setText(profilemodel.company)
        updateProfileTitleText.setText(profilemodel.title)
        updateProfileNoteText.setText(profilemodel.note)
        photo = profilemodel.photo

        val imageBytes = Base64.decode(photo, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        photoUpdateProfile.setImageBitmap(decodedImage)

    }

    fun init() {
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


    //POST Function
    fun rawJSON() {

        //IMAGE POST
        val byteArrayOutputStream = ByteArrayOutputStream()
        selectedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()

        val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
        imageDataString = encoded
        Log.d("Image :", encoded)

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

        job = CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = RetrofitOperations.instance.updateProfile(
                "Bearer " + token.toString(),
                updateProfileModel = updateProfileModel
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
        val intentHome = Intent(applicationContext, BottomNavigationViewActivity::class.java)
        startActivity(intentHome)

    }

    fun changePasswordButton(view: View) {
        val intent = Intent(applicationContext, ChangePassword::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}