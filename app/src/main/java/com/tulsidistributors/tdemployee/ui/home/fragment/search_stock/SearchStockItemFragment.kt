package com.tulsidistributors.tdemployee.ui.home.fragment.search_stock

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.tulsidistributors.tdemployee.databinding.FragmentSearchStockItemBinding
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.StatusMessageModel
import com.tulsidistributors.tdemployee.model.search_stock.SearchStockItemData
import com.tulsidistributors.tdemployee.model.search_stock.SearchStockItemModel
import com.tulsidistributors.tdemployee.ui.adapter.AddProductItemClickListner
import com.tulsidistributors.tdemployee.ui.adapter.OnAddItemClickListner
import com.tulsidistributors.tdemployee.ui.adapter.SearchStockItemAdapter
import com.tulsidistributors.tdemployee.ui.home.HomePageActivity
import com.tulsidistributors.tdemployee.utils.noDataFound
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception


class SearchStockItemFragment : Fragment(),OnAddItemClickListner {

    lateinit var binding: FragmentSearchStockItemBinding
    lateinit var searchStockRecycler: RecyclerView
    lateinit var brandName:String
    lateinit var userLoginPreferences: UserLoginPreferences
    lateinit var mContext:Context
    lateinit var brandId:String
    val args:SearchStockItemFragmentArgs by navArgs()
    lateinit var dealerId:String
    lateinit var saleExecutiveId:String
    lateinit var from:String
    lateinit var shimmerLayout: ShimmerFrameLayout

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

        (activity as HomePageActivity).hideToolBar()


        mContext = requireContext()
        dealerId = args.dealerId
        from = args.from

        shimmerLayout = binding.shimmerLayout
        shimmerLayout.startShimmer()

//        brandName = args.brandName

        userLoginPreferences = UserLoginPreferences(requireActivity().dataStore)

        getLoginDetail()



        searchStockRecycler = binding.searchStockRecycler

        val layoutManger = LinearLayoutManager(requireContext())
        searchStockRecycler.layoutManager = layoutManger

        binding.searchViewItem.searchItemEt.doAfterTextChanged {query ->

            searchStockItem(query.toString(),brandId)
        }


    }



    private fun getStockItem(brandId:String) {
        showToast(mContext, "$brandId")

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = getStockItemApiCall(brandId)

                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData?.status.equals("1")) {
                        noDataFound(searchStockRecycler,shimmerLayout)
                        val searchItem: ArrayList<SearchStockItemData> = responseData!!.stock_list
                        val adapter = SearchStockItemAdapter(searchItem,this@SearchStockItemFragment,from = from)
                        searchStockRecycler.adapter = adapter

                    } else {
                        showToast(mContext, "${responseData?.message}")

                    }
                } else {
                    showToast(mContext, "Response Code ${response.code()} and Response Message ${response.message()}")

                }

            } catch (e: Exception) {
                showToast(mContext, "Exception occured ${e.message}")

            }
        }
    }

    private suspend fun getStockItemApiCall(brandId:String): Response<SearchStockItemModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getStockItem(brandId)
        }
    }


    private fun searchStockItem(searchQuery: String, brandId:String) {

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = searchStockItme(searchQuery,brandId)

                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData?.status.equals("1")) {

                        shimmerLayout.stopShimmer()
                        noDataFound(searchStockRecycler,shimmerLayout)
                        val searchItem: ArrayList<SearchStockItemData> = responseData!!.stock_list
                        val adapter = SearchStockItemAdapter(searchItem,this@SearchStockItemFragment,from = from)
                        searchStockRecycler.adapter = adapter

                    } else {
                        showToast(mContext, "${responseData?.message}")

                    }
                } else {
                    showToast(mContext, "Response Code ${response.code()} and Response Message ${response.message()}")

                }

            } catch (e: Exception) {
                showToast(mContext, "Exception occured ${e.message}")

            }
        }

    }

    private suspend fun searchStockItme(search: String,brandId:String): Response<SearchStockItemModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getStockItemList(brandId = brandId, search = search)
        }
    }

    private fun getLoginDetail() {
      userLoginPreferences.brandIdFlow.asLiveData().observe(viewLifecycleOwner,{
          brandId = it.toString()
          showToast(mContext,it.toString())
          getStockItem(brandId)
      })

        userLoginPreferences.saleExecutiveIdFlow.asLiveData().observe(viewLifecycleOwner,{
            saleExecutiveId = it.toString()
        })
    }

    override fun onAddBtnClicked(
        positon: Int,
        productId: String,
        addItemBtn: Button,
        alreadyAddedBtn: Button
    ) {

        showToast(mContext,"ProductId : $productId")

        addProductToDealer(saleExecutiveId,dealerId,productId,addItemBtn,alreadyAddedBtn)
    }

    fun addProductToDealer(saleExeId:String,dealerId:String,productId:String,addItemBtn:Button,alreadyAddedBtn:Button){
        viewLifecycleOwner.lifecycleScope.launch {
            try {

                val response = addProductToDealerApiCall(saleExeId, dealerId, productId)

                if (response.isSuccessful){
                    val responseData = response.body()
//                    showToast(mContext, responseData!!.message)

                    // hiding the view
                    noDataFound(alreadyAddedBtn,addItemBtn)

                }else{
                    showToast(mContext,"Response Error ${response.message()}")
                }

            }catch (e:Exception){
                showToast(mContext,"Execption Occured ${e.message}")
            }
        }
    }


    suspend fun addProductToDealerApiCall(saleExeId:String,dealerId:String,productId:String):Response<StatusMessageModel>{
        return  withContext(Dispatchers.IO){
            BaseClient.getInstance.addProductToDealer(sales_executive_id = saleExeId,dealer_id = dealerId,product_id = productId)
        }
    }


    override fun onResume() {
        super.onResume()
        showToast(mContext,"dddd")
        (activity as HomePageActivity).hideToolBar()
    }


    override fun onPause() {
        super.onPause()

        (activity as HomePageActivity).showToolbar()
    }

}