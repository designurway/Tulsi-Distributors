package com.tulsidistributors.tdemployee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.databinding.AddProductItemsBinding
import com.tulsidistributors.tdemployee.databinding.FragmentAddProductListBinding
import com.tulsidistributors.tdemployee.model.get_admin_product.DealerProductData

class AddProductAdapter(val productItem: ArrayList<DealerProductData>) :
    RecyclerView.Adapter<AddProductAdapter.AddProduct_VH>() {

    class AddProduct_VH(binding: AddProductItemsBinding) : RecyclerView.ViewHolder(binding.root) {

        val brandName = binding.brandName
        val productDrescription = binding.productDes
        val productPrice = binding.productPrice

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProduct_VH {
        val binding =
            AddProductItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddProduct_VH(binding)
    }

    override fun onBindViewHolder(holder: AddProduct_VH, position: Int) {
        holder.brandName.text = productItem.get(position).product_name
        holder.productDrescription.text = productItem.get(position).product_desc
        holder.productPrice.text = "Rs ${productItem.get(position).basic_amount}"
    }

    override fun getItemCount(): Int {
        return productItem.size
    }
}