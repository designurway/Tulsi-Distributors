package com.tulsidistributors.tdemployee.model.login

data class LoginData(
    val id:String,
    val emp_id:String,
    val first_name:String,
    val last_name:String,
    val email:String,
    val phone_number:String,
    val otp:String,
    val alter_number:String,
    val address:String,
    val profile:String,
    val aadhar_number:String,
    val aadhar_image:String,
    val date_of_birth:String,
    val date_of_joining:String,
    val password:String,
)
