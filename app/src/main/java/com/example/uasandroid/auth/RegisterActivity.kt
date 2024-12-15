package com.example.uasandroid.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uasandroid.MainActivity
import com.example.uasandroid.R
import com.example.uasandroid.database.UserDao
import com.example.uasandroid.database.UserRoomDatabase
import com.example.uasandroid.databinding.ActivityRegisterBinding
import com.example.uasandroid.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var prefManager: PrefManager
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        val database = UserRoomDatabase.getDatabase(this)
        userDao = database.userDao()

        with(binding){

            btnRegisterUser.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                val confirmPassword = edtConfirmPassword.text.toString()
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(this@RegisterActivity,"Mohon isi semua data",Toast.LENGTH_SHORT).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(this@RegisterActivity, "Password tidak sama", Toast.LENGTH_SHORT).show()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val user = User(username = username, password = password, role = "user")
                        userDao.insertUser(user)

                        // Setelah selesai, kembali ke thread utama untuk update UI
                        withContext(Dispatchers.Main) {
                            prefManager.saveUsername(username)
                            prefManager.savePassword(password)
                            prefManager.saveRole("user")
                            prefManager.setLoggedIn(true)
                            checkLoginStatus()
                        }
                    }
                }
            }

            btnRegisterAdmin.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                val confirmPassword = edtConfirmPassword.text.toString()
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(this@RegisterActivity,"Mohon isi semua data",Toast.LENGTH_SHORT).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(this@RegisterActivity, "Password tidak sama", Toast.LENGTH_SHORT).show()
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val admin = User(username = username, password = password, role = "admin")
                        userDao.insertUser(admin)

                        // Setelah selesai, kembali ke thread utama untuk update UI
                        withContext(Dispatchers.Main) {
                            prefManager.saveUsername(username)
                            prefManager.savePassword(password)
                            prefManager.saveRole("admin")
                            prefManager.setLoggedIn(true)
                            checkLoginStatus()
                        }
                    }
                }
            }

            txtSign.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
    }
    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            Toast.makeText(this@RegisterActivity, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this@RegisterActivity, "Registrasi gagal", Toast.LENGTH_SHORT).show()
        }
    }
}