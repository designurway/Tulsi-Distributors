package com.tulsidistributors.tdemployee.ui.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tulsidistributors.tdemployee.R
import com.tulsidistributors.tdemployee.databinding.FragmentNotificationBinding
import com.tulsidistributors.tdemployee.ui.home.fragment.adapter.NotificationAdapter
import com.tulsidistributors.tdemployee.ui.home.model.NotificationModel


class NotificationFragment : Fragment() {
    lateinit var binding:FragmentNotificationBinding
    lateinit var notificationRv:RecyclerView
    lateinit var notificationAdapter:NotificationAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding= FragmentNotificationBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        notificationRv=binding.notificationRv

        val list:ArrayList<NotificationModel> = ArrayList()
        list.add(NotificationModel("New Task","Surprisingly, that’s it. And that’s all you need to know to create RecyclerViews","2021-03-10"))
        list.add(NotificationModel("New Task","Surprisingly, that’s it. And that’s all you need to know to create RecyclerViews","2021-03-11"))

        notificationAdapter= NotificationAdapter(list)
        val linearLayout=LinearLayoutManager(activity)
        notificationRv.setLayoutManager(linearLayout)
        notificationRv.setAdapter(notificationAdapter)
    }

}