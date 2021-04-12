package com.tulsidistributors.tdemployee.ui.home.fragment.assigned_orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentOrderPlacedSucessfullyBinding
import com.tulsidistributors.tdemployee.ui.home.HomePageActivity


class OrderPlacedSucessfullyFragment : Fragment() {

    lateinit var binding:FragmentOrderPlacedSucessfullyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      binding = FragmentOrderPlacedSucessfullyBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as HomePageActivity).hideToolBar()

        binding.okBtn.setOnClickListener {
            val action = OrderPlacedSucessfullyFragmentDirections.actionOrderPlacedSucessfullyFragmentToHomeFragment()
            findNavController().navigate(action)
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
}