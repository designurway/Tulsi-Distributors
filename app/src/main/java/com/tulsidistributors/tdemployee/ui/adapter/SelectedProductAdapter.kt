package com.designurway.tdapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.designurway.tdapplication.model.SelectedProductModel
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.ListSelectedBinding

data class SelectedProductAdapter(
    var selectedProductModel: ArrayList<SelectedProductModel>,
    var context: Context,


    ) : RecyclerView.Adapter<SelectedProductAdapter.ViewHolder>() {

    var total = 0

    class ViewHolder(binding: ListSelectedBinding) : RecyclerView.ViewHolder(binding.root) {

        val proName = binding.nameTv
        val proQnty = binding.qntyTvItem
        val proPrice = binding.priceTv


    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectedProductAdapter.ViewHolder {

        val binding =
            ListSelectedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

        /* val view = LayoutInflater.from(parent.context).inflate(R.layout.list_selected, parent, false)
            return SelectedProductAdapter.ViewHolder(view)*/
    }

    override fun getItemCount(): Int {
        return selectedProductModel.size
    }

    override fun onBindViewHolder(holder: SelectedProductAdapter.ViewHolder, position: Int) {
        holder.proName.text = selectedProductModel.get(position).proName
        holder.proQnty.text = selectedProductModel.get(position).proQnty
        holder.proPrice.text = selectedProductModel.get(position).proPrice


        total = selectedProductModel.get(position).proPrice.toInt() + total


    }

}
