package com.tulsidistributors.tdemployee.model.pending_payment_list

data class PendingPaymentListData(
    val purchase_date:String,
    val dealer_id:String,
    val reference_id:String,
    val pending_amount:String,
    val total_amount:String,
    val advance_amount:String,
)
