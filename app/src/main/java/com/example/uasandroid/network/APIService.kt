package com.example.uasandroid.network

import com.example.uasandroid.model.Laptop
import com.example.uasandroid.model.LaptopResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {

    // Route untuk menambahkan data
    @POST("laptops")
    suspend fun createLaptop(@Body laptop: Laptop): Response<Laptop>

    // Route untuk mengambil data
    @GET("laptops")
    suspend fun getLaptops(): Response<List<LaptopResponse>>

    // Route untuk update data
    @POST("laptops/{id}")
    suspend fun updateLaptop(
        @Path("id") id: String,
        @Body film: Laptop
    ): Response<Laptop>

    // Route untuk delete data
    @DELETE("laptops/{id}")
    suspend fun deleteLaptop(
        @Path("id") id: String
    ): Response<Void>
}