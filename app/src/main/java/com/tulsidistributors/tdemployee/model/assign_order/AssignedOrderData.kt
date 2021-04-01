package com.tulsidistributors.tdemployee.model.assign_order

data class AssignedOrderData(
    val dealer_id:String,
    val name:String,
    val shop_name:String,
    val address:String,
    val latitude:String,
    val longitude:String,
    val order_status:String,
    val routing_id:String,
    val date:String
    )