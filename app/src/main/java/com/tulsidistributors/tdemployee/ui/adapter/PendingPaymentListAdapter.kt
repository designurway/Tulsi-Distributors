package com.tulsidistributors.tdemployee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.databinding.PendingPaymentListItemBinding
import com.tulsidistributors.tdemployee.model.pending_payment_list.PendingPaymentListData

class PendingPaymentListAdapter(
    val itemList: ArrayList<PendingPaymentListData>,
    val listner: OnPendingListClicked
) :
    RecyclerView.Adapter<PendingPaymentListAdapter.PendingPayment_VH>() {

    class PendingPayment_VH(val binding: PendingPaymentListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val refrenceId = binding.refrenceId
        val pendingAmount = binding.pendingAmount
        val purchaseDate = binding.purchaseDate
        val pendingListLayout = binding.pendingListLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingPayment_VH {
        val binding = PendingPaymentListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PendingPayment_VH(binding)
    }

    override fun onBindViewHolder(holder: PendingPayment_VH, position: Int) {
        holder.pendingAmount.text = "Rs ${itemList[position].pending_amount}"
        holder.refrenceId.text = itemList[position].reference_id
        holder.purchaseDate.text = itemList[position].purchase_date

        val pendingAmnt = itemList[position].pending_amount
        val totalAmnt = itemList[position].total_amount
        val advancAmnt = itemList[position].advance_amount
        val refrenceId = itemList[position].reference_id

        holder.pendingListLayout.setOnClickListener {
            listner.OnPendingItemClicked(
                position = position,
                advanceAmount = advancAmnt,
                totalAmnt = totalAmnt,
                pendingAmnt = pendingAmnt,
                refrenceId = refrenceId
            )
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


}

interface OnPendingListClicked {
    fun OnPendingItemClicked(
        position: Int,
        totalAmnt: String,
        advanceAmount: String,
        pendingAmnt: String,
        refrenceId:String
    )
}