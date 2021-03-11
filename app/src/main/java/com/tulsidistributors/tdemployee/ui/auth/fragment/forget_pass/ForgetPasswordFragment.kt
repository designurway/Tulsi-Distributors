package com.tulsidistributors.tdemployee.ui.auth.fragment.forget_pass

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentForgetPasswordBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.StatusMessageModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ForgetPasswordFragment : Fragment() {

    lateinit var binding: FragmentForgetPasswordBinding
    lateinit var empPhone: EditText
    lateinit var generateBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        empPhone = binding.newPassEmailEt
        generateBtn = binding.sendOtp

        binding.sendOtp.setOnClickListener {

            val phone = empPhone.text.toString().trim()

            if (phone.isEmpty()) {
                empPhone.error = "Required field"
                empPhone.requestFocus()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.Main).launch {
                val response = generateOtp(phone)
                if (response.status.equals("1")) {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                    val action =
                        ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToVerifyForgotPassFragment(phone)
                    view.findNavController().navigate(action)
                } else {

                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()

                }
            }


        }


    }

    suspend private fun generateOtp(phone: String): StatusMessageModel {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.generateOtp(phone).body()!!
        }
    }
}