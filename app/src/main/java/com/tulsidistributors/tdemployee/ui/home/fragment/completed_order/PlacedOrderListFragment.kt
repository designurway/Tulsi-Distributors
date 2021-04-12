package com.tulsidistributors.tdemployee.ui.home.fragment.completed_order

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowId
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.tulsidistributors.tdemployee.databinding.FragmentPlacedOrderListBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.placed_order_list.PlacedOrderListData
import com.tulsidistributors.tdemployee.model.placed_order_list.PlacedOrderListModel
import com.tulsidistributors.tdemployee.ui.adapter.PlaceOrderListAdapter
import com.tulsidistributors.tdemployee.utils.noDataFound
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception


class PlacedOrderListFragment : Fragment() {

    lateinit var binding:FragmentPlacedOrderListBinding
    lateinit var placeOrderListRecycler:RecyclerView
    val args:PlacedOrderListFragmentArgs by navArgs()
    lateinit var dealerId:String
    lateinit var date:String
    lateinit var mContext:Context
    lateinit var shimmerLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlacedOrderListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()

        dealerId = args.dealerId
        date = args.purchaseDate

        placeOrderListRecycler = binding.placeOrderListRecycler
        shimmerLayout = binding.shimmerLayout
        shimmerLayout.startShimmer()

        val layoutManager = LinearLayoutManager(requireContext())
        placeOrderListRecycler.layoutManager = layoutManager

        getPlacedOrderList()

    }

    private fun getPlacedOrderList() {
        viewLifecycleOwner.lifecycleScope.launch {

            try {
                val response = getPlacedOrderListApiCall()

                if (response.isSuccessful){
                    val responseData = response.body()
                    val placeOrderList:ArrayList<PlacedOrderListData> = responseData!!.placed_order_list

                    val placedOrderAdapter = PlaceOrderListAdapter(placeOrderList)
                    placeOrderListRecycler.adapter = placedOrderAdapter
                    shimmerLayout.stopShimmer()
                    noDataFound(placeOrderListRecycler,shimmerLayout)
                    showToast(mContext, "Sucess ${responseData.message}")
                }else{
                    showToast(mContext, "Response Message ${response.message()}")
                }
            }catch (e:Exception){
                showToast(mContext, "Exception Occured ${e.message}")
            }
        }
    }


    suspend private  fun getPlacedOrderListApiCall():Response<PlacedOrderListModel>{
        return withContext(Dispatchers.IO){
            BaseClient.getInstance.getPlacedOrderList(dealerId,date)
        }
    }


}