package com.example.uasandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uasandroid.auth.LoginActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.uasandroid.auth.PrefManager
import com.example.uasandroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        checkLoginStatus()

        with(binding){
            val navController = findNavController(R.id.nav_host_fragment)
            bottomNavigationView.setupWithNavController(navController)
        }
    }

    //    cek login
    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (!isLoggedIn) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        } else {
            when (prefManager.getRole()) {
                "admin" -> {
                    startActivity(Intent(this@MainActivity, AdminActivity::class.java))
                    finish()
                }
                "user" -> {
                    // Jangan meluncurkan MainActivity lagi karena sudah berada di MainActivity
                    // Tidak perlu aksi tambahan untuk role "user"
                }
            }
        }
    }
}