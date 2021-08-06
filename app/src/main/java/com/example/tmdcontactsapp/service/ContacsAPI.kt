package com.example.tmdcontactsapp.service

import com.example.tmdcontactsapp.model.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*


interface ContacsAPI {

    //Get Contact List for ContactsListFragment
    @GET("/api/Contacts/GetListByUserId")
    fun getData(
        @Header("Authorization") Bearer : String,
        @Header("accept") acceptString: String = "*/*",
        @Query(value = "userId", encoded = true) userId: Int
    ): retrofit2.Call<List<ContactsModel>>

    //Get Contact Detail for DetailPerson
    @GET("/api/Contacts/Get")
    fun getContactData(
    @Header("Authorization") Bearer : String,
    @Header("accept") acceptString: String = "*/*",
    @Query(value = "id", encoded = true) contactId: Int
    ): retrofit2.Call<ProfileModel>

    //Get Profile Data by Email in Login For ProfileFragment
    @GET("/api/Users/GetByEmail")
    fun getProfileData(
        @Header("Authorization") Bearer : String,
        @Header("accept") acceptString: String = "*/*",
        @Query(value = "email", encoded = true) email: String
    ): retrofit2.Call<ProfileModel>

    //Get User Image by Email in Home&Profile For ContactsList&Profile Fragment
    @GET("/api/Users/GetByEmail")
    fun getUserImage(
        @Header("Authorization") Bearer : String,
        @Header("accept") acceptString: String = "*/*",
        @Query(value = "email", encoded = true) email: String
    ): retrofit2.Call<UserImageModel>

    //Get Contact Image by UserId for DetailPerson
    @GET("/api/Contacts/Get")
    fun getContactImage(
        @Header("Authorization") Bearer : String,
        @Header("accept") acceptString: String = "*/*",
        @Query(value = "id", encoded = true) contactId: Int
    ): retrofit2.Call<UserImageModel>

    //Get Group List for GroupsFragment
    @GET("/api/Groups/GetListByUserId")
    fun getDataGroups(
        @Header("Authorization") Bearer : String,
        @Header("accept") acceptString: String = "*/*",
        @Query(value = "userId", encoded = true) userId: Int
    ): retrofit2.Call<List<GroupsModel>>



    //Get Group Contacts for GroupDetails
    @GET("/api/GroupsContacts/GetListByGroupId")
    fun getDataGroupDetails(
        @Header("Authorization") Bearer : String,
        @Header("accept") acceptString: String = "*/*",
        @Query(value = "groupId", encoded = true) groupId: Int
    ): retrofit2.Call<List<GroupDetailsModel>>


    //Post Create User Data for ActivityRegister
    @POST("/api/Auths/Register")
    suspend fun createEmployee(
        @Header("accept") acceptString: String = "application/json-patch+json",
        @Header("Content-Type") contentType: String = "application/json-patch+json",
        @Body registerModel: RegisterModel
        // @Field("name") name: String,
        // @Field("userId") userId: Int
    ): Response<ResponseBody>

    //Post User Login Data for MainActivity
    @POST("/api/Auths/Login/")
    suspend fun login(
        @Header("accept") acceptString: String = "application/json-patch+json",
        @Header("Content-Type") contentType: String = "application/json-patch+json",
        @Body loginModel: LoginModel
        // @Field("name") name: String,
        // @Field("userId") userId: Int
    ): Response<TokenModel>


    //Post Add Contact Data for AddPersonFragment
    @POST("/api/Contacts/Add/")
    suspend fun addContact(
        @Header("Authorization") Bearer : String,
        @Header("accept") acceptString: String = "application/json-patch+json",
        @Header("Content-Type") contentType: String = "application/json-patch+json",
        @Body addContactModel: AddContactModel
        // @Field("name") name: String,
        // @Field("userId") userId: Int
    ): Response<ResponseBody>

