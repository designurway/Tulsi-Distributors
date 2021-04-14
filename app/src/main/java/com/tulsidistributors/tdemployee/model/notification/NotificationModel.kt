package com.tulsidistributors.tdemployee.ui.home.fragment.models

import com.google.gson.annotations.SerializedName

data class NotificationModel(

    val title:String,

    val body: String,
    val profile: String,
    val created_date: String,

    @SerializedName("created_date")
    val createdDate:String


)
