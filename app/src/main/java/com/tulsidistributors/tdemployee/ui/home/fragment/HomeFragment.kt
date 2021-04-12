package com.tulsidistributors.tdemployee.ui.home.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentHomeBinding
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.ui.adapter.HomeAdapter
import com.tulsidistributors.tdemployee.model.home.CategoryModel
import com.tulsidistributors.tdemployee.ui.adapter.HomeItemClicked
import com.tulsidistributors.tdemployee.utils.showToast


class HomeFragment : Fragment(), HomeItemClicked {

    lateinit var binding: FragmentHomeBinding
    lateinit var categoryRv: RecyclerView
    var categoryAdapter: HomeAdapter? = null
    lateinit var action: NavDirections
    lateinit var userLoginPreferences: UserLoginPreferences
    lateinit var brandId:String
    lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()

        userLoginPreferences = UserLoginPreferences(requireActivity().dataStore)

        getLoginDetails()

        categoryRv = binding.categoryRv


        val list: ArrayList<CategoryModel> = ArrayList()
        list.add(
            CategoryModel(
                "Assigned",
                ContextCompat.getDrawable(requireContext(), R.drawable.assigned_task)!!
            )
        )

        list.add(
            CategoryModel(
                "Completed",
                ContextCompat.getDrawable(requireContext(), R.drawable.completed_task)!!

            )
        )

        list.add(
            CategoryModel(
                "Stocks",
                ContextCompat.getDrawable(requireContext(), R.drawable.packages)!!
            )
        )

        list.add(
            CategoryModel(
                "Attendance",
                ContextCompat.getDrawable(requireContext(), R.drawable.attendance)!!
            )
        )


        list.add(
            CategoryModel(
                "Pending Payment",
                ContextCompat.getDrawable(requireContext(), R.drawable.pending_payment )!!
            )
        )


        categoryAdapter = HomeAdapter(list, this)
        val gridLayoutManager = GridLayoutManager(activity, 2)
        categoryRv.setLayoutManager(gridLayoutManager)
        categoryRv.setAdapter(categoryAdapter)

    }

    private fun getLoginDetails() {
        userLoginPreferences.brandIdFlow.asLiveData().observe(viewLifecycleOwner,{
            brandId = it.toString()
        })
    }

    override fun homeItemClickedListner(name: String) {

        when (name) {
            "Assigned" -> {
                action = HomeFragmentDirections.actionHomeFragmentToAssignedOrderFragment()
                requireView().findNavController().navigate(action)
            }
            "Completed" -> {
                action = HomeFragmentDirections.actionHomeFragmentToCompletedOrderFragment()
                requireView().findNavController().navigate(action)
            }

            "Stocks" -> {
                action = HomeFragmentDirections.actionHomeFragmentToStockItemFragment(dealerId = "none",from = "home")
                requireView().findNavController().navigate(action)
                showToast(mContext,brandId)
            }

            "Attendance" -> {
                action = HomeFragmentDirections.actionHomeFragmentToAttendenceFragment()
                requireView().findNavController().navigate(action)
            }

            "Pending Payment" ->{
                action = HomeFragmentDirections.actionHomeFragmentToPendingPaymentFragment()
                requireView().findNavController().navigate(action)
            }
        }
    }
}
