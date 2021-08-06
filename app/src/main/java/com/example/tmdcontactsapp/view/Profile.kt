package com.example.tmdcontactsapp.view

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tmdcontactsapp.R

class Profile : AppCompatActivity() {

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileImageView = findViewById(R.id.profileImageView)
        profileNameText = findViewById(R.id.profileNameText)
        profileSurnameText = findViewById(R.id.profileSurnameText)
        profileEmailText = findViewById(R.id.profileEmailText)
        profileAddressText = findViewById(R.id.profileAddressText)
        profileBirthdayText = findViewById(R.id.profileBirthdayText)
        profileCellphoneText = findViewById(R.id.profileCellphoneText)
        profileWorkphoneText = findViewById(R.id.profileWorkphoneText)
        profileHomephoneText = findViewById(R.id.profileHomephoneText)
        profileCompanyText = findViewById(R.id.profileCompanyText)
        profileNoteText = findViewById(R.id.profileNoteText)
//        profileLogoutButtonText = findViewById(R.id.profileLogoutButtonText)


    }
}