package com.tulsidistributors.tdemployee.ui.auth.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.datastore.dataStore
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.tulsidistributors.tdemployee.databinding.FragmentLoginBinding
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.login.LoginModel
import kotlinx.coroutines.*
import com.tulsidistributors.tdemployee.datastore.dataStore
import retrofit2.Response
import java.lang.Exception


class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    lateinit var forgotPass: TextView
    lateinit var loginEmpId: EditText
    lateinit var loginPassword: EditText
    lateinit var loginSubmit: ImageView
    lateinit var loginPrefrence:UserLoginPreferences

    lateinit var telephonyManager: TelephonyManager
    lateinit var imeiNumber: String
    private val REQUEST_CODE = 101

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

        loginPrefrence = UserLoginPreferences(requireActivity().dataStore)

        forgotPass = binding.forgotPassTxt
        loginEmpId = binding.emailEt
        loginPassword = binding.passwordEt
        loginSubmit = binding.signBtn!!

        forgotPass.setOnClickListener {
             /*val action = LoginFragmentDirections.actionLoginFragmentToForgetPasswordFragment()
             view.findNavController().navigate(action)*/

            getLoginData()

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
                            val distributor_id = data.data.distributor_id
                            val email = data.data.email
                            val phone_number = data.data.phone_number
                            val role = data.data.role


                            loginPrefrence.saveUserLoginDetail(
                                empId = emp_id,
                                distributor_id = distributor_id,
                                email = email,
                                phone = phone_number,
                                role = role
                            )

                            Toast.makeText(context, data?.message, Toast.LENGTH_SHORT).show()
                            val action =
                                LoginFragmentDirections.actionLoginFragmentToSelfieFragment()
                            view.findNavController().navigate(action)


                        } else {
                            Toast.makeText(context, data?.message, Toast.LENGTH_SHORT).show()

                        }

                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Response Code ${response.code()} Response Message : ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                    // Show API error. This is the error raised by the client.

                    Toast.makeText(
                        requireContext(),
                        "Exception Occurred: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }


            }
        }

    }


    suspend private fun executiveLogin(empId: String, pwd: String): Response<LoginModel> {
        return withContext(Dispatchers.IO) {

            BaseClient.getInstance.executiveLogin(empId, pwd)
        }
    }

    private fun getLoginData() {
        loginPrefrence.empIdFlow.asLiveData().observe(viewLifecycleOwner, { empId ->
            Toast.makeText(requireContext(), "EmpId: $empId", Toast.LENGTH_SHORT).show()
        })
    }


}