package com.tulsidistributors.tdemployee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tulsidistributors.tdemployee.databinding.PlacedOrderListBinding
import com.tulsidistributors.tdemployee.model.placed_order_list.PlacedOrderListData

class PlaceOrderListAdapter(val placedOrderList:ArrayList<PlacedOrderListData>):RecyclerView.Adapter<PlaceOrderListAdapter.PlaceOrder_VH>() {

    class PlaceOrder_VH(binding:PlacedOrderListBinding) :RecyclerView.ViewHolder(binding.root){
        val titleTv  = binding.titleTv
        val descriptionTv  = binding.descriptionTv
        val priceNumTv  = binding.priceNumTv
        val quantityNumTv  = binding.quantityNumTv
        val orderPlaceDate  = binding.orderPlaceDate
        val placeOrderImg  = binding.placeOrderImg
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceOrder_VH {
        val binding = PlacedOrderListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PlaceOrder_VH(binding)
    }

    override fun onBindViewHolder(holder: PlaceOrder_VH, position: Int) {
        holder.titleTv.text = placedOrderList.get(position).product_name
        holder.descriptionTv.text = placedOrderList.get(position).description
        holder.priceNumTv.text = placedOrderList.get(position).product_price
        holder.quantityNumTv.text = placedOrderList.get(position).order_quantity
        holder.orderPlaceDate.text = placedOrderList.get(position).purchase_date

        Glide.with(holder.quantityNumTv).load(placedOrderList[position].product_image).into(holder.placeOrderImg)
    }

    override fun getItemCount(): Int {
        return placedOrderList.size
    }


}