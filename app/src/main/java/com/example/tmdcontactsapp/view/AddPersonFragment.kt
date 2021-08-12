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
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.`class`.Preferences.get
import com.example.tmdcontactsapp.`class`.Preferences.savePrefs
import com.example.tmdcontactsapp.`class`.RetrofitOperations
import com.example.tmdcontactsapp.model.AddContactModel
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AddPersonFragment : Fragment() {


    private var job: Job? = null
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

    private var userId: Int? = null
    private var token: String? = null

    companion object {
        @JvmStatic
        fun newInstance() =
            AddPersonFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.activity_add_person, container, false)

        photoAddPerson = view.findViewById(R.id.photoAddPerson)
        addPersonNameText = view.findViewById(R.id.addPersonNameText)
        addPersonSurnameText = view.findViewById(R.id.addPersonSurnameText)
        addPersonEmailText = view.findViewById(R.id.addPersonEmailText)
        addPersonAddressText = view.findViewById(R.id.addPersonAddressText)
        addPersonBirthdayText = view.findViewById(R.id.addPersonBirthdayText)
        addPersonCellphoneText = view.findViewById(R.id.addPersonCellphoneText)
        addPersonWorkphoneText = view.findViewById(R.id.addPersonWorkphoneText)
        addPersonHomephoneText = view.findViewById(R.id.addPersonHomephoneText)
        addPersonCompanyText = view.findViewById(R.id.addPersonCompanyText)
        addPersonTitleText = view.findViewById(R.id.addPersonTitleText)
        addPersonNoteText = view.findViewById(R.id.addPersonNoteText)
        addPersonAddButton = view.findViewById(R.id.addPersonAddButton)

        token = context?.savePrefs()?.get("token", "nullValue")
        userId = context?.savePrefs()?.get("userId", -1)


        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarMenu)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)


        //Check nullable variables from ADD Button
        addPersonAddButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (addPersonNameText.text.isEmpty().apply {}) {
                    Toast.makeText(
                        context,
                        "Name field can not be empty!",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (addPersonSurnameText.text.isEmpty().apply {}) {
                    Toast.makeText(
                        context,
                        "Surname field can not be empty!",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (addPersonEmailText.text.isEmpty().apply {}) {
                    Toast.makeText(
                        context,
                        "Email field can not be empty!",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (addPersonCellphoneText.text.isEmpty().apply {}) {
                    Toast.makeText(
                        context,
                        "Cell phone number field can not be empty!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    rawJSON()
                }
            }
        })


        //Choose a photo Button
        photoAddPerson.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (ContextCompat.checkSelfPermission(
                        context!!,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        1
                    )
                } else {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intentToGallery, 2)
                }

            }
        })


        return view

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_title, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //Update Image Button Onclick Function
    fun choosePhotoAddPerson(view: View) {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
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
                            context?.contentResolver?.let {
                                ImageDecoder.createSource(
                                    it,
                                    selectedPicture!!
                                )
                            }
                        selectedBitmap = source?.let { ImageDecoder.decodeBitmap(it) }
                        photoAddPerson.setImageBitmap(selectedBitmap)
                    } else {
                        selectedBitmap =
                            MediaStore.Images.Media.getBitmap(
                                context?.contentResolver,
                                selectedPicture
                            )
                        photoAddPerson.setImageBitmap(selectedBitmap)
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
        selectedBitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()

        val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
        imageDataString = encoded

        val addContactModel = AddContactModel(
            addPersonNameText.text.toString(),
            addPersonSurnameText.text.toString(),
            addPersonEmailText.text.toString(),
            addPersonAddressText.text.toString(),
            addPersonBirthdayText.text.toString(),
            imageDataString,
            addPersonCellphoneText.text.toString(),
            addPersonWorkphoneText.text.toString(),
            addPersonHomephoneText.text.toString(),
            addPersonCompanyText.text.toString(),
            addPersonTitleText.text.toString(),
            addPersonNoteText.text.toString(),
            userId!!
        )
        job = CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response =
                RetrofitOperations.instance.addContact(
                    "Bearer " + token.toString(),
                    addContactModel = addContactModel
                )

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        //  JsonParser.parseString(
                        response.body()
                            ?.string()
                    )
                    //     )

                    Log.d("Pretty Printed JSON :", prettyJson)
                    Log.d("Image :", encoded)


                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }

}