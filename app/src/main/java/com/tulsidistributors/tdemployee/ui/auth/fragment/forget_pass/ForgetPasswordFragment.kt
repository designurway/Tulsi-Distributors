package com.tulsidistributors.tdemployee.ui.auth.fragment.forget_pass

import android.content.Context
import android.os.Bundle
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentForgetPasswordBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.StatusMessageModel
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class ForgetPasswordFragment : Fragment() {

    lateinit var binding: FragmentForgetPasswordBinding
    lateinit var empPhone: EditText
    lateinit var generateBtn: Button
    lateinit var mContext: Context

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

        mContext = requireContext()

        empPhone = binding.newPassEmailEt
        generateBtn = binding.sendOtp

        binding.sendOtp.setOnClickListener {

            val phone = empPhone.text.toString().trim()

            if (phone.isEmpty()) {
                empPhone.error = "Required field"
                empPhone.requestFocus()
                return@setOnClickListener
            }


            viewLifecycleOwner.lifecycleScope.launch {

                try {
                    val response = generateOtp(phone)
                    val responseData = response.body()
                    if (response.isSuccessful){
                        if (responseData?.status.equals("1")){

                            showToast(mContext,"${responseData?.message}")

                            val action =ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToVerifyForgotPassFragment(
                                phone
                            )
                            view.findNavController().navigate(action)
                        }else{
                            showToast(mContext,"On Fa ${responseData?.message}")

                        }
                    }else{
                        showToast(mContext,"Response Code ${response.code()} Response Message ${response.message()}")
                    }

                } catch (e: Exception) {

                    showToast(mContext,"Exception Occured ${e.message}")
                }

            }

        }


    }

    suspend private fun generateOtp(phone: String): Response<StatusMessageModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.generateOtp(phone)
        }
    }
}