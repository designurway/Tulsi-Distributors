package com.tulsidistributors.tdemployee.ui.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.dataModel
import com.tulsidistributors.tdemployee.databinding.AddProductItemsBinding
import com.tulsidistributors.tdemployee.databinding.FragmentAddProductListBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.get_admin_product.DealerProductData
import com.tulsidistributors.tdemployee.model.get_admin_product.DealerProductModel
import com.tulsidistributors.tdemployee.ui.adapter.AddProductAdapter
import com.tulsidistributors.tdemployee.ui.adapter.AddProductItemClickListner
import com.tulsidistributors.tdemployee.ui.home.HomePageActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class AddProductListFragment : Fragment(), AddProductItemClickListner {

    val args: AddProductListFragmentArgs by navArgs()
    lateinit var binding: FragmentAddProductListBinding
    lateinit var productRecycler: RecyclerView
    lateinit var show_btn: AppCompatButton
    var id: String = ""
    var shop_address: String = ""
    var shopName: String = ""
     var totalQty:Int =0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAddProductListBinding.inflate(inflater, container, false)

        (activity as HomePageActivity).hideToolBar()

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = args.dealerId
        shop_address = args.address
        shopName = args.shopName

        productRecycler = binding.listRv
        show_btn = binding.showBtn

        binding.shopAddressTv.text = shop_address
        binding.shopNameTv.text = shopName


        val layoutManager = LinearLayoutManager(requireContext())
        productRecycler.layoutManager = layoutManager

        binding.addProdSearch.searchItemEt.doOnTextChanged { text, start, before, count ->

            if (text!!.length >= 2) {
                searchDealerProductItem(id, searchQuery = text.toString())
            } else {
                getDealerProductItem(id)
            }
        }

        getDealerProductItem(id)

    }

    private fun getDealerProductItem(dealerId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val response = getDealerProductItemApiCall(dealerId = dealerId)

            try {
                if (response.isSuccessful) {
                    val responseData = response.body()

                    if (responseData?.status.equals("1")) {
                        val productItem: ArrayList<DealerProductData> =
                            responseData!!.product_details
                        val adapter = AddProductAdapter(productItem, this@AddProductListFragment)
                        productRecycler.adapter = adapter
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
                        "Response Code ${response.code()} Response Message ${response.message()}",
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

    private suspend fun getDealerProductItemApiCall(dealerId: String): Response<DealerProductModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getDealerProductItem("1", "TD001")
        }
    }


    private fun searchDealerProductItem(dealerId: String, searchQuery: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val response = searchDealerProductItemApiCall(dealerId = dealerId, searchQuery)

            try {
                if (response.isSuccessful) {
                    val responseData = response.body()

                    if (responseData?.status.equals("1")) {
                        val productItem: ArrayList<DealerProductData> =
                            responseData!!.product_details
                        val adapter = AddProductAdapter(productItem, this@AddProductListFragment)
                        productRecycler.adapter = adapter
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
                        "Response Code ${response.code()} Response Message ${response.message()}",
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

    private suspend fun searchDealerProductItemApiCall(
        dealerId: String,
        searchQuery: String
    ): Response<DealerProductModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.searchDealerProductItem("1", searchQuery, "TD001")
        }
    }


    override fun onResume() {
        super.onResume()
        (activity as HomePageActivity).hideToolBar()
    }


    override fun onPause() {
        super.onPause()

        (activity as HomePageActivity).showToolbar()
    }

    override fun addButtonClicked(position: String, productQty: String) {
        Toast.makeText(
            requireContext(),
            "Add buttuon Clicked $position Product $productQty",
            Toast.LENGTH_SHORT
        ).show()

    }
    var qty:Int = 0
    override fun plusButtonClicked(position: String, productQty: String,productQtyTxt: TextView) {

//        val productQtyTxt:TextView = binding.findViewById(R.id.product_qty_tv)

//
//       var  model :dataModel?=null
////
//        model?.Qnty= qty
////
//        totalQty += model!!.Qnty

//        productQtyTxt.text = totalQty.toString()
        qty=qty+1
//        model.Qnty=qty

        Toast.makeText(
            requireContext(),
            "Plus buttuon Clicked $position Product $qty",
            Toast.LENGTH_SHORT
        ).show()

    }

    override fun minusButtonClicked(position: String, productQty: String, productQtyTxt: TextView) {
        Toast.makeText(
            requireContext(),
            "Minus buttuon Clicked $position Product $productQty",
            Toast.LENGTH_SHORT
        ).show()
    }


}