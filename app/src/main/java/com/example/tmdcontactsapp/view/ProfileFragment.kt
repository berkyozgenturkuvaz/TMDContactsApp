package com.example.tmdcontactsapp.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.model.UserImageModel
import com.example.tmdcontactsapp.service.ContacsAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"
private const val ARG_PARAM5 = "param5"
private const val ARG_PARAM6 = "param6"
private const val ARG_PARAM7 = "param7"
private const val ARG_PARAM8 = "param8"
private const val ARG_PARAM9 = "param9"
private const val ARG_PARAM10 = "param10"
private const val ARG_PARAM11 = "param11"
private const val ARG_PARAM12 = "param12"
private const val ARG_PARAM13 = "param13"
private const val ARG_PARAM14 = "param14"


class ProfileFragment : Fragment() {

    lateinit var profileImageView: ImageView
    lateinit var profileNameText: TextView
    lateinit var profileSurnameText: TextView
    lateinit var profileEmailText: TextView
    lateinit var profileAddressText: TextView
    lateinit var profileBirthdayText: TextView
    lateinit var profileCellphoneText: TextView
    lateinit var profileWorkphoneText: TextView
    lateinit var profileHomephoneText: TextView
    lateinit var profileCompanyText: TextView
    lateinit var profileTitleText: TextView
    lateinit var profileNoteText: TextView
    lateinit var profileGroupText: TextView
    lateinit var profileLogoutButtonText: Button
    lateinit var updateProfile: Button
    lateinit var toolbar: Toolbar

    private var userId: Int? = 0
    private var nameProfile: String? = null
    private var surnameProfile: String? = null
    private var emailProfile: String? = null
    private var addressProfile: String? = null
    private var birthDateProfile: String? = null
    private var cellPhoneProfile: String? = null
    private var workPhoneProfile: String? = null
    private var homePhoneProfile: String? = null
    private var companyProfile: String? = null
    private var titleProfile: String? = null
    private var noteProfile: String? = null
    private var photo: String? = ""
    private var token: String? = null

    private val BASE_URL = "http://tmdcontacts-api.dev.tmd"


    companion object {
        @JvmStatic
        fun newInstance(
            userId: Int,
            nameProfile: String,
            surnameProfile: String,
            emailProfile: String,
            addressProfile: String,
            birthDateProfile: String,
            cellPhoneProfile: String,
            workPhoneProfile: String,
            homePhoneProfile: String,
            companyProfile: String,
            titleProfile: String,
            noteProfile: String,
            token: String
        ) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM12, userId)
                    putString(ARG_PARAM1, nameProfile)
                    putString(ARG_PARAM2, surnameProfile)
                    putString(ARG_PARAM3, emailProfile)
                    putString(ARG_PARAM4, addressProfile)
                    putString(ARG_PARAM5, birthDateProfile)
                    putString(ARG_PARAM6, cellPhoneProfile)
                    putString(ARG_PARAM7, workPhoneProfile)
                    putString(ARG_PARAM8, homePhoneProfile)
                    putString(ARG_PARAM9, companyProfile)
                    putString(ARG_PARAM10, titleProfile)
                    putString(ARG_PARAM11, noteProfile)
                    putString(ARG_PARAM14, token)

                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            userId = it.getInt(ARG_PARAM12, 0)
            nameProfile = it.getString(ARG_PARAM1)
            surnameProfile = it.getString(ARG_PARAM2)
            emailProfile = it.getString(ARG_PARAM3)
            addressProfile = it.getString(ARG_PARAM4)
            birthDateProfile = it.getString(ARG_PARAM5)
            cellPhoneProfile = it.getString(ARG_PARAM6)
            workPhoneProfile = it.getString(ARG_PARAM7)
            homePhoneProfile = it.getString(ARG_PARAM8)
            companyProfile = it.getString(ARG_PARAM9)
            titleProfile = it.getString(ARG_PARAM10)
            noteProfile = it.getString(ARG_PARAM11)
            token = it.getString(ARG_PARAM14)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.activity_profile, container, false)

        profileImageView = view.findViewById(R.id.profileImageView)
        profileNameText = view.findViewById(R.id.profileNameText)
        profileSurnameText = view.findViewById(R.id.profileSurnameText)
        profileEmailText = view.findViewById(R.id.profileEmailText)
        profileAddressText = view.findViewById(R.id.profileAddressText)
        profileBirthdayText = view.findViewById(R.id.profileBirthdayText)
        profileCellphoneText = view.findViewById(R.id.profileCellphoneText)
        profileWorkphoneText = view.findViewById(R.id.profileWorkphoneText)
        profileHomephoneText = view.findViewById(R.id.profileHomephoneText)
        profileCompanyText = view.findViewById(R.id.profileCompanyText)
        profileTitleText = view.findViewById(R.id.profileTitleText)
        profileNoteText = view.findViewById(R.id.profileNoteText)
