package com.example.tmdcontactsapp.model

//import com.google.gson.annotations.SerializedName

data class ContactsModel(
    val id : Int,
    //@SerializedName("name")
    val name : String,
  //  @SerializedName("surname")
    val surname : String,
   // @SerializedName("email")
    val email: String,
   // @SerializedName("password")
    val password: String,
   // @SerializedName("address")
    val address : String,
  //  @SerializedName("birthDate")
    val birthDate : String,
 //   @SerializedName("photo")
    val photo : String = "",
  //  @SerializedName("cellPhone")
    val tel : String,
  //  @SerializedName("workPhone")
    val telBusiness : String,
  //  @SerializedName("homePhone")
    val telHome : String,
  //  @SerializedName("company")
    val company : String,
 //   @SerializedName("title")
    val title : String,
 //   @SerializedName("note")
    val note : String
    )
