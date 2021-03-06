package com.tulsidistributors.tdemployee.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.ui.home.fragment.models.NotificationModel

class NotificationAdapter(val notificationList:ArrayList<NotificationModel>): RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var title:TextView = itemView.findViewById(R.id.title_tv)
        var message:TextView=itemView.findViewById(R.id.description_tv)
        var date:TextView=itemView.findViewById(R.id.date_tv)
        var notificationImg:ImageView=itemView.findViewById(R.id.notificationImg)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.list_notifications,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.text=notificationList.get(position).title
        holder.message.text=notificationList.get(position).body
        holder.date.text=notificationList.get(position).created_date

        Glide.with(holder.message).load(notificationList[position].profile).into(holder.notificationImg)


    }

    override fun getItemCount(): Int {
        return notificationList.size
    }
}