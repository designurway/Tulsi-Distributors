package com.tulsidistributors.tdemployee.model.place_order_model

data class PlaceOrderData(
    val sales_executive_id: String,
    val product_id: String,
    val ref_no: String,
    val dealer_id: String,
    val routing_id: String,
    val stock_quantity: String,
    var order_quantity: String
)
