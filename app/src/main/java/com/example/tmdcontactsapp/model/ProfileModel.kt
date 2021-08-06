package com.example.tmdcontactsapp.model

data class ProfileModel(
    val id: Int? = 0,
    val name: String? = "",
    val surname: String? = "",
    val email: String? = "",
    val password: String? = "",
    val address: String? = "",
    val birthDate: String? = "",
    val photo: String = "",
    val tel: String? = "",
    val telBusiness: String? = "",
    val telHome: String? = "",
    val company: String? = "",
    val title: String? = "",
    val note: String? = ""
)
