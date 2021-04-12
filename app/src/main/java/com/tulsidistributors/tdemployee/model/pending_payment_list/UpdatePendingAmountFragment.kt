package com.tulsidistributors.tdemployee.model.pending_payment_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentUpdatePendingAmountBinding
import com.tulsidistributors.tdemployee.json.BaseClient
import com.tulsidistributors.tdemployee.model.StatusMessageModel
import com.tulsidistributors.tdemployee.ui.home.fragment.assigned_orders.AddPaymentFragmentDirections
import com.tulsidistributors.tdemployee.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


class UpdatePendingAmountFragment : Fragment() {

    lateinit var binding: FragmentUpdatePendingAmountBinding
    lateinit var mContext: Context
    lateinit var pendingTv: TextView
    lateinit var advanceAmountEt: EditText
    lateinit var paymentType: Spinner
    lateinit var remarksEt: EditText
    lateinit var submitAmount: Button
    lateinit var paymentMode: String
    lateinit var previousPendingAmount: String
    lateinit var totalAmount: String
    lateinit var previousAdvanceAmount: String
    lateinit var refrenceId: String
    lateinit var dealerId: String
    lateinit var remarks: String
    lateinit var advanceAmount: String
    lateinit var pendingAmount: String

    val args: UpdatePendingAmountFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUpdatePendingAmountBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()

        previousPendingAmount = args.pendingAmount
        totalAmount = args.totalAmount
        previousAdvanceAmount = args.advanceAmount
        dealerId = args.dealerId
        refrenceId = args.refrenceId

        pendingTv = binding.pndAmt
        advanceAmountEt = binding.advanceAmount
        paymentType = binding.paymentType
        remarksEt = binding.remarkTv
        submitAmount = binding.submitPaymentBtn


        pendingTv.setText("Pending Amount Rs $previousPendingAmount")


        val arrayList = ArrayList<String>()
        arrayList.add("Payment Type")
        arrayList.add("Cash")
        arrayList.add("Card")
        arrayList.add("UPI")
        arrayList.add("NEFT")
        arrayList.add("DD")
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, arrayList)

        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        paymentType!!.adapter = arrayAdapter
        paymentType!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                paymentMode = adapterView.getItemAtPosition(i).toString()
                Toast.makeText(requireContext(), "Selected: $paymentMode", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        submitAmount.setOnClickListener {


            advanceAmount = advanceAmountEt.text.toString().trim()
            val paymentType = paymentType.selectedItem.toString().trim()
            remarks = remarksEt.text.toString().trim()

            pendingAmount = (previousPendingAmount.toInt() - advanceAmount.toInt()).toString()

            if (advanceAmount.isEmpty()) {
                advanceAmountEt.error = "Required field"
                advanceAmountEt.requestFocus()
                return@setOnClickListener
            }

            if (remarks.isEmpty()) {
                remarksEt.error = "Required field"
                remarksEt.requestFocus()
                return@setOnClickListener

            }

            updatePendingPaymentDetails()

        }

    }

    private fun updatePendingPaymentDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = updatePendingPaymentDetailsApiCall()
                if (response.isSuccessful) {
                    val action =
                        UpdatePendingAmountFragmentDirections.actionUpdatePendingAmountFragmentToOrderPlacedSucessfullyFragment()
                    findNavController().navigate(action)
                } else {
                    showToast(mContext, "Response ${response.message()}")
                }
            } catch (e: Exception) {
                showToast(mContext, "Exception ${e.message}")
            }
        }
    }


    private suspend fun updatePendingPaymentDetailsApiCall(): Response<StatusMessageModel> {
        return withContext(Dispatchers.IO) {
            BaseClient.getInstance.updatePendingPaymentDetails(
                refrenceId,
                dealer_id = dealerId,
                total_amount = totalAmount,
                pending_amount = pendingAmount,
                advance_amount = advanceAmount,
                payment_mode = paymentMode,
                remarks = remarks
            )
        }
    }

}
