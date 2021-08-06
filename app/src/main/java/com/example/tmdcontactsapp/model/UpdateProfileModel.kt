package com.example.tmdcontactsapp.model

data class UpdateProfileModel(
    val Id : Int,
    val Name : String,
    val Surname : String,
    val Email: String,
    val Address : String,
    val BirthDate : String,
    val Photo : String = "",
    val Tel : String,
    val TelBusiness : String,
    val TelHome : String,
    val Company : String,
    val Title : String,
    val Note : String,
    val Status : Boolean
)
