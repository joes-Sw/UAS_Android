package com.example.uasandroid.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.uasandroid.model.LaptopResponse

@Dao
interface LaptopDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLaptop(laptop: LaptopResponse)

    @Update
    fun updateLaptop(laptop: LaptopResponse)

    @Delete
    fun deleteLaptop(laptop: LaptopResponse)

    @Query("SELECT * FROM laptop_table")
    fun getAllLaptops(): List<LaptopResponse>
}