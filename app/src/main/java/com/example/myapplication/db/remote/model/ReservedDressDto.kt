package com.example.myapplication.db.remote.model

data class ReservedDressDto(
    var dress_imgurl: String,
    var dress_name: String,
    var dress_option_detail: String,
    var reserved_dress_id: Int,
    var select_price: String
)
