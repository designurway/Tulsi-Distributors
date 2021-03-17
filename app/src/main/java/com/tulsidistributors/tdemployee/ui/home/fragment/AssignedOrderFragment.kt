package com.tulsidistributors.tdemployee.ui.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.databinding.FragmentAssignedOrderBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.assign_order.AssignedOrderData
import com.tulsidistributors.tdemployee.model.assign_order.AssignedOrderModel
import com.tulsidistributors.tdemployee.ui.adapter.AssignedOrderAdapter
import com.tulsidistributors.tdemployee.ui.adapter.AssignedOrderClicked
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.lang.Exception


class AssignedOrderFragment : Fragment(), AssignedOrderClicked {

    lateinit var binding: FragmentAssignedOrderBinding
    lateinit var assignedOrderRecyclerView: RecyclerView
    lateinit var assignAdapter: AssignedOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAssignedOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        assignedOrderRecyclerView = binding.assignedOrderRecyclerView

        val layoutManager = LinearLayoutManager(requireContext())
        assignedOrderRecyclerView.layoutManager = layoutManager


        CoroutineScope(Dispatchers.Main).launch {

            try {
                val response = getAssignedOder("TD001")
                if (response.isSuccessful) {
                    val data = response.body()
                    val assignedOrder: ArrayList<AssignedOrderData> = data!!.assigned_order
                    assignAdapter = AssignedOrderAdapter(assignedOrder, this@AssignedOrderFragment)
                    assignedOrderRecyclerView.adapter = assignAdapter

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Response Code : ${response.code()} and Respone Message : ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(), "Exception Occured : ${e.message}", Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    suspend private fun getAssignedOder(empId: String): Response<AssignedOrderModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getAssignedOrder("TD001")
        }
    }

    override fun onItemClicked(postion: Int, address: String, id: String) {
        val action =
            AssignedOrderFragmentDirections.actionAssignedOrderFragmentToAddProductListFragment2(address = address,dealerId = id)
        requireView().findNavController().navigate(action)
    }


}