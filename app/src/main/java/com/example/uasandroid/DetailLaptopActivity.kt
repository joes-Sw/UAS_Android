package com.example.uasandroid

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.uasandroid.databinding.ActivityDetailLaptopBinding
import com.example.uasandroid.model.LaptopResponse

class DetailLaptopActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLaptopBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLaptopBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val laptop = intent.getParcelableExtra("laptop", LaptopResponse::class.java)

        with(binding) {
            if (laptop != null) {
                txtBrandName.text = laptop.brand
                txtPrice.text = laptop.price.toString()
                txtProcessor.text = laptop.processor
                txtGraphics.text = laptop.graphics
                txtRam.text = laptop.ram
                txtRom.text = laptop.rom
                txtBenchmark.text = laptop.benchmark.toString()
                Glide
                    .with(binding.root.context)
                    .load(laptop.image)
                    .centerCrop()
                    .into(imgLaptop)
            }

            btnBack.setOnClickListener{
                val intent = Intent(this@DetailLaptopActivity, AdminActivity::class.java)
                startActivity(intent)
            }
        }
    }
}