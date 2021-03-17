package com.tulsidistributors.tdemployee.ui.home.fragment.search_stock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentAdminBrandBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.admin_brand.AdminBrandData
import com.tulsidistributors.tdemployee.model.admin_brand.AdminBrandModel
import com.tulsidistributors.tdemployee.model.search_stock.SearchStockItemData
import com.tulsidistributors.tdemployee.model.search_stock.SearchStockItemModel
import com.tulsidistributors.tdemployee.ui.adapter.AdminBrandAdapter
import com.tulsidistributors.tdemployee.ui.adapter.OnAdminBrandItemClicked
import com.tulsidistributors.tdemployee.ui.adapter.SearchStockItemAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception


class AdminBrandFragment : Fragment(), OnAdminBrandItemClicked {

    lateinit var binding: FragmentAdminBrandBinding
    lateinit var adminBrandRecycler: RecyclerView
    lateinit var adminAdapter: AdminBrandAdapter
    lateinit var searchItemEt:EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAdminBrandBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adminBrandRecycler = binding.adminBrandRecycler
        searchItemEt = binding.searchAdminBrand.searchItemEt

        val layoutmanager = LinearLayoutManager(requireContext())
        adminBrandRecycler.layoutManager = layoutmanager




        searchItemEt.doOnTextChanged { text, start, before, count ->

            if (text!!.length >= 3){
                Toast.makeText(requireContext(), "$text", Toast.LENGTH_SHORT).show()
                searchAdminBrand(text.toString())
            }else{
                getAdimBrand()
            }
        }

        getAdimBrand()


    }


    private fun getAdimBrand() {

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = adminBrandListApiCall()

                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData?.status.equals("1")) {

                        val listItem: ArrayList<AdminBrandData> = responseData!!.brands_list
                        adminAdapter = AdminBrandAdapter(listItem, this@AdminBrandFragment)

                        adminBrandRecycler.adapter = adminAdapter

                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Response Code ${response.code()} and Response Message ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Exception Occured ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private suspend fun adminBrandListApiCall(): Response<AdminBrandModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getAdminBrandList()
        }
    }

    private fun searchAdminBrand(search: String) {


        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = searchAdminApiCall(search)

                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData?.status.equals("1")) {

                        val listItem: ArrayList<AdminBrandData> = responseData!!.brands_list
                        adminAdapter = AdminBrandAdapter(listItem, this@AdminBrandFragment)

                        adminBrandRecycler.adapter = adminAdapter

                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Response Code ${response.code()} and Response Message ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Exception Occured ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }


    private suspend fun searchAdminApiCall(searchQuery:String):Response<AdminBrandModel> {
        return withContext(Dispatchers.IO){
            BaseClient.getInstance.searchAdimItem(searchQuery)
        }
    }

    override fun BrandItemClicked(postion: String, brandName: String) {

        val action = AdminBrandFragmentDirections.actionAdminBrandFragmentToSearchStockItemFragment(brandName)
        requireView().findNavController().navigate(action)

    }


}