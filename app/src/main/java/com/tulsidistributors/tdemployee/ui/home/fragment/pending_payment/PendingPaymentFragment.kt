package com.tulsidistributors.tdemployee.ui.home.fragment.pending_payment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentPendingPaymentBinding
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.assign_order.AssignedOrderData
import com.tulsidistributors.tdemployee.model.assign_order.AssignedOrderModel
import com.tulsidistributors.tdemployee.ui.adapter.AssignedOrderAdapter
import com.tulsidistributors.tdemployee.ui.adapter.AssignedOrderClicked
import com.tulsidistributors.tdemployee.utils.noDataFound
import com.tulsidistributors.tdemployee.utils.showLog
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception


class PendingPaymentFragment : Fragment(), AssignedOrderClicked {

    lateinit var binding:FragmentPendingPaymentBinding
    lateinit var pendingPaymentRecyclerView: RecyclerView
    lateinit var assignAdapter: AssignedOrderAdapter
    lateinit var mContext: Context
    lateinit var shimmerLayout: ShimmerFrameLayout
    lateinit var userLoginPreferences: UserLoginPreferences
    var EmpId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         binding =   FragmentPendingPaymentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()

        userLoginPreferences = UserLoginPreferences(requireActivity().dataStore)

        pendingPaymentRecyclerView = binding.pendingPaymentRecyclerView
        shimmerLayout = binding.shimmerLayout

        getUserDetails()

        val layoutManager = LinearLayoutManager(requireContext())
        pendingPaymentRecyclerView.layoutManager = layoutManager

    }


    private fun getAssignedOrder(empId:String){
        CoroutineScope(Dispatchers.Main).launch {

            try {
                val response = getAssignedOderApiCall(empId)
                if (response.isSuccessful) {
                    val data = response.body()
                    val assignedOrder: ArrayList<AssignedOrderData> = data!!.assigned_order
                    assignAdapter = AssignedOrderAdapter(assignedOrder, this@PendingPaymentFragment)
                    pendingPaymentRecyclerView.adapter = assignAdapter

                    shimmerLayout.stopShimmer()
                    noDataFound(pendingPaymentRecyclerView,shimmerLayout)

                } else {

                    showToast(mContext,"Response Code : ${response.code()} and Respone Message : ${response.message()}")

                }
            } catch (e: Exception) {

                showToast(mContext,"Exception Occured : ${e.message}")

            }
        }
    }

    suspend private fun getAssignedOderApiCall(empId: String): Response<AssignedOrderModel> {
        return withContext(Dispatchers.IO) {
            showLog("empId","EmpId: $empId")
            BaseClient.getInstance.getAssignedOrder(empId)
        }
    }

    override fun onItemClicked(
        postion: Int,
        shopName: String,
        dealer_id: String,
        shop_address: String,
        latitude: String,
        longitude: String,
        routingId: String
    ) {
        val action = PendingPaymentFragmentDirections.actionPendingPaymentFragmentToPendingPaymentListFragment(dealerId = dealer_id)
        findNavController().navigate(action)
    }

    private fun getUserDetails(){

//        val emp = GetUserDetails(userLoginPreferences,viewLifecycleOwner).getEmpId()

        userLoginPreferences.empIdFlow.asLiveData().observe(viewLifecycleOwner,{
            EmpId = it.toString()
            getAssignedOrder(it.toString())
            showToast(mContext,EmpId)
        })


    }


}