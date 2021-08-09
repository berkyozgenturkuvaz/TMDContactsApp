package com.example.tmdcontactsapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.`class`.Preferences.get
import com.example.tmdcontactsapp.`class`.Preferences.savePrefs

class VerificationCode : AppCompatActivity() {

    private var verificationCode: String? = null
    private var verificationCodeUser: String? = null
    private var emailForPass: String? = null
    lateinit var verificationCodeText: EditText
    lateinit var verificationButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code)

        verificationCodeText = findViewById(R.id.verificationCodeText)
        verificationButton = findViewById(R.id.verifyCodeButton)

        verificationCode = savePrefs()["verificationCode", ""]
        emailForPass = savePrefs()["emailForPass", ""]

    }

    fun verifyCodeButton(view: View) {
        verificationCodeUser = verificationCodeText.text.toString()
        if (verificationCodeUser.toString() == verificationCode.toString()) {
            val intentToResetPass = Intent(applicationContext, ResetPassword::class.java)
            startActivity(intentToResetPass)
        }
        else{
            Toast.makeText(
                applicationContext,
                "The entered code is invalid!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}