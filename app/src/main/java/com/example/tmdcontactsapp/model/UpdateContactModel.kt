package com.example.tmdcontactsapp.model

data class UpdateContactModel(
    val Id : Int,
    var Name: String,
    val Surname: String,
    val Email: String,
    val Address : String,
    var BirthDate: String,
    val Photo : String? = "",
    val Tel : String,
    val TelBusiness : String,
    val TelHome : String,
    var Company : String,
    val Title : String,
    val Note : String
)
