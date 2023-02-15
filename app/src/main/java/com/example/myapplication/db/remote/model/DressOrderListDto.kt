package com.example.myapplication.db.remote.model

data class DressOrderListDto(
    var dress_image_url: String,
    var dress_name: String,
    var dress_reservation_id: Int,
    var order_time: String,
    var price: String,
    var reservation_status: String,
    var store_name: String
)
