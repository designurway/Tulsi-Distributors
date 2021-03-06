package com.tulsidistributors.tdemployee.ui.auth.fragment.forget_pass

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentCreateNewPasswordBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.StatusMessageModel
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CreateNewPasswordFragment : Fragment() {

    lateinit var binding: FragmentCreateNewPasswordBinding
    private val args: CreateNewPasswordFragmentArgs by navArgs()
    lateinit var passwordEt: EditText
    lateinit var confirmpwdEt: EditText
    lateinit var changePwdBtn: Button
    lateinit var mContext:Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateNewPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()

        val phone = args.phoneNumber
        passwordEt = binding.newPasswordEt
        confirmpwdEt = binding.confirmPasswordEt
        changePwdBtn = binding.confirmNewPassword


        changePwdBtn.setOnClickListener {
            val password = passwordEt.text.toString().trim()
            val confirmPwd = confirmpwdEt.text.toString().trim()

            if (password.isEmpty()) {
                passwordEt.error = "Required fields"
                passwordEt.requestFocus()
                return@setOnClickListener

            } else if (confirmPwd.isEmpty()) {
                confirmpwdEt.error = "Required field"
                confirmpwdEt.requestFocus()
                return@setOnClickListener
            } else if (!password.equals(confirmPwd)) {
                showToast(mContext,"Passwords do not match")
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.Main).launch {
                val response = updateNewPassword(phone, password)

                if (response.status.equals("1")) {
                    showToast(mContext,response.message)
                    val action =
                        CreateNewPasswordFragmentDirections.actionCreateNewPasswordFragmentToLoginFragment()
                    view.findNavController().navigate(action)

                } else {

                    showToast(mContext,response.message)

                }
            }
        }

    }

    suspend private fun updateNewPassword(phone: String, password: String): StatusMessageModel {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.updateNewPassword(phone, password).body()!!
        }
    }


}