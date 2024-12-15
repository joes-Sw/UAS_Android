package com.example.uasandroid.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.uasandroid.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    suspend fun getUserByUsernameAndPassword(username: String, password: String): User?
}