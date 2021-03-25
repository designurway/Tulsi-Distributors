package com.tulsidistributors.tdemployee.model.placed_order_list

data class PlacedOrderListModel(
    val status:String,
    val message:String,
    val placed_order_list:ArrayList<PlacedOrderListData>
)
