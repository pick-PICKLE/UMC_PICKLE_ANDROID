package com.example.myapplication.db.remote.model

data class DressOverviewDto(
    var dress_default_img: String? = null,
    var dress_id: Int,
    var dress_like: Boolean?= false,
    var dress_name: String? = null,
    var dress_price: String? = null,
    var store_id: Int,
    var store_name: String? = null
)
