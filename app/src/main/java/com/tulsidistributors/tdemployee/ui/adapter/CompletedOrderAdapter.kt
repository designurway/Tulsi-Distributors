package com.tulsidistributors.tdemployee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.databinding.CompletedTaskListItemBinding
import com.tulsidistributors.tdemployee.model.completed_order.CompletedOrderData

class CompletedOrderAdapter(val orderList: ArrayList<CompletedOrderData>) :
    RecyclerView.Adapter<CompletedOrderAdapter.COrder_VH>() {


    class COrder_VH(binding: CompletedTaskListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val shopName = binding.shopName
        val completedAddress = binding.completedAddress
        val completedDate = binding.completedDate
        val completedTime = binding.completedTime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): COrder_VH {
        val binding =
            CompletedTaskListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return COrder_VH(binding)
    }

    override fun onBindViewHolder(holder: COrder_VH, position: Int) {
       holder.shopName.text = orderList.get(position).shop_name
        holder.completedAddress.text = orderList.get(position).address
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}