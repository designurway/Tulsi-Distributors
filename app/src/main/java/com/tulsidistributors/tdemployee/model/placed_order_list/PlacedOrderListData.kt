package com.tulsidistributors.tdemployee.model.placed_order_list

data class PlacedOrderListData(
    val dealer_id:String,
    val date:String,
    val purchase_date:String,
    val order_quantity:String,
    val product_name:String,
    val product_image:String,
    val description:String,
    val product_price:String,
    val login_time:String,
)
