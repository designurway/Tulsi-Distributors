package com.tulsidistributors.tdemployee.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.databinding.AddProductItemsBinding
import com.tulsidistributors.tdemployee.databinding.FragmentAddProductListBinding
import com.tulsidistributors.tdemployee.model.get_admin_product.DealerProductData

class AddProductAdapter(
    val productItem: ArrayList<DealerProductData>,
    val listner: AddProductItemClickListner
) :
    RecyclerView.Adapter<AddProductAdapter.AddProduct_VH>() {

    lateinit var binding: AddProductItemsBinding

    class AddProduct_VH(binding: AddProductItemsBinding) : RecyclerView.ViewHolder(binding.root) {

        val brandName = binding.brandName
        val productDrescription = binding.productDes
        val productPrice = binding.productPrice
        val addProductBtn = binding.addProductBtn
        val minusBtn = binding.minusBtn
        val plusBtn = binding.plusBtn
        val productQtyTxt = binding.productQtyTv
        val constriantQuantity = binding.constriantQuantity
        val oldQnty = binding.oldQnty

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddProduct_VH {
        binding =
            AddProductItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddProduct_VH(binding)
    }

    override fun onBindViewHolder(holder: AddProduct_VH, position: Int) {

        val productName = productItem.get(position).product_name
        val productPrice = productItem.get(position).basic_amount
        val brandId = productItem.get(position).brand_id
        val productId = productItem.get(position).product_id

        holder.addProductBtn.text = "ADD"

        holder.brandName.text = productName
        holder.productDrescription.text = productItem.get(position).description
        holder.productPrice.text = "Rs ${productPrice}"

        var productQty = holder.productQtyTxt.text.toString().toInt()



        holder.minusBtn.setOnClickListener {
            if (productQty > 1) {
                productQty--
                listner.minusButtonClicked(position = position, productQty, holder.productQtyTxt)

            }

        }

        holder.plusBtn.setOnClickListener {
            productQty++
            listner.plusButtonClicked(position = position, productQty, holder.productQtyTxt)
        }

        holder.constriantQuantity.setOnClickListener {

            listner.openDialogBox(holder.oldQnty, position)
        }

        holder.addProductBtn.setOnClickListener {
            listner.onAddButtonClicked(
                holder.addProductBtn,
                position = position,
                prodactName = productName,
                prodctQuantity = productQty,
                productPice = productPrice,
                brandId = brandId,
                product_id = productId
            )
        }
    }

    override fun getItemCount(): Int {
        return productItem.size
    }

}

interface AddProductItemClickListner {
    fun plusButtonClicked(position: Int, productQty: Int, productQtyTxt: TextView)
    fun minusButtonClicked(position: Int, productQty: Int, productQtyTxt: TextView)
    fun openDialogBox(oldQnty: TextView, position: Int)
    fun onAddButtonClicked(
        addBtn: Button,
        position: Int,
        prodactName: String,
        prodctQuantity: Int,
        productPice: String,
        brandId: String,
        product_id:String
    )
}