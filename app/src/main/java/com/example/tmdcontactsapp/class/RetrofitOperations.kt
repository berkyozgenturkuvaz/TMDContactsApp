package com.example.tmdcontactsapp.`class`

import com.example.tmdcontactsapp.service.ContacsAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitOperations{

    private const val BASE_URL = "http://tmdcontacts-api.dev.tmd"

    val instance: ContacsAPI by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ContacsAPI::class.java)
    }
}
