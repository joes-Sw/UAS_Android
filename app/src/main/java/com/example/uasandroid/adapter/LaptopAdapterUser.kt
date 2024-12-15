package com.example.uasandroid.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.provider.SyncStateContract.Helpers.insert
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uasandroid.DetailLaptopActivity
import com.example.uasandroid.R
import com.example.uasandroid.database.LaptopDao
import com.example.uasandroid.database.LaptopRoomDatabase
import com.example.uasandroid.databinding.ListLaptopUserBinding
import com.example.uasandroid.model.Laptop
import com.example.uasandroid.model.LaptopResponse
import com.example.uasandroid.network.BaseAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LaptopAdapterUser(private val listLaptop: MutableList<LaptopResponse>) :
    RecyclerView.Adapter<LaptopAdapterUser.LaptopViewHolder>() {
    private lateinit var binding: ListLaptopUserBinding

    private lateinit var mLaptopDao: LaptopDao
    private lateinit var executorService: ExecutorService

    inner class LaptopViewHolder(private val binding: ListLaptopUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(binding.root.context, DetailLaptopActivity::class.java)
                    intent.putExtra("laptop", listLaptop[position]) // mengirim data ke detail film)
                    binding.root.context.startActivity(intent)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(laptop: LaptopResponse) {
            with(binding) {
                txtBrand.text = laptop.brand
                txtPrice.text = "Rp. ${laptop.price},00"
                Glide
                    .with(binding.root.context)
                    .load(laptop.image)
                    .centerCrop()
                    .into(imgLaptop)

                if (laptop.isFavorite) {
                    btnSave.setImageResource(R.drawable.bookmark_solid)
                } else {
                    btnSave.setImageResource(R.drawable.bookmark_regular)
                }

                btnSave.setOnClickListener{
                    if (!laptop.isFavorite) {
                        btnSave.setImageResource(R.drawable.bookmark_solid)
                        val laptopUpdate = Laptop(brand = laptop.brand, price = laptop.price, image = laptop.image, processor = laptop.processor, isFavorite = true,  graphics = laptop.graphics, ram = laptop.ram, rom = laptop.rom, benchmark = laptop.benchmark)
                        val laptopUpdateResponse = LaptopResponse(id = laptop.id, brand = laptop.brand, price = laptop.price, image = laptop.image, processor = laptop.processor, isFavorite = true,  graphics = laptop.graphics, ram = laptop.ram, rom = laptop.rom, benchmark = laptop.benchmark)
                        updateLaptop(
                            laptop.id,
                            laptopUpdate,
                            onSuccess = { filmUpdated ->
                                insert(laptopUpdateResponse)
                                Log.i("storage lokal", "data berhasil dimasukkan ke storage lokal: $filmUpdated")
                                Log.i("data yang dimasukkan di lokal", laptop.toString())
                            }, onError = { errorMessage ->
                                Log.i("storage lokal", "data gagal dimasukkan ke storage lokal: $errorMessage")
                            }
                        )
                    } else {
                        btnSave.setImageResource(R.drawable.bookmark_regular)
                        val laptopUpdate = Laptop( brand = laptop.brand, price = laptop.price, image = laptop.image, processor = laptop.processor, isFavorite = false,  graphics = laptop.graphics, ram = laptop.ram, rom = laptop.rom, benchmark = laptop.benchmark)
                        val laptopUpdateResponse = LaptopResponse(id = laptop.id, brand = laptop.brand, price = laptop.price, image = laptop.image, processor = laptop.processor, isFavorite = false,  graphics = laptop.graphics, ram = laptop.ram, rom = laptop.rom, benchmark = laptop.benchmark)
                        updateLaptop(
                            laptop.id,
                            laptopUpdate,
                            onSuccess = { filmUpdated ->
                                delete(laptopUpdateResponse)
                                Log.i("storage lokal", "data berhasil dihapus dari storage lokal: $filmUpdated")
                            }, onError = { errorMessage ->
                                Log.i("storage lokal", "data gagal dihapus dari storage lokal: $errorMessage")
                            }
                        )
                    }


                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaptopViewHolder {
        binding = ListLaptopUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        executorService = Executors.newSingleThreadExecutor()
        val db = LaptopRoomDatabase.getDatabase(binding.root.context)
        mLaptopDao = db!!.laptopDao()!!
        return LaptopViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listLaptop.size
    }

    override fun onBindViewHolder(holder: LaptopViewHolder, position: Int) {
        holder.bind(listLaptop[position])
    }

    // Fungsi untuk memperbarui data
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<LaptopResponse>) {
        listLaptop.clear()
        listLaptop.addAll(newList)
        notifyDataSetChanged() // Memberi tahu RecyclerView untuk memperbarui tampilan
    }

    // menyimpan data di storage lokal (Dao)
    private fun insert(laptop: LaptopResponse) {
        executorService.execute { mLaptopDao.insertLaptop(laptop) }
    }

    private fun delete(laptop: LaptopResponse) {
        executorService.execute { mLaptopDao.deleteLaptop(laptop) }
    }

    private fun updateLaptop(
        id: String,
        laptop: Laptop,
        onSuccess: (Laptop?) -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = BaseAPI.api.updateLaptop(id, laptop)
                if (response.isSuccessful) {
                    val updatedLaptop = response.body() // Respons berhasil
                    withContext(Dispatchers.Main) {
                        Log.d("Laptop Update adapter", "Laptop updated successfully: $updatedLaptop")
                        Toast.makeText(binding.root.context, "Success to update Laptop", Toast.LENGTH_SHORT).show()
                        onSuccess(updatedLaptop) // Memanggil callback onSuccess
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    withContext(Dispatchers.Main) {
                        Log.e("Laptop Update", "Failed to update Laptop: $errorBody")
                        Toast.makeText(binding.root.context, "Failed to update Laptop", Toast.LENGTH_SHORT).show()
                        onError(errorBody) // Memanggil callback onError
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("Laptop Updateeeee", "Exception: ${e.message}")
                    Toast.makeText(binding.root.context, "Errore: ${e.message}", Toast.LENGTH_SHORT).show()
                    onError(e.message ?: "Unknown exception") // Memanggil callback onError
                }
            }
        }
    }

}