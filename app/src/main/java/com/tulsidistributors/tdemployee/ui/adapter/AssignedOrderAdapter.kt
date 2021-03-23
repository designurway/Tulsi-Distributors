package com.tulsidistributors.tdemployee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.databinding.AssigneOrderItemListBinding
import com.tulsidistributors.tdemployee.model.assign_order.AssignedOrderData
import kotlinx.coroutines.CoroutineScope

class AssignedOrderAdapter(
    val orderList: ArrayList<AssignedOrderData>,
    val listner: AssignedOrderClicked
) :
    RecyclerView.Adapter<AssignedOrderAdapter.AOrder_VH>() {

    class AOrder_VH(binding: AssigneOrderItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        val shopName = binding.shopName
        val shopAddress = binding.shopAddress
        val shopImg = binding.shopImg
        val assignedOrderTime = binding.assignedOrderTime
        val pendingOrderClicked = binding.pendingOrderClicked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AOrder_VH {
        val orderBinding: AssigneOrderItemListBinding = AssigneOrderItemListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AOrder_VH(orderBinding)
    }

    override fun onBindViewHolder(holder: AOrder_VH, position: Int) {
        holder.shopName.text = orderList.get(position).shop_name
        holder.shopAddress.text = orderList.get(position).address
        holder.assignedOrderTime.text = orderList.get(position).date

        holder.pendingOrderClicked.setOnClickListener {
            val dealer_id = orderList.get(position).dealer_id
            val shop_address = orderList.get(position).address
            listner.onItemClicked(
                position,
                shopName = orderList.get(position).shop_name,
                dealer_id = dealer_id,
                shop_address = shop_address
            )
        }

    }

    override fun getItemCount(): Int {
        return orderList.size
    }


}

interface AssignedOrderClicked {
    fun onItemClicked(postion: Int, shopName: String, dealer_id: String, shop_address: String)
}

