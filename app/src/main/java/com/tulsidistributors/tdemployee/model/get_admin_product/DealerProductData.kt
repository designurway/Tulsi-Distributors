package com.tulsidistributors.tdemployee.model.get_admin_product

import retrofit2.http.GET

data class DealerProductData(
    val product_name:String,
    val basic_amount:String,
    val total_amount:String,
    val tax:String,
    val description:String,
    val product_image:String,
    val brand_id:String,
    var quantity:Int


)