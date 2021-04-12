package com.tulsidistributors.tdemployee.model.place_order_model

data class PlaceOrderModel(
    val routingId:String,
    val order_status:String,
    val order_list:ArrayList<PlaceOrderData>
)