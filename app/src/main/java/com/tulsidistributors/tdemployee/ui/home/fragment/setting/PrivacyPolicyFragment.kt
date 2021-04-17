package com.tulsidistributors.tdemployee.ui.home.fragment.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.AddProductItemsBinding
import com.tulsidistributors.tdemployee.databinding.FragmentPrivacyPolicyBinding
import com.tulsidistributors.tdemployee.databinding.FragmentTermsConditionBinding


class PrivacyPolicyFragment : Fragment() {

    lateinit var binding: FragmentPrivacyPolicyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPrivacyPolicyBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.privacyWebView.loadUrl("https://idlydose.in/Gugliya/TulsiDistributorApi/privacy_policy.html")
    }

}