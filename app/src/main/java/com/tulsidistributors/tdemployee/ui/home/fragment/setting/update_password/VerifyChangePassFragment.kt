package com.tulsidistributors.tdemployee.ui.home.fragment.setting.update_password

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentCreateNewPasswordBinding
import com.tulsidistributors.tdemployee.databinding.FragmentVerifyChangePassBinding
import com.tulsidistributors.tdemployee.databinding.FragmentVerifyForgotPassBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.StatusMessageModel
import com.tulsidistributors.tdemployee.ui.auth.fragment.forget_pass.VerifyForgotPassFragmentArgs
import com.tulsidistributors.tdemployee.ui.auth.fragment.forget_pass.VerifyForgotPassFragmentDirections
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class VerifyChangePassFragment : Fragment() {

    lateinit var binding: FragmentVerifyChangePassBinding
    lateinit var otpEt1: EditText
    lateinit var otpEt2: EditText
    lateinit var otpEt3: EditText
    lateinit var otpEt4: EditText
    lateinit var mContext: Context

    private val args: VerifyChangePassFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVerifyChangePassBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()

        val phone = args.phone

        otpEt1 = binding.otpEt1
        otpEt2 = binding.otpEt2
        otpEt3 = binding.otpEt3
        otpEt4 = binding.otpEt4



        binding.verifyOtp.setOnClickListener {

            val otp_one = otpEt1.text.toString().trim()
            val otp_two = otpEt2.text.toString().trim()
            val otp_three = otpEt3.text.toString().trim()
            val otp_four = otpEt4.text.toString().trim()

            val otp = otp_one + otp_two + otp_three + otp_four

            if (otp_one.isEmpty()) {
                otpEt1.error = "Required fields"
                otpEt1.requestFocus()
                return@setOnClickListener

            } else
                if (otp_two.isEmpty()) {
                    otpEt2.error = "Required fields"
                    otpEt2.requestFocus()
                    return@setOnClickListener
                } else
                    if (otp_three.isEmpty()) {
                        otpEt3.error = "Required fields"
                        otpEt3.requestFocus()
                        return@setOnClickListener
                    } else
                        if (otp_four.isEmpty()) {
                            otpEt4.error = "Required fields"
                            otpEt4.requestFocus()
                            return@setOnClickListener
                        }

            CoroutineScope(Dispatchers.Main).launch {

                val response = verifyPhoneOtp(phone, otp)

                if (response.status.equals("1")) {
                    showToast(mContext, "On Sucess ${response.message}")
                    val actions =
                        VerifyChangePassFragmentDirections.actionVerifyChangePassFragmentToChangeNewPassFragment(phone)
                    view.findNavController().navigate(actions)
                } else {
                    showToast(mContext, "not  Sucess ${response.message}")


                }
            }
        }

    }

    suspend private fun verifyPhoneOtp(phone: String, otp: String): StatusMessageModel {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.verifyPhoneOtp(phone, otp).body()!!
        }
    }

}