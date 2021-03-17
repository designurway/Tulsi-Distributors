package com.tulsidistributors.tdemployee.model.admin_brand

data class AdminBrandModel(
    val status: String,
    val message: String,
    val brands_list: ArrayList<AdminBrandData>
)
