package com.tulsidistributors.tdemployee.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentProfileBinding
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.user_detail.UserDetailModel
import com.tulsidistributors.tdemployee.ui.auth.AuthActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    lateinit var action: NavDirections
    lateinit var name:String
    lateinit var imageUrl:String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserDetail()

        binding.editProfileBtn.setOnClickListener {
            action = ProfileFragmentDirections.actionProfileFragmentToUpdateProfileFragment(name = name,imageUrl = imageUrl)
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

        binding.signoutLayout.setOnClickListener {
            val userLoginPreferences = UserLoginPreferences(requireActivity().dataStore)

            viewLifecycleOwner.lifecycleScope.launch {
                val abc = userLoginPreferences.logout()
                val intent = Intent(requireActivity(), AuthActivity::class.java)
                requireContext().startActivity(intent)
                requireActivity().finish()

            }
        }
    }

    private fun getUserDetail() {

        viewLifecycleOwner.lifecycleScope.launch {

            try {
                val response = getUserDetailApiCall()

                if (response.isSuccessful) {

                    val responseData = response.body()
                    if (responseData!!.status.equals("1")){

                        Toast.makeText(requireContext(), "${responseData.message}", Toast.LENGTH_SHORT).show()

                        name = responseData.first_name

                        binding.name.text = name
                        binding.mobile.text = responseData.phone_number
                         imageUrl = "http://192.168.4.166:8000/${responseData.profile}"
//                        Glide.with(requireView()).load(imageUrl).into(binding.profileImg)
                    }else{
                        Toast.makeText(requireContext(), "${responseData.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Response Message ${response.message()}", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Exception Occured ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private suspend fun getUserDetailApiCall(): Response<UserDetailModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getUserDetails("TD001")
        }
    }
}