package com.tulsidistributors.tdemployee.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.ListCategoryBinding
import com.tulsidistributors.tdemployee.model.home.CategoryModel


class HomeAdapter(val categoryList: ArrayList<CategoryModel>,val listner:HomeItemClicked) :
    RecyclerView.Adapter<HomeAdapter.Home_VH>() {


    class Home_VH(binding: ListCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
         var image = binding.img
         var name = binding.nameTv
         var cv = binding.cv



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Home_VH {
        val view = ListCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Home_VH(view)
    }

    override fun onBindViewHolder(holder: Home_VH, position: Int) {
            holder.name?.text = categoryList.get(position).name
            holder.image.setImageDrawable(categoryList.get(position).image)

        holder.cv.setOnClickListener {
            listner.homeItemClickedListner(categoryList.get(position).name)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}


interface HomeItemClicked{
    fun homeItemClickedListner( name:String)
}