//        profileLogoutButtonText = view.findViewById(R.id.profileLogoutButtonText)
        updateProfile = view.findViewById(R.id.exampleButton)

        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarMenu)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        loadData()

        updateProfile.setOnClickListener {
            updateProfile()
        }

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        profileNameText.text = "Name: " + nameProfile.toString()
        profileSurnameText.text = "Surname: " + surnameProfile.toString()
        profileEmailText.text = "Email: " + emailProfile.toString()
        profileAddressText.text = "Address: " + addressProfile.toString()
        profileBirthdayText.text = "Birthdate: " + birthDateProfile.toString()
        profileCellphoneText.text = "Tel: " + cellPhoneProfile.toString()
        profileWorkphoneText.text = "Work Tel: " + workPhoneProfile.toString()
        profileHomephoneText.text = "Home Tel: " + homePhoneProfile.toString()
        profileCompanyText.text = "Company: " + companyProfile.toString()
        profileTitleText.text = "Title: " + titleProfile.toString()
        profileNoteText.text = "Note: " + noteProfile.toString()


    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.main_profile, menu)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.Logout -> {
//                Toast.makeText(requireActivity(), "Logout", Toast.LENGTH_LONG).show()
//            }
//            R.id.updateIcon -> {
//                Toast.makeText(requireActivity(), "Update Profile", Toast.LENGTH_LONG).show()
//                val intent = Intent(context, Update_Profile::class.java)
//                intent.putExtra("userId", userId)
//                intent.putExtra("name", nameProfile)
//                intent.putExtra("surname", surnameProfile)
//                intent.putExtra("email", emailProfile)
//                intent.putExtra("address", addressProfile)
//                intent.putExtra("birthdate",birthDateProfile)
//                intent.putExtra("photo", photo)
//                intent.putExtra("tel",cellPhoneProfile)
//                intent.putExtra("telBusiness",workPhoneProfile)
//                intent.putExtra("telHome", homePhoneProfile)
//                intent.putExtra("company",companyProfile)
//                intent.putExtra("title",titleProfile)
//                intent.putExtra("note",noteProfile)
//                intent.putExtra("token", token)
//                startActivity(intent)
//
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

    //Get User Image using with email
    private fun loadData() {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ContacsAPI::class.java)
        val call =
            emailProfile?.let { service.getUserImage("Bearer " + token.toString(), email = it) }

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
                        val decodedImage =
                            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        profileImageView.setImageBitmap(decodedImage)

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

    fun updateProfile() {
        val intent = Intent(context, Update_Profile::class.java)
        intent.putExtra("userId", userId)
        intent.putExtra("name", nameProfile)
        intent.putExtra("surname", surnameProfile)
        intent.putExtra("email", emailProfile)
        intent.putExtra("address", addressProfile)
        intent.putExtra("birthdate", birthDateProfile)
        intent.putExtra("tel", cellPhoneProfile)
        intent.putExtra("telBusiness", workPhoneProfile)
        intent.putExtra("telHome", homePhoneProfile)
        intent.putExtra("company", companyProfile)
        intent.putExtra("title", titleProfile)
        intent.putExtra("note", noteProfile)
        intent.putExtra("token", token)
        startActivity(intent)
    }

}