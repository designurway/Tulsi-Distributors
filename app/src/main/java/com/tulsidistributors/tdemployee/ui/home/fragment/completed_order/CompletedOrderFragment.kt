package com.tulsidistributors.tdemployee.ui.home.fragment.completed_order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentCompletedOrderBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.completed_order.CompletedOderModel
import com.tulsidistributors.tdemployee.model.completed_order.CompletedOrderData
import com.tulsidistributors.tdemployee.ui.adapter.CompletedOrderAdapter
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

        cOrderRecyclerView = binding.cOrderRecyclerView

        val layoutManager = LinearLayoutManager(requireContext())
        cOrderRecyclerView.layoutManager = layoutManager

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = getCompletedOrder()
                if (response.isSuccessful) {
                    val data = response.body()

                    val orderData: ArrayList<CompletedOrderData> = data!!.completed_order
                    compledtedAdapter =
                        CompletedOrderAdapter(orderData, this@CompletedOrderFragment)

                    cOrderRecyclerView.adapter = compledtedAdapter

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Response Code : ${response.code()} and Respone Message : ${response.message()}",
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

    suspend private fun getCompletedOrder(): Response<CompletedOderModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getCompletedOrder("TD001")
        }
    }

    override fun OnOrderItemClicked(position: Int, dealerId: String, date: String) {
        val action =
            CompletedOrderFragmentDirections.actionCompletedOrderFragmentToPlacedOrderListFragment(
                dealerId = dealerId,
                date
            )

        requireView().findNavController().navigate(action)
    }


}