package com.tulsidistributors.tdemployee.ui.home.fragment.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentTermsConditionBinding


class TermsConditionFragment : Fragment() {

    lateinit var binding:FragmentTermsConditionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      binding = FragmentTermsConditionBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.termsWebView.loadUrl("https://stackoverflow.com/questions/8614553/can-someone-give-one-exact-example-of-webview-implementation-in-android")
    }

}