package com.example.uasandroid.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Laptop(
    @SerializedName("brand")
    val brand: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("price")
    val price: Int,
    @SerializedName("processor")
    val processor: String,
    @SerializedName("graphics")
    val graphics: String,
    @SerializedName("ram")
    val ram: String,
    @SerializedName("rom")
    val rom: String,
    @SerializedName("benchmark")
    val benchmark: Int,
    @SerializedName("isFavorite")
    val isFavorite: Boolean = false,
) : Parcelable
