package com.tulsidistributors.tdemployee.ui.home.fragment.search_stock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentSearchStockItemBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.search_stock.SearchStockItemData
import com.tulsidistributors.tdemployee.model.search_stock.SearchStockItemModel
import com.tulsidistributors.tdemployee.ui.adapter.SearchStockItemAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception


class SearchStockItemFragment : Fragment() {

    lateinit var binding: FragmentSearchStockItemBinding
    lateinit var searchStockRecycler: RecyclerView
    val args: SearchStockItemFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchStockItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        brandName = args.brandName

        searchStockRecycler = binding.searchStockRecycler

        val layoutManger = LinearLayoutManager(requireContext())
        searchStockRecycler.layoutManager = layoutManger

        binding.searchViewItem.searchItemEt.doOnTextChanged { text, start, before, count ->

            searchStockItem(text.toString(),"HAIER")
        }


        getStockItem("HAIER")

    }

    private fun getStockItem(brand:String) {

        Toast.makeText(requireContext(), "$brand", Toast.LENGTH_SHORT).show()

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = getStockItemApiCall(brand)

                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData?.status.equals("1")) {

                        val searchItem: ArrayList<SearchStockItemData> = responseData!!.stock_items
                        val adapter = SearchStockItemAdapter(searchItem)
                        searchStockRecycler.adapter = adapter

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "${responseData?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
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
                    "Exception occured ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private suspend fun getStockItemApiCall(brand:String): Response<SearchStockItemModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getStockItem(brand)
        }
    }


    private fun searchStockItem(searchQuery: String,brandNam:String) {

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = searchStockItme(searchQuery,brandNam)

                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData?.status.equals("1")) {

                        val searchItem: ArrayList<SearchStockItemData> = responseData!!.stock_items
                        val adapter = SearchStockItemAdapter(searchItem)
                        searchStockRecycler.adapter = adapter

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "${responseData?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
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
                    "Exception occured ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private suspend fun searchStockItme(search: String,brandNam:String): Response<SearchStockItemModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getStockItemList(brandName = brandNam, search = search)
        }
    }

}