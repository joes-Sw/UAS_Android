package com.example.uasandroid

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uasandroid.databinding.ActivityEditLaptopBinding
import com.example.uasandroid.model.Laptop
import com.example.uasandroid.model.LaptopResponse
import com.example.uasandroid.network.BaseAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditLaptopActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditLaptopBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditLaptopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val laptop = intent.getParcelableExtra("laptop", LaptopResponse::class.java)

        with(binding) {
            if (laptop != null) {
                edtBrand.setText(laptop.brand)
                edtImg.setText(laptop.image)
                edtPrice.setText(laptop.price.toString())
                edtProcessor.setText(laptop.processor)
                edtGraphics.setText(laptop.graphics)
                edtRam.setText(laptop.ram)
                edtRom.setText(laptop.rom)
                edtBenchmark.setText(laptop.benchmark.toString())

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
                        Toast.makeText(this@EditLaptopActivity, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        val laptopUpdate = Laptop(brand = txtBrand, image = txtImage, price = txtPrice, processor = txtProcessor, graphics = txtGraphics, ram = txtRam, rom = txtRom, benchmark = txtBenchmark)
                        updateLaptop(laptop.id, laptopUpdate)
                    }
                }
            }
        }
    }

    private fun updateLaptop(id: String, laptop: Laptop) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = BaseAPI.api.updateLaptop(id, laptop)
                if (response.isSuccessful) {
                    val updatedFilm = response.body() // Respons berhasil
                    runOnUiThread {
                        Log.d("Laptop Update", "Laptop updated successfully: $updatedFilm")
                        Toast.makeText(this@EditLaptopActivity, "Laptop updated!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@EditLaptopActivity, AdminActivity::class.java))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Laptop Update", "Failed to update laptop: $errorBody")
                    runOnUiThread {
                        Toast.makeText(this@EditLaptopActivity, "Failed to update laptop", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("Laptop Update", "Exception: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@EditLaptopActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}