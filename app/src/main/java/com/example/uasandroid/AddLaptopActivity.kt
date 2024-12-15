package com.example.uasandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uasandroid.databinding.ActivityAddLaptopBinding
import com.example.uasandroid.model.Laptop
import com.example.uasandroid.network.BaseAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddLaptopActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddLaptopBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLaptopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnDone.setOnClickListener{
                val txtBrand = edtBrand.text.toString()
                val txtImage = edtImg.text.toString()
                val txtPrice = edtPrice.text.toString().toInt()
                val txtProcessor = edtProcessor.text.toString()
                val txtGraphics = edtGraphics.text.toString()
                val txtRam = edtRam.text.toString()
                val txtRom = edtRom.text.toString()
                val txtBenchmark = edtBenchmark.text.toString().toInt()

                if (txtBrand.isEmpty() || txtImage.isEmpty() || txtPrice == null || txtProcessor.isEmpty() || txtGraphics.isEmpty() || txtRam.isEmpty() || txtRom.isEmpty()) {
                    Toast.makeText(this@AddLaptopActivity, "Please fill all fields", Toast.LENGTH_SHORT).show()
                } else {
                    val laptop = Laptop(txtBrand, txtImage, txtPrice, txtProcessor, txtGraphics, txtRam, txtRom, txtBenchmark)
                    saveLaptopToFirebase(laptop)
                }
            }
        }
    }

    private fun saveLaptopToFirebase(laptop: Laptop) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = BaseAPI.api.createLaptop(laptop)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("API_RESPONSE", "Response: $responseBody")
                    runOnUiThread {
                        Toast.makeText(this@AddLaptopActivity, "Laptop saved successfully!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@AddLaptopActivity, AdminActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    Log.e("API_ERROR", "Error: ${response.errorBody()?.string()}")
                    runOnUiThread {
                        Toast.makeText(this@AddLaptopActivity, "Error saving Laptop: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("API_EXCEPTION", "Exception: $e")
                runOnUiThread {
                    Toast.makeText(this@AddLaptopActivity, "Exception: $e", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}