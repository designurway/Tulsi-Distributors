package com.tulsidistributors.tdemployee.model.attendance

import com.google.gson.annotations.SerializedName

data class AttendanceData(
    val emp_id:String,
    val first_name:String,
    val profile:String,
    val email:String,
    val phone_number:String,
    val login_date:String,
    val login_time:String,
    @SerializedName("logout")
    val logout_time:String
)
