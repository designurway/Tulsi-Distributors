package com.tulsidistributors.tdemployee.ui.home.fragment.pending_payment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.databinding.FragmentPendingPaymentListBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.pending_payment_list.PendingPaymentListData
import com.tulsidistributors.tdemployee.model.pending_payment_list.PendingPaymentListModel
import com.tulsidistributors.tdemployee.ui.adapter.OnPendingListClicked
import com.tulsidistributors.tdemployee.ui.adapter.PendingPaymentListAdapter
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response


class PendingPaymentListFragment : Fragment(), OnPendingListClicked {

    lateinit var binding: FragmentPendingPaymentListBinding
    val args: PendingPaymentListFragmentArgs by navArgs()
    lateinit var dealerId: String
    lateinit var mContext: Context
    lateinit var pndingListRecycler: RecyclerView
    lateinit var adapter: PendingPaymentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPendingPaymentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()

        dealerId = args.dealerId

        pndingListRecycler = binding.pndingListRecycler

        getPendingPaymentList()


        val layoutManager = LinearLayoutManager(mContext)

        pndingListRecycler.layoutManager = layoutManager


    }

    private fun getPendingPaymentList() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = getPendingPaymentListApiCall()
                if (response.isSuccessful) {
                    val responseData = response.body()
                    val data: ArrayList<PendingPaymentListData> = responseData!!.pending_order_list
                    adapter = PendingPaymentListAdapter(responseData.pending_order_list,this@PendingPaymentListFragment)
                    pndingListRecycler.adapter = adapter
                } else {
                    showToast(mContext, "Response Message ${response.message()}")
                }
            } catch (e: Exception) {
                showToast(mContext, "Exception Occured ${e.message}")
            }
        }
    }

    private suspend fun getPendingPaymentListApiCall(): Response<PendingPaymentListModel> {
        return with(Dispatchers.IO) {
            BaseClient.getInstance.getPendingPaymentList(dealerId)
        }
    }

    override fun OnPendingItemClicked(
        position: Int,
        totalAmnt: String,
        advanceAmount: String,
        pendingAmnt: String,
        refrenceId: String
    ) {
        val action =
            PendingPaymentListFragmentDirections.actionPendingPaymentListFragmentToUpdatePendingAmountFragment(
                dealerId = dealerId, pendingAmount = pendingAmnt,
                totalAmount = totalAmnt,
                advanceAmount = advanceAmount,
                refrenceId=refrenceId
            )
        findNavController().navigate(action)
    }


}