package com.example.uasandroid.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uasandroid.DetailLaptopActivity
import com.example.uasandroid.EditLaptopActivity
import com.example.uasandroid.databinding.ListLaptopAdminBinding
import com.example.uasandroid.model.LaptopResponse
import com.example.uasandroid.network.BaseAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LaptopAdapterAdmin(private val listLaptop: MutableList<LaptopResponse>) :
    RecyclerView.Adapter<LaptopAdapterAdmin.LaptopViewHolder>() {
    private lateinit var binding: ListLaptopAdminBinding

    inner class LaptopViewHolder(private val binding: ListLaptopAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(binding.root.context, DetailLaptopActivity::class.java)
                    intent.putExtra("laptop", listLaptop[position]) // mengirim data ke detail laptop
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

                btnEdit.setOnClickListener{
                    val intent = Intent(binding.root.context, EditLaptopActivity::class.java)
                    intent.putExtra("laptop", laptop)
                    binding.root.context.startActivity(intent)
                }

                btnTrash.setOnClickListener{
                    deleteItem(laptop.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LaptopViewHolder {
        binding = ListLaptopAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    fun deleteItem(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = BaseAPI.api.deleteLaptop(id)
                if (response.isSuccessful) {
                    Log.i("data berhasil di simpan dihapus dari database", response.body().toString())
                }
            } catch (e: Exception) {
                Log.e("gagal menghapus data", e.toString())
            }
        }

    }
}