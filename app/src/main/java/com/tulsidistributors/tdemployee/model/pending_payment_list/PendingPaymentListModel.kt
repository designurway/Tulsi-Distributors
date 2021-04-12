package com.tulsidistributors.tdemployee.model.pending_payment_list

data class PendingPaymentListModel(
    val status:String,
    val message:String,
    val pending_order_list:ArrayList<PendingPaymentListData>
)