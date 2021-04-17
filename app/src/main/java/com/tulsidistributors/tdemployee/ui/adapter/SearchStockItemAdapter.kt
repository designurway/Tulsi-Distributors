package com.tulsidistributors.tdemployee.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tulsidistributors.tdemployee.databinding.SearchStockItemBinding
import com.tulsidistributors.tdemployee.model.search_stock.SearchStockItemData
import com.tulsidistributors.tdemployee.model.search_stock.SearchStockItemModel
import com.tulsidistributors.tdemployee.utils.showToast
import java.util.zip.Inflater

class SearchStockItemAdapter(val stockItem:ArrayList<SearchStockItemData>,val listner: OnAddItemClickListner,val from:String):RecyclerView.Adapter<SearchStockItemAdapter.SearchStock_VH>() {

    class SearchStock_VH(binding:SearchStockItemBinding):RecyclerView.ViewHolder(binding.root){
        val staockProductName = binding.staockProductName
        val stockProductDes = binding.stockProductDes
        val stockQty = binding.stockQty
        val addItem = binding.addItem
        val alreadyAddedBtn = binding.alreadyAdded
        val stockAvail = binding.stockAvail
        val addBtnLayout = binding.addBtnLayout
        val searchStockImg = binding.searchStockImg
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchStock_VH {
        val binding = SearchStockItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchStock_VH(binding)
    }

    override fun onBindViewHolder(holder: SearchStock_VH, position: Int) {
        holder.staockProductName.text = stockItem.get(position).product_name
        holder.stockProductDes.text = stockItem.get(position).description
        holder.stockQty.text = "Qty : ${stockItem.get(position).quantity}"
        holder.stockAvail.text = stockItem.get(position).product_status

        Glide.with(holder.stockAvail).load(stockItem[position].product_image).into(holder.searchStockImg)

//        showToast(holder.addBtnLayout.context,"From => $from")

        if (from.equals("add_product_list")){
            holder.addBtnLayout.visibility = View.VISIBLE
        }else{
            holder.addBtnLayout.visibility = View.GONE
        }

        val productId = stockItem.get(position).product_id

        holder.addItem.setOnClickListener {
            listner.onAddBtnClicked(position,productId = productId,addItemBtn = holder.addItem,holder.alreadyAddedBtn)
        }
    }

    override fun getItemCount(): Int {
        return stockItem.size
    }
}

interface OnAddItemClickListner{
    fun onAddBtnClicked(positon:Int,productId:String,addItemBtn:Button,alreadyAddedBtn:Button)
}