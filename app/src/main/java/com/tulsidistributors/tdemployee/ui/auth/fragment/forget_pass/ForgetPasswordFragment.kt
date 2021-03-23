package com.tulsidistributors.tdemployee.ui.auth.fragment.forget_pass

import android.os.Bundle
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


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


            viewLifecycleOwner.lifecycleScope.launch {

                try {
                    val response = generateOtp(phone)
                    val responseData = response.body()
                    if (response.isSuccessful){
                        if (responseData?.status.equals("1")){
                            Toast.makeText(requireContext(), "${responseData?.message}", Toast.LENGTH_SHORT).show()
                            val action =ForgetPasswordFragmentDirections.actionForgetPasswordFragmentToVerifyForgotPassFragment(
                                phone
                            )
                            view.findNavController().navigate(action)
                        }else{
                            Toast.makeText(requireContext(), "On Fa ${responseData?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(requireContext(), "Response Code ${response.code()} Response Message ${response.message()}", Toast.LENGTH_SHORT).show()
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


    }

    suspend private fun generateOtp(phone: String): Response<StatusMessageModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.generateOtp(phone)
        }
    }
}