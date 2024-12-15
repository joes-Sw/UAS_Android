package com.example.uasandroid.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BaseAPI {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://ppbo-api.vercel.app/ZRik3/") // Ganti dengan URL Firebase Realtime Database
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: APIService by lazy {
        retrofit.create(APIService::class.java)
    }
}