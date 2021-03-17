package com.tulsidistributors.tdemployee.model.search_stock

data class SearchStockItemData(
    val id:String,
    val brand_name:String,
    val brand_logo:String,
    val product_code:String,
    val brand_id:String,
    val product_name:String,
    val product_desc:String,
    val basic_amount:String,
    val tax:String,
    val total_amount:String,
    val status:String,
    val quantity:String
)
