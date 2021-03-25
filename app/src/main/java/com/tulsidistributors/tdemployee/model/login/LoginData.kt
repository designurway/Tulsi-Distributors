package com.tulsidistributors.tdemployee.model.login

data class LoginData(
    val id:String,
    val emp_id:String,
    val distributor_id:String,
    val brand_id:String,
    val first_name:String,
    val last_name:String,
    val email:String,
    val phone_number:String,
    val alter_number:String,
    val device_id:String,
    val address:String,
    val profile:String,
    val role:String,
    val aadhar_number:String,
    val aadhar_image:String,
    val date_of_birth:String,
    val date_of_joining:String,
)
