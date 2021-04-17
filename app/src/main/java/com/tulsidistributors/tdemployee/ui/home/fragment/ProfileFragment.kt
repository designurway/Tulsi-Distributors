package com.tulsidistributors.tdemployee.ui.home.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.tulsidistributors.tdemployee.databinding.FragmentProfileBinding
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.StatusMessageModel
import com.tulsidistributors.tdemployee.model.user_detail.UserDetailModel
import com.tulsidistributors.tdemployee.ui.auth.AuthActivity
import com.tulsidistributors.tdemployee.utils.AutoLogout
import com.tulsidistributors.tdemployee.utils.Common
import com.tulsidistributors.tdemployee.utils.showToast
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
            action = ProfileFragmentDirections.actionProfileFragmentToUpdateProfileFragment(
                name = name, imageUrl = imageUrl,
                phoneNumber = phoneNum
            )
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

                updateLogoutTime()

            }
        }
    }

    private fun updateLogoutTime() {

        viewLifecycleOwner.lifecycleScope.launch {
            try {

                val date = Common().getCurrentDate("yyyy-MM-dd")

                val response = updateLogoutTimeApiCall(empId, date)

                if (response.isSuccessful){

                    val responseData = response.body()

                    showToast(mContext, responseData!!.message)

                    AutoLogout(mContext).cancelAlarm()

                    userLoginPreferences.saveIsLoggedIn(false)
                    val intent = Intent(mContext, AuthActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

                    requireContext().startActivity(intent)
                    requireActivity().finishAffinity()




                }else{
                    showToast(mContext, "Response Error ${response.message()}")
                }
            }catch (e: java.lang.Exception){
                showToast(mContext, "Exception Occurred ${e.message}")
            }
        }

    }

    private suspend fun updateLogoutTimeApiCall(empID: String, loginDate: String) :Response<StatusMessageModel> {
        return withContext(Dispatchers.IO){
            BaseClient.getInstance.updateLogoutTime(empID, loginDate)

        }

    }

    private fun getLoginDetails() {
        userLoginPreferences.empIdFlow.asLiveData().observe(viewLifecycleOwner, {
            empId = it.toString()
            getUserDetail(it.toString())

        })
    }

    private fun getUserDetail(empID: String) {

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

    private suspend fun getUserDetailApiCall(empID: String): Response<UserDetailModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.getUserDetails(
                empID
            )
        }
    }

}