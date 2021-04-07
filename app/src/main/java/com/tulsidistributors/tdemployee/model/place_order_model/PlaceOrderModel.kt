package com.tulsidistributors.tdemployee.model.place_order_model

data class PlaceOrderModel(
    val reference_id:String,
    val dealer_id:String,
    val sales_executive_id:String,
    val total_amount:String,
    val pending_amount:String,
    val purchase_date:String,
    val advance_amount:String,
    val order_items:ArrayList<PlaceOrderData>
)