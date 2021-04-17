package com.tulsidistributors.tdemployee.model.place_order_model

data class PlaceOrderData(
    var routing_id: String,
    var sales_executive_id: String,
    var product_id: String,
    var ref_no: String,
    var dealer_id: String,
    var order_quantity: String,
    var pending_order:String,
    var item_price: String,
    var tax: String,
    var price_after_tax: String,


    )
