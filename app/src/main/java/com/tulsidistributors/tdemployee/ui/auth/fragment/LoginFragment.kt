package com.tulsidistributors.tdemployee.ui.auth.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentLoginBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.StatusMessageModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception


class LoginFragment : Fragment() {

    lateinit var binding:FragmentLoginBinding
    lateinit var forgotPass: TextView
    lateinit var loginEmpId: EditText
    lateinit var loginPassword: EditText
    lateinit var loginSubmit: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forgotPass = binding.forgotPassTxt
        loginEmpId = binding.emailEt
        loginPassword = binding.passwordEt
        loginSubmit = binding.signBtn!!

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
                    val response = executiveLogin(empId,pwd)

                    if (response.isSuccessful){
                        val data = response.body()

                        if(data?.status.equals("1")){

                            Toast.makeText(context, data?.message, Toast.LENGTH_SHORT).show()
                            val action=LoginFragmentDirections.actionLoginFragmentToSelfieFragment()
                            view.findNavController().navigate(action)


                        }else{
                            Toast.makeText(context, data?.message, Toast.LENGTH_SHORT).show()

                        }
                        
                    }else{
                        Toast.makeText(requireContext(), "Response Code ${response.code()} Response Message : ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }catch (e:Exception){
                    // Show API error. This is the error raised by the client.

                    Toast.makeText(requireContext(),
                        "Exception Occurred: ${e.message}",
                        Toast.LENGTH_LONG).show()
                }

               /* val response = executiveLogin(empId, pwd)

                if (response.isSuccessful)

                if(response.status.equals("1")){

                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                    val action=LoginFragmentDirections.actionLoginFragmentToSelfieFragment()
                    view.findNavController().navigate(action)


                }else{
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()

                }*/
            }
        }



    }

    suspend private fun executiveLogin(empId: String, pwd: String): Response<StatusMessageModel> {
        return withContext(Dispatchers.IO) {

            BaseClient.getInstance.executiveLogin(empId, pwd)
        }
    }
}