    //Post Password For ForgotPassword
    @POST("/api/Auths/ForgotPassword")
    suspend fun forgotPass(
        @Header("accept") acceptString: String = "*/*",
        @Query(value = "email", encoded = true) email: String
    ): Response<ResponseBody>

    //Post Password For ResetPassword
    @POST("/api/Auths/ResetPassword")
    suspend fun resetPass(
        @Header("accept") acceptString: String = "/*/",
        @Header("Content-Type") contentType: String = "application/json-patch+json",
        @Body resetPasswordModel: ResetPasswordModel
    ): Response<ResponseBody>

    //Post Password For changePassword in UpdateProfile
    @POST("/api/Auths/ResetPasswordVerification")
    suspend fun changePass(
        @Header("Authorization") Bearer : String,
        @Header("accept") acceptString: String = "/*/",
        @Header("Content-Type") contentType: String = "application/json-patch+json",
        @Query(value = "currentPassword", encoded = true) currentPassword: String,
        @Body changePasswordModel: ChangePasswordModel
    ): Response<ResponseBody>


    //Post Add a Group for AddGroup
    @POST("/api/Groups/Add")
    suspend fun addGroup(
        @Header("Authorization") Bearer : String,
        @Header("accept") acceptString: String = "*/*",
        @Header("Content-Type") contentType: String = "application/json-patch+json",
        @Body addGroupModel: AddGroupModel
    ): Response<ResponseBody>


    //Post Update Profile Data for UpdateProfile
    @POST("/api/Users/Update")
    suspend fun updateProfile(
        @Header("Authorization") Bearer : String,
        @Header("accept") acceptString: String = "/*/",
        @Header("Content-Type") contentType: String = "application/json-patch+json",
        @Body updateProfileModel: UpdateProfileModel
    ): Response<ResponseBody>

    //Post Update Profile Data for UpdateProfile
    @POST("/api/Contacts/Update")
    suspend fun updateContact(
        @Header("Authorization") Bearer : String,
        @Header("accept") acceptString: String = "/*/",
        @Header("Content-Type") contentType: String = "application/json-patch+json",
        @Body updateContactModel: UpdateContactModel
    ): Response<ResponseBody>

    //Post Update a Group for UpdateGroup
    @POST("/api/Groups/Update")
    suspend fun updateGroup(
        @Header("Authorization") Bearer : String,
        @Header("accept") acceptString: String = "*/*",
        @Header("Content-Type") contentType: String = "application/json-patch+json",
        @Body updateGroupModel: UpdateGroupModel
    ): Response<ResponseBody>

    //Post Add a Group Contact for GroupDetails
    @POST("/api/GroupsContacts/Add")
    suspend fun addGroupContact(
        @Header("Authorization") Bearer : String,
        @Header("accept") acceptString: String = "*/*",
        @Header("Content-Type") contentType: String = "application/json-patch+json",
        @Body addGroupContactModel: AddGroupContactModel
    ): Response<ResponseBody>


    //Post Contact Info For DeleteContact
    @POST("/api/Contacts/Delete")
    suspend fun deleteContact(
        @Header("Authorization") Bearer : String,
        @Header("accept") acceptString: String = "*/*",
        @Query(value = "id", encoded = true) id: Int
    ): Response<ResponseBody>

    //Post Contact Info For DeleteGroup
    @POST("/api/Groups/Delete")
    suspend fun deleteGroup(
        @Header("Authorization") Bearer : String,
        @Header("accept") acceptString: String = "*/*",
        @Query(value = "id", encoded = true) id: Int
    ): Response<ResponseBody>

    //Post Contact Info For DeleteGroup
    @POST("/api/GroupsContacts/Delete")
    suspend fun deleteGroupContact(
        @Header("Authorization") Bearer : String,
        @Header("accept") acceptString: String = "*/*",
        @Header("Content-Type") contentType: String = "application/json-patch+json",
        @Body deleteGroupContactModel: DeleteGroupContactModel
    ): Response<ResponseBody>

}