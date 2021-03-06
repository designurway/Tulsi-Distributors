package com.tulsidistributors.tdemployee.model.completed_order

data class CompletedOrderData(
    val dealer_id: String,
    val name: String,
    val shop_name: String,
    val address: String,
    val latitude: String,
    val longitude: String,
    val routing_id: String,
    val order_status: String,
    val date: String
)