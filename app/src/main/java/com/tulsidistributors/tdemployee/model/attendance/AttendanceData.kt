package com.tulsidistributors.tdemployee.model.attendance

data class AttendanceData(
    val emp_id:String,
    val first_name:String,
    val profile:String,
    val email:String,
    val phone_number:String,
    val date:String,
    val logi_in_time:String,
    val logout_time:String
)
