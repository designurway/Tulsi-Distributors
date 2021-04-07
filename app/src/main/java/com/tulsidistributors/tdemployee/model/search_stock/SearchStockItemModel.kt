package com.tulsidistributors.tdemployee.model.search_stock

data class SearchStockItemModel(
    val status:String,
    val message:String,
    val stock_list:ArrayList<SearchStockItemData>
)
