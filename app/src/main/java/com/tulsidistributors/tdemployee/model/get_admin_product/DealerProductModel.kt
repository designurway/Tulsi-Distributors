package com.tulsidistributors.tdemployee.model.get_admin_product

data class DealerProductModel(
    val status: String,
    val message: String,
    val product_details: ArrayList<DealerProductData>
)
