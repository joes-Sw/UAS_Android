package com.example.uasandroid.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.uasandroid.model.LaptopResponse

@Database(entities = [LaptopResponse::class], version = 1, exportSchema = false)
abstract class LaptopRoomDatabase : RoomDatabase() {
    abstract fun laptopDao(): LaptopDao

    companion object {
        @Volatile
        private var INSTANCE: LaptopRoomDatabase? = null
        fun getDatabase(context: Context): LaptopRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(LaptopRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, LaptopRoomDatabase::class.java, "laptops"
                    )
                        .build()
                }
            }
            return INSTANCE
        }
    }
}