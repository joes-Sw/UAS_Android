package com.example.uasandroid.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "laptop_table")
data class LaptopResponse(
    @SerializedName("_id")
    @PrimaryKey @ColumnInfo(name = "id")
    val id: String,
    @SerializedName("brand")
    @ColumnInfo(name = "brand")
    val brand: String,
    @SerializedName("image")
    @ColumnInfo(name = "image")
    val image: String,
    @SerializedName("price")
    @ColumnInfo(name = "price")
    val price: Int,
    @SerializedName("processor")
    @ColumnInfo(name = "processor")
    val processor: String,
    @SerializedName("graphics")
    @ColumnInfo(name = "graphics")
    val graphics: String,
    @SerializedName("ram")
    @ColumnInfo(name = "ram")
    val ram: String,
    @SerializedName("rom")
    @ColumnInfo(name = "rom")
    val rom: String,
    @SerializedName("benchmark")
    @ColumnInfo(name = "benchmark")
    val benchmark: Int,
    @SerializedName("isFavorite")
    val isFavorite: Boolean,
) : Parcelable
