package com.tulsidistributors.tdemployee.model.assign_order

data class AssignedOrderModel(
    val status: String,
    val message: String,
    val assigned_order: ArrayList<AssignedOrderData>
)
