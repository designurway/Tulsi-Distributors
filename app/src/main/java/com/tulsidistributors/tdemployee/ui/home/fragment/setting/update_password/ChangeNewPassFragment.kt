package com.tulsidistributors.tdemployee.ui.home.fragment.setting.update_password

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
import com.tulsidistributors.tdemployee.databinding.ActivityAuthBinding.inflate
import com.tulsidistributors.tdemployee.databinding.FragmentChangeNewPassBinding
import com.tulsidistributors.tdemployee.databinding.FragmentVerifyChangePassBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.StatusMessageModel
import com.tulsidistributors.tdemployee.ui.auth.fragment.forget_pass.CreateNewPasswordFragmentArgs
import com.tulsidistributors.tdemployee.ui.auth.fragment.forget_pass.CreateNewPasswordFragmentDirections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangeNewPassFragment : Fragment() {

    lateinit var binding: FragmentChangeNewPassBinding

    private val args: ChangeNewPassFragmentArgs by navArgs()
    lateinit var passwordEt: EditText
    lateinit var confirmpwdEt: EditText
    lateinit var changePwdBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChangeNewPassBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val phone = args.phone
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
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }

            Toast.makeText(requireContext(), "Phone $phone Password $password", Toast.LENGTH_SHORT).show()

            CoroutineScope(Dispatchers.Main).launch {
                val response = updateNewPassword(phone, password)


                if (response.status.equals("1")) {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                    val action =
                        ChangeNewPassFragmentDirections.actionChangeNewPassFragmentToProfileFragment()
                    view.findNavController().navigate(action)

                } else {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()

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