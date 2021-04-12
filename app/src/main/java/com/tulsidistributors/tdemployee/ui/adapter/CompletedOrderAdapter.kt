package com.tulsidistributors.tdemployee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.databinding.CompletedTaskListItemBinding
import com.tulsidistributors.tdemployee.model.completed_order.CompletedOrderData

class CompletedOrderAdapter(val orderList: ArrayList<CompletedOrderData>,val listner:OnCompletedOrderClicked) :
    RecyclerView.Adapter<CompletedOrderAdapter.COrder_VH>() {


    class COrder_VH(binding: CompletedTaskListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val shopName = binding.shopName
        val completedAddress = binding.completedAddress
        val completedDate = binding.completedDate
        val orderLayout = binding.orderLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): COrder_VH {
        val binding =
            CompletedTaskListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return COrder_VH(binding)
    }

    override fun onBindViewHolder(holder: COrder_VH, position: Int) {
       holder.shopName.text = orderList.get(position).shop_name
        holder.completedAddress.text = orderList.get(position).address
        holder.completedDate.text = orderList.get(position).date
        holder.orderLayout.setOnClickListener {
            listner.OnOrderItemClicked(position,orderList.get(position).dealer_id,orderList.get(position).date)
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    interface OnCompletedOrderClicked{
        fun OnOrderItemClicked(position:Int,dealerId:String,date:String)
    }
}