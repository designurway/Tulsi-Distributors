package com.tulsidistributors.tdemployee.ui.home.fragment.assigned_orders

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentAddPaymentBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.StatusMessageModel
import com.tulsidistributors.tdemployee.utils.Common
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.temporal.TemporalAmount


class AddPaymentFragment : Fragment() {
    var spinner: Spinner? = null
    lateinit var binding:FragmentAddPaymentBinding
    lateinit var mContext:Context
    val args:AddPaymentFragmentArgs by navArgs()
     var totalAmount: Int = 0
    lateinit var paymentMode:String
    lateinit var referenceNo:String
    lateinit var dealerId:String
    lateinit var pendingAmount: String
    lateinit var advanceAmount:String
    lateinit var remark:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAddPaymentBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()

        totalAmount = args.totalAmount
        referenceNo = args.refrenceNo
        dealerId = args.dealerId

        binding.totalAmountET.setText(totalAmount.toString())

        spinner = binding.paymentType

        val arrayList = ArrayList<String>()
        arrayList.add("Payment Type")
        arrayList.add("Cash")
        arrayList.add("Card")
        arrayList.add("UPI")
        arrayList.add("NEFT")
        arrayList.add("DD")
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, arrayList)

        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner!!.adapter = arrayAdapter
        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                paymentMode = adapterView.getItemAtPosition(i).toString()
                Toast.makeText(requireContext(), "Selected: $paymentMode", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.submitPaymentBtn.setOnClickListener {

            advanceAmount = binding.advanceAmount.text.toString().trim()

            pendingAmount = (totalAmount - advanceAmount.toInt()).toString()
            remark = binding.remarkTv.text.toString()

            addPaymentDetails()
        }
    }

    private fun addPaymentDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = addPaymentDetailsApiCall()
                if (response.isSuccessful){
                   val action = AddPaymentFragmentDirections.actionAddPaymentFragmentFragmentToOrderPlacedSucessfullyFragment()
                    findNavController().navigate(action)
                }else{
                    showToast(mContext,"Response ${response.message()}")
                }
            }catch (e:Exception){
                showToast(mContext,"Exception ${e.message}")
            }
        }
    }

    private suspend fun addPaymentDetailsApiCall():Response<StatusMessageModel> {
        return withContext(Dispatchers.IO){
            BaseClient.getInstance.addPaymentDetails(
                reference_no = referenceNo,
                dealer_id = dealerId,
                pending_amount = pendingAmount,
                advance_amount = advanceAmount,
                paymentMode,
                remarks = remark,
                Common().getCurrentDate("yyyy-MM-dd"),
                total_amount = totalAmount.toString()
            )
        }
    }


}