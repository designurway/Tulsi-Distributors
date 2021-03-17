package com.tulsidistributors.tdemployee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.databinding.SearchStockItemBinding
import com.tulsidistributors.tdemployee.model.search_stock.SearchStockItemData
import com.tulsidistributors.tdemployee.model.search_stock.SearchStockItemModel
import java.util.zip.Inflater

class SearchStockItemAdapter(val stockItem:ArrayList<SearchStockItemData>):RecyclerView.Adapter<SearchStockItemAdapter.SearchStock_VH>() {

    class SearchStock_VH(binding:SearchStockItemBinding):RecyclerView.ViewHolder(binding.root){
        val staockProductName = binding.staockProductName
        val stockProductDes = binding.stockProductDes
        val stockProductPrice = binding.stockProductPrice
        val stockAvail = binding.stockAvail
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchStock_VH {
        val binding = SearchStockItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchStock_VH(binding)
    }

    override fun onBindViewHolder(holder: SearchStock_VH, position: Int) {
        holder.staockProductName.text = stockItem.get(position).product_name
        holder.stockProductDes.text = stockItem.get(position).product_desc
        holder.stockProductPrice.text = "Rs ${stockItem.get(position).total_amount}"
        holder.stockAvail.text = stockItem.get(position).status
    }

    override fun getItemCount(): Int {
        return stockItem.size
    }
}