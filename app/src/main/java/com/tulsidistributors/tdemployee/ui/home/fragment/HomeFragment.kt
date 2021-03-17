package com.tulsidistributors.tdemployee.ui.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentHomeBinding
import com.tulsidistributors.tdemployee.ui.adapter.HomeAdapter
import com.tulsidistributors.tdemployee.model.home.CategoryModel
import com.tulsidistributors.tdemployee.ui.adapter.HomeItemClicked


class HomeFragment : Fragment(), HomeItemClicked {

    lateinit var binding: FragmentHomeBinding
    lateinit var categoryRv: RecyclerView
    var categoryAdapter: HomeAdapter? = null
    lateinit var action:NavDirections

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

        categoryRv = binding.categoryRv


        val list: ArrayList<CategoryModel> = ArrayList()
        list.add(
            CategoryModel(
                "Assigned",
                requireActivity().getDrawable(R.drawable.assigned_task)!!
            )
        )

        list.add(
            CategoryModel(
                "Completed",
                requireActivity().getDrawable(R.drawable.completed_task)!!
            )
        )

        list.add(
            CategoryModel(
                "Stocks",
                requireActivity().getDrawable(R.drawable.packages)!!
            )
        )

        list.add(
            CategoryModel(
                "Attendance",
                requireActivity().getDrawable(R.drawable.attendance)!!
            )
        )

        /* list.add(CategoryModel("Settings",
             requireActivity().getDrawable(R.drawable.setting)!!
         ))*/


        categoryAdapter = HomeAdapter(list, this)
        val gridLayoutManager = GridLayoutManager(activity, 2)
        categoryRv.setLayoutManager(gridLayoutManager)
        categoryRv.setAdapter(categoryAdapter)

    }

    override fun homeItemClickedListner(name: String) {
//        Toast.makeText(requireContext(), "Item Clicked : ${name}", Toast.LENGTH_SHORT).show()

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
                action = HomeFragmentDirections.actionHomeFragmentToStockFragment()
                requireView().findNavController().navigate(action)
            }

            "Attendance" -> {
                action = HomeFragmentDirections.actionHomeFragmentToAttendenceFragment()
                requireView().findNavController().navigate(action)
            }
        }
    }
}
