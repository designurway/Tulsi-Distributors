package com.tulsidistributors.tdemployee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.databinding.AdminBrandItemListBinding
import com.tulsidistributors.tdemployee.model.admin_brand.AdminBrandData

class AdminBrandAdapter(
    val brandList: ArrayList<AdminBrandData>,
    val listner: OnAdminBrandItemClicked
) :
    RecyclerView.Adapter<AdminBrandAdapter.AdminBrand_VH>() {


    class AdminBrand_VH(binding: AdminBrandItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val adminBrandLogo = binding.adminBrandLogo
        val adminBrandName = binding.adminBrandName
        val adminItemLayout = binding.adminItemLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminBrand_VH {
        val binding =
            AdminBrandItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminBrand_VH(binding)
    }

    override fun onBindViewHolder(holder: AdminBrand_VH, position: Int) {
        holder.adminBrandName.text = brandList.get(position).brand_name

        holder.adminItemLayout.setOnClickListener {
            listner.BrandItemClicked(position.toString(), brandList.get(position).brand_name)
        }
    }

    override fun getItemCount(): Int {
        return brandList.size
    }
}

interface OnAdminBrandItemClicked {
    fun BrandItemClicked(postion: String, brandName: String)

}