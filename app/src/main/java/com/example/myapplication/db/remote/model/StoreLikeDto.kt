package com.example.myapplication.db.remote.model

data class StoreLikeDto(
    var address: String,
    var hours_of_operation: String,
    var imageUrl: String,
    var name: String,
    val store_id: Int,
    var store_open_day: String
)
