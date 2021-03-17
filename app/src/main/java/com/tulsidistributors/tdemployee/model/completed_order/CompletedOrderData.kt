package com.tulsidistributors.tdemployee.model.completed_order

data class CompletedOrderData(
    val id: String,
    val shop_name: String,
    val address: String,
    val phone_number: String,
    val order_status: String,
    val date_time_in: String
)