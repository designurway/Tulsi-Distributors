package com.tulsidistributors.tdemployee.ui.home.fragment.completed_order

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentCompletedOrderBinding
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.completed_order.CompletedOderModel
import com.tulsidistributors.tdemployee.model.completed_order.CompletedOrderData
import com.tulsidistributors.tdemployee.ui.adapter.CompletedOrderAdapter
import com.tulsidistributors.tdemployee.utils.noDataFound
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception


class CompletedOrderFragment : Fragment(), CompletedOrderAdapter.OnCompletedOrderClicked {

    lateinit var binding: FragmentCompletedOrderBinding
    lateinit var cOrderRecyclerView: RecyclerView
    lateinit var compledtedAdapter: CompletedOrderAdapter
    lateinit var userLoginPreferences: UserLoginPreferences
    lateinit var EmpId:String
    lateinit var mContext:Context
    lateinit var shimmerLayout:ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentCompletedOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext =  requireContext()

        userLoginPreferences = UserLoginPreferences(requireActivity().dataStore)

        cOrderRecyclerView = binding.cOrderRecyclerView

        shimmerLayout = binding.shimmerLayout
        shimmerLayout.startShimmer()

        val layoutManager = LinearLayoutManager(requireContext())
        cOrderRecyclerView.layoutManager = layoutManager

        getLoginDetail()

    }

    fun getCompletedOrder(empId:String){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = getCompletedOrderApiCall(empId)
                if (response.isSuccessful) {

                    shimmerLayout.stopShimmer()
                    noDataFound(cOrderRecyclerView,shimmerLayout)

                    val data = response.body()

                    val orderData: ArrayList<CompletedOrderData> = data!!.completed_order
                    compledtedAdapter =
                        CompletedOrderAdapter(orderData, this@CompletedOrderFragment)

                    cOrderRecyclerView.adapter = compledtedAdapter



                } else {
                    showToast(mContext, "Response Code : ${response.code()} and Respone Message : ${response.message()}")

                }
            } catch (e: Exception) {
                showToast(mContext, "Exception occured ${e.message}")


            }
        }
    }

    suspend private fun getCompletedOrderApiCall(empId:String): Response<CompletedOderModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getCompletedOrder(empId)
        }
    }

    override fun OnOrderItemClicked(position: Int, dealerId: String, date: String) {
        val action =
            CompletedOrderFragmentDirections.actionCompletedOrderFragmentToPlacedOrderListFragment(dealerId = dealerId,purchaseDate = date)

        requireView().findNavController().navigate(action)
    }

    fun getLoginDetail(){
        userLoginPreferences.empIdFlow.asLiveData().observe(viewLifecycleOwner,{
            EmpId = it.toString()

            getCompletedOrder(it.toString())
        })
    }


}