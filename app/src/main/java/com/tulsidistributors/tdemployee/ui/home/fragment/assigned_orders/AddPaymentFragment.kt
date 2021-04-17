package com.tulsidistributors.tdemployee.ui.home.fragment.assigned_orders

import android.app.DatePickerDialog
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
import java.util.*
import kotlin.collections.ArrayList


class AddPaymentFragment : Fragment() {
    var spinner: Spinner? = null
    lateinit var binding:FragmentAddPaymentBinding
    lateinit var mContext:Context
    val args:AddPaymentFragmentArgs by navArgs()
     var totalAmount: Int = 0
    lateinit var paymentMode:String
    lateinit var referenceNo:String
    lateinit var selectedDate: String
    lateinit var dealerId:String
    lateinit var pendingAmount: String
    lateinit var advanceAmount:String
    lateinit var remark:String
    lateinit var datePickerDialog: DatePickerDialog

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


        binding.selectDueDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]




            datePickerDialog = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, Year, Month, dayOfMonth ->

                    //Date format
                    if (Month < 10 && dayOfMonth < 10) {
                        selectedDate = "$Year-0${(Month + 1)}-0$dayOfMonth"
                    } else if (Month > 10 && dayOfMonth < 10) {
                        selectedDate = "$Year-${(Month + 1)}-0$dayOfMonth"
                    } else if (Month < 10 && dayOfMonth > 10) {
                        selectedDate = "$Year-0${(Month + 1)}-$dayOfMonth"
                    } else {
                        selectedDate = "$Year-${(Month + 1)}-$dayOfMonth"
                    }


                    binding.selectDueDate.text = dayOfMonth.toString() + "/" + (Month + 1) + "/" + Year



                    showToast(mContext, "selected date $selectedDate")

                }, year, month, day
            )
            datePickerDialog.show()


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
                total_amount = totalAmount.toString(),
                due_date = selectedDate
            )
        }
    }


}