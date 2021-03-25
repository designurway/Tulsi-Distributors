package com.tulsidistributors.tdemployee.ui.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    lateinit var action: NavDirections

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editProfileBtn.setOnClickListener {
            action = ProfileFragmentDirections.actionProfileFragmentToUpdateProfileFragment()
            requireView().findNavController().navigate(action)
        }

        binding.changPassLayout.setOnClickListener {
            action = ProfileFragmentDirections.actionProfileFragmentToChangePasswordFragment()
            requireView().findNavController().navigate(action)
        }


        binding.supportLayout.setOnClickListener {

            action = ProfileFragmentDirections.actionProfileFragmentToSupportFragment()
            requireView().findNavController().navigate(action)
        }


        binding.privacyTxt.setOnClickListener {
            action = ProfileFragmentDirections.actionProfileFragmentToPrivacyPolicyFragment()
            requireView().findNavController().navigate(action)
        }


        binding.termsTxt.setOnClickListener {
            action = ProfileFragmentDirections.actionProfileFragmentToTermsConditionFragment()
            requireView().findNavController().navigate(action)
        }


        binding.aboutTxt.setOnClickListener {
            action = ProfileFragmentDirections.actionProfileFragmentToAboutUsFragment()
            requireView().findNavController().navigate(action)
        }
    }
}