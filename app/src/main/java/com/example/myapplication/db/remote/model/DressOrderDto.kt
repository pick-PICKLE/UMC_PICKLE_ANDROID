package com.example.myapplication.db.remote.model

data class DressOrderDto(
    val comment: String,
    val dress_reservation_id: Int,
    val hours_of_operation: String,
    val pickup_datetime: String,
    val price: String,
    val reservation_status: String,
    val reservedDressList: List<ReservedDressDto>,
    val store_address: String,
    val store_name: String,
    val store_open_day: String
)