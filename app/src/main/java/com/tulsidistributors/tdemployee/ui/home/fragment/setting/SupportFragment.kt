package com.tulsidistributors.tdemployee.ui.home.fragment.setting

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import com.tulsidistributors.tdemployee.databinding.FragmentSupportBinding
import com.tulsidistributors.tdemployee.datastore.UserLoginPreferences
import com.tulsidistributors.tdemployee.datastore.dataStore
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.StatusMessageModel
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class SupportFragment : Fragment() {

    lateinit var binding: FragmentSupportBinding
    lateinit var empIdEt: EditText
    lateinit var empQuery: EditText
    lateinit var submitQuery: Button
    lateinit var mContext: Context
    lateinit var userLoginPreferences: UserLoginPreferences
    lateinit var EmpId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSupportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()

        userLoginPreferences = UserLoginPreferences(requireActivity().dataStore)

        getUserLoginDetail()

        empIdEt = binding.employeeIdEt
        empQuery = binding.descriptionEt
        submitQuery = binding.sendBtn

        submitQuery.setOnClickListener {
            val id = empIdEt.text.toString().trim()
            val query = empQuery.text.toString().trim()

            if (id.isEmpty()) {
                empIdEt.error = "Required fields"
                empIdEt.requestFocus()
                return@setOnClickListener
            } else if (query.isEmpty()) {
                empQuery.error = "Required fields"
                empQuery.requestFocus()
                return@setOnClickListener
            } else {


                CoroutineScope(Dispatchers.Main).launch {
                    try {

                        val response = postQuery(id, query)
                        if (response.isSuccessful) {

                            showToast(mContext, response.body()!!.message)
                            val action =
                                SupportFragmentDirections.actionSupportFragmentToHomeFragment()
                            requireView().findNavController()
                                .navigate(action)

                        } else {
                            showToast(mContext, response.body()!!.message)

                        }

                    } catch (e: Exception) {
                        showToast(mContext, "Exeption Occured ${e.message}")

                    }
                }
            }

        }


    }

    suspend private fun postQuery(empId: String, message: String): Response<StatusMessageModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.postQuery(empId, message)
        }
    }

    fun getUserLoginDetail() {
        userLoginPreferences.empIdFlow.asLiveData().observe(viewLifecycleOwner, {
            EmpId = it.toString()
            empIdEt.setText(it.toString())
        })
    }

}