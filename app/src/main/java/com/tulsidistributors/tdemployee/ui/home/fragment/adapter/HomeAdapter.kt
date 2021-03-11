package com.tulsidistributors.tdemployee.ui.home.fragment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.ui.home.model.CategoryModel


class HomeAdapter(val categoryList: ArrayList<CategoryModel>) :
    RecyclerView.Adapter<HomeAdapter.Home_VH>() {


    class Home_VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var image: ImageView = itemView.findViewById(R.id.img)
         var name: TextView? = itemView.findViewById(R.id.name_tv)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Home_VH {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_category,
            parent,
            false
        )
        return Home_VH(view)
    }

    override fun onBindViewHolder(holder: Home_VH, position: Int) {
            holder.name?.text = categoryList.get(position).name
            holder.image.setImageDrawable(categoryList.get(position).image)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}