package com.tulsidistributors.tdemployee.ui.home.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    lateinit var action: NavDirections
     var name:String =""
    lateinit var imageUrl:String
    lateinit var mContext:Context
    lateinit var userLoginPreferences: UserLoginPreferences
    lateinit var empId:String
    lateinit var phoneNum:String
    lateinit var editProfileBtn:Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userLoginPreferences = UserLoginPreferences(requireActivity().dataStore)

        getLoginDetails()

        mContext = requireContext()

        editProfileBtn= binding.editProfileBtn

        binding.editProfileBtn.setOnClickListener {
            action = ProfileFragmentDirections.actionProfileFragmentToUpdateProfileFragment(name = name,imageUrl = imageUrl,
            phoneNumber = phoneNum)
            requireView().findNavController().navigate(action)
        }

     /*   binding.changPassLayout.setOnClickListener {
            action = ProfileFragmentDirections.actionProfileFragmentToChangePasswordFragment()
            requireView().findNavController().navigate(action)
        }*/


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
                val abc = userLoginPreferences.saveIsLoggedIn(false)
                val intent = Intent(requireActivity(), AuthActivity::class.java)
                requireContext().startActivity(intent)
                requireActivity().finish()

            }
        }
    }

    private fun getLoginDetails() {
        userLoginPreferences.empIdFlow.asLiveData().observe(viewLifecycleOwner,{
            empId = it.toString()
            getUserDetail(it.toString())

        })
    }

    private fun getUserDetail(empID:String) {

        viewLifecycleOwner.lifecycleScope.launch {

            try {
                val response = getUserDetailApiCall(empID)

                if (response.isSuccessful) {

                    val responseData = response.body()
                    if (responseData!!.status.equals("1")){
                        showToast(mContext, "${responseData.message}")

                        name = responseData.first_name

                        binding.name.text = name
                        binding.mobile.text = responseData.phone_number
                        imageUrl = responseData.profile
                        phoneNum = responseData.phone_number
                        editProfileBtn.visibility=View.VISIBLE
                        Glide.with(requireView()).load(imageUrl).into(binding.profileImg)


                    }else{
                        showToast(mContext, "${responseData.message}")
                    }
                } else {
                    showToast(mContext, "Response Message ${response.message()}")
                }

            } catch (e: Exception) {
                showToast(mContext, "Exception Occured ${e.message}")

            }
        }

    }

    private suspend fun getUserDetailApiCall(empID:String): Response<UserDetailModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getUserDetails(empID
            )
        }
    }
}