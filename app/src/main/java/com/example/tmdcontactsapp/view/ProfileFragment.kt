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
import com.example.tmdcontactsapp.`class`.Preferences.get
import com.example.tmdcontactsapp.`class`.Preferences.savePrefs
import com.example.tmdcontactsapp.`class`.Preferences.set
import com.example.tmdcontactsapp.`class`.RetrofitOperations
import com.google.gson.Gson
import kotlinx.coroutines.*

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
    lateinit var updateProfile: Button
    lateinit var toolbar: Toolbar

    private var userId: Int? = 0
    private var userMail: String? = null
    private var token: String? = null
    private var job: Job? = null

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


    companion object {
        @JvmStatic
        fun newInstance(

        ) =
            ProfileFragment().apply {
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
        updateProfile = view.findViewById(R.id.exampleButton)

        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarMenu)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        token = context?.savePrefs()?.get("token", "value")
        userMail = context?.savePrefs()?.get("userMail", "value")

        updateProfile.setOnClickListener {
            updateProfile()
        }

        loadData()

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

    override fun onStart() {
        super.onStart()
    }

    //Get Profile Data using with email
    @SuppressLint("SetTextI18n")
    private fun loadData() {

        job = CoroutineScope(Dispatchers.IO).launch {

            val response = RetrofitOperations.instance.getProfileData(
                "Bearer " + token.toString(),
                email = userMail.toString()
            )

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val profileModels = response.body()
                        profileNameText.text = "Name: " + profileModels?.name.toString()
                        profileSurnameText.text = "Surname: " + profileModels?.surname.toString()
                        profileEmailText.text = "Email: " + profileModels?.email.toString()
                        profileAddressText.text = "Address: " + profileModels?.address.toString()
                        profileBirthdayText.text =
                            "Birthdate: " + profileModels?.birthDate.toString()
                        profileCellphoneText.text = "Tel: " + profileModels?.tel.toString()
                        profileWorkphoneText.text =
                            "Work Tel: " + profileModels?.telBusiness.toString()
                        profileHomephoneText.text = "Home Tel: " + profileModels?.telHome.toString()
                        profileCompanyText.text = "Company: " + profileModels?.company.toString()
                        profileTitleText.text = "Title: " + profileModels?.title.toString()
                        profileNoteText.text = "Note: " + profileModels?.note.toString()
                        photo = profileModels?.photo

                        val imageBytes = Base64.decode(photo, 0)
                        val decodedImage =
                            BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        profileImageView.setImageBitmap(decodedImage)

                        requireContext().savePrefs()["userProfile"] = Gson().toJson(profileModels)
                        onStart()


                    }
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }
    }


    fun updateProfile() {
        val intent = Intent(context, Update_Profile::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }


}