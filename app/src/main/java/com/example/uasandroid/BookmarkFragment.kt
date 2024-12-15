package com.example.uasandroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uasandroid.adapter.LaptopAdapterUser
import com.example.uasandroid.database.LaptopDao
import com.example.uasandroid.database.LaptopRoomDatabase
import com.example.uasandroid.databinding.FragmentBookmarkBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookmarkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookmarkFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private lateinit var mLaptopDao: LaptopDao
    private lateinit var executorService: ExecutorService

    private val laptopAdapter by lazy { LaptopAdapterUser(mutableListOf()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        executorService = Executors.newSingleThreadExecutor()
        val db = LaptopRoomDatabase.getDatabase(requireContext())
        mLaptopDao = db!!.laptopDao()!!
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            listLaptop.apply {
                adapter = laptopAdapter
                layoutManager = GridLayoutManager(
                    this@BookmarkFragment.requireContext(),
                    2,
                    RecyclerView.VERTICAL,
                    false
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getAllLaptop()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookmarkFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookmarkFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun getAllLaptop(){
        executorService.execute{
            try {
                val laptops = mLaptopDao.getAllLaptops() // Mendapatkan semua laptop dari database
                requireActivity().runOnUiThread {
                    laptopAdapter.updateData(laptops) // Memperbarui adapter dengan data dari database
                }
            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    // Menangani error jika terjadi
                    Toast.makeText(requireContext(), "Error fetching laptop: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}