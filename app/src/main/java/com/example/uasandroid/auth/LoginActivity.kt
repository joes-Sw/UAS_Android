package com.example.uasandroid.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uasandroid.AdminActivity
import com.example.uasandroid.MainActivity
import com.example.uasandroid.database.UserDao
import com.example.uasandroid.database.UserRoomDatabase
import com.example.uasandroid.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)

        val database = UserRoomDatabase.getDatabase(this)
        userDao = database.userDao()

        with(binding){

            btnLoginAdmin.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Mohon isi semua data", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val user = userDao.getUserByUsernameAndPassword(username, password)
                        withContext(Dispatchers.Main) {
                            if (user != null && user.role == "admin") {
                                prefManager.setLoggedIn(true)
                                prefManager.saveUsername(username)
                                prefManager.savePassword(password)
                                prefManager.saveRole(user.role)
                                checkLoginStatus()
                            } else {
                                Toast.makeText(this@LoginActivity, "Username atau password admin salah", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            btnLoginUser.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Mohon isi semua data", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val user = userDao.getUserByUsernameAndPassword(username, password)
                        withContext(Dispatchers.Main) {
                            if (user != null && user.role == "user") {
                                prefManager.setLoggedIn(true)
                                prefManager.saveUsername(username)
                                prefManager.savePassword(password)
                                prefManager.saveRole(user.role)
                                checkLoginStatus()
                            } else {
                                Toast.makeText(this@LoginActivity, "Username atau password user salah", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            txtSign.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
        }
    }

    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            if (prefManager.getRole() == "admin") {
                Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, AdminActivity::class.java))
            } else if (prefManager.getRole()=="user") {
                Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        } else {
            Toast.makeText(this@LoginActivity, "Login gagal",
                Toast.LENGTH_SHORT).show()
        }
    }
}