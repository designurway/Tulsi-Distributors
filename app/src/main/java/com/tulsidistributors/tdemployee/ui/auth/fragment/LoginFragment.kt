package com.tulsidistributors.tdemployee.ui.auth.fragment

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentLoginBinding
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.login.LoginModel
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.*
import retrofit2.Response


class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    lateinit var forgotPass: TextView
    lateinit var loginEmpId: EditText
    lateinit var loginPassword: EditText
    lateinit var loginSubmit: ImageView
    lateinit var loginPrefrence: UserLoginPreferences

    lateinit var telephonyManager: TelephonyManager
    lateinit var imeiNumber: String
    private val REQUEST_CODE = 101
    lateinit var mContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()

//        showToast(mContext,"Login Page")

        loginPrefrence = UserLoginPreferences(requireActivity().dataStore)

        getLoginDetails()

        forgotPass = binding.forgotPassTxt
        loginEmpId = binding.emailEt
        loginPassword = binding.passwordEt
        loginSubmit = binding.signBtn

        forgotPass.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment()
            view.findNavController().navigate(action)

        }

        loginSubmit.setOnClickListener {
            val empId = loginEmpId.text.toString().trim()
            val pwd = loginPassword.text.toString().trim()

            if (empId.isEmpty()) {
                loginEmpId.error = "Required fields"
                loginEmpId.requestFocus()
                return@setOnClickListener
            } else
                if (pwd.isEmpty()) {
                    loginEmpId.error = "Required fields"
                    loginEmpId.requestFocus()
                    return@setOnClickListener
                }


            CoroutineScope(Dispatchers.Main).launch {

                try {
                    val response = executiveLogin(empId, pwd)

                    if (response.isSuccessful) {
                        val data = response.body()

                        if (data?.status.equals("1")) {

                            //Saveing LoginDetail In DataStore
                            val emp_id = data!!.data.emp_id
                            val sale_executive_id = data.data.sales_executive_id
                            val distributor_id = data.data.distributor_id
                            val email = data.data.email
                            val phone_number = data.data.phone_number
                            val role = data.data.role
                            val brand_name = data.data.brand_name
                            val brand_id = data.data.brand_id


                            loginPrefrence.saveUserLoginDetail(
                                sale_executive_id = sale_executive_id,
                                empId = emp_id,
                                distributor_id = distributor_id,
                                email = email,
                                phone = phone_number,
                                role = role,
                                brand_name = brand_name,
                                brand_id = brand_id,
                                loggedInFirstTime = true
                            )
                            showToast(mContext, data!!.message)
                            val action =
                                LoginFragmentDirections.actionLoginFragmentToSelfieFragment()
                            view.findNavController().navigate(action)

                            requireView().findNavController().popBackStack(R.id.selfieFragment,true)


                        } else {
                            showToast(mContext, " ${data!!.message}")
                        }

                    } else {

                        showToast(
                            mContext,
                            "Response Code ${response.code()} Response Message : ${response.message()}"
                        )

                    }
                } catch (e: Exception) {
                    // Show API error. This is the error raised by the client.
                    showToast(mContext, "Exception Occurred: ${e.message}")
                }


            }
        }

    }

    private fun getLoginDetails() {
        loginPrefrence.loggedInFirstTime.asLiveData().observe(viewLifecycleOwner, { loggedInFirstTime ->
            if (loggedInFirstTime==true){
              val action = LoginFragmentDirections.actionLoginFragmentToSelfieFragment()
                findNavController().navigate(action)

            }
        })
    }


    suspend private fun executiveLogin(empId: String, pwd: String): Response<LoginModel> {
        return withContext(Dispatchers.IO) {

            BaseClient.getInstance.executiveLogin(empId, pwd)
        }
    }

    private fun getLoginData() {
        loginPrefrence.empIdFlow.asLiveData().observe(viewLifecycleOwner, { empId ->
            showToast(mContext, "EmpId: $empId")
        })
    }


}