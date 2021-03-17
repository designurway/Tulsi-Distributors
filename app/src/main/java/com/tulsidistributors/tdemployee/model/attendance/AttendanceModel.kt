package com.tulsidistributors.tdemployee.model.attendance

data class AttendanceModel(
    val status: String,
    val message: String,
    val data: AttendanceData
)