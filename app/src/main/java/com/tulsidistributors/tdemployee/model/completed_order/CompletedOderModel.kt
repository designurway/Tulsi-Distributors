package com.tulsidistributors.tdemployee.model.completed_order

data class CompletedOderModel(
    val status: String,
    val message: String,
    val completed_order: ArrayList<CompletedOrderData>
)