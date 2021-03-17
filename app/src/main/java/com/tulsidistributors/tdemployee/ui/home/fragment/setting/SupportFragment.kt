package com.tulsidistributors.tdemployee.ui.home.fragment.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentSupportBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.StatusMessageModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class SupportFragment : Fragment() {

    lateinit var binding: FragmentSupportBinding
    lateinit var empId: EditText
    lateinit var empQuery: EditText
    lateinit var submitQuery: Button

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

        empId=binding.employeeIdEt
        empQuery=binding.descriptionEt
        submitQuery=binding.sendBtn

        submitQuery.setOnClickListener {
            val id=empId.text.toString().trim()
            val query=empQuery.text.toString().trim()

            if (id.isEmpty()){
                empId.error="Required fields"
                empId.requestFocus()
                return@setOnClickListener
            }

            else if(query.isEmpty()){
                empQuery.error="Required fields"
                empQuery.requestFocus()
                return@setOnClickListener
            }
            else{


                CoroutineScope(Dispatchers.Main).launch {
                    try {

                        val response=postQuery(id,query)
                        if(response.isSuccessful){
                            Toast.makeText(requireContext(), response.body()!!.message, Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(requireContext(), response.body()!!.message, Toast.LENGTH_SHORT).show()

                        }

                    }catch (e:Exception){
                        Toast.makeText(requireContext(), "Exeption Occured ${e.message}", Toast.LENGTH_SHORT).show()

                    }
                }
            }

        }


    }

    suspend private fun postQuery(empId:String,message:String): Response<StatusMessageModel> {
        return withContext(Dispatchers.IO){
            BaseClient.getInstance.postQuery(empId, message)
        }
    }


}