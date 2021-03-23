package com.tulsidistributors.tdemployee.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.databinding.AddProductItemsBinding
import com.tulsidistributors.tdemployee.databinding.FragmentAddProductListBinding
import com.tulsidistributors.tdemployee.model.get_admin_product.DealerProductData

class AddProductAdapter(val productItem: ArrayList<DealerProductData>,val listner:AddProductItemClickListner) :
    RecyclerView.Adapter<AddProductAdapter.AddProduct_VH>() {

    lateinit var binding:AddProductItemsBinding

    class AddProduct_VH(binding: AddProductItemsBinding) : RecyclerView.ViewHolder(binding.root) {

        val brandName = binding.brandName
        val productDrescription = binding.productDes
        val productPrice = binding.productPrice
        val addProductBtn = binding.addProductBtn
        val minusBtn = binding.minusBtn
        val plusBtn = binding.plusBtn
        val productQtyTxt = binding.productQtyTv

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProduct_VH {
         binding =
            AddProductItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddProduct_VH(binding)
    }

    override fun onBindViewHolder(holder: AddProduct_VH, position: Int) {
        holder.brandName.text = productItem.get(position).product_name
        holder.productDrescription.text = productItem.get(position).description
        holder.productPrice.text = "Rs ${productItem.get(position).basic_amount}"

        val productQty = holder.productQtyTxt.text.toString().trim()


        holder.addProductBtn.setOnClickListener {
            listner.addButtonClicked(position = position.toString(),productQty)
        }

        holder.minusBtn.setOnClickListener {
            listner.minusButtonClicked(position = position.toString(),productQty,holder.productQtyTxt)
        }

        holder.plusBtn.setOnClickListener {

            listner.plusButtonClicked(position = position.toString(),productQty,holder.productQtyTxt)
        }
    }

    override fun getItemCount(): Int {
        return productItem.size
    }

}

interface AddProductItemClickListner{
    fun addButtonClicked(position:String,productQty:String)
    fun plusButtonClicked(position:String,productQty:String,productQtyTxt: TextView)
    fun minusButtonClicked(position:String,productQty:String,productQtyTxt: TextView)

}