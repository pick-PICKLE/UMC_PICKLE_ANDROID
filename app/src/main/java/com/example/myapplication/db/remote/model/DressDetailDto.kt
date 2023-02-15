package com.example.myapplication.db.remote.model

data class DressDetailDto(
    var comment: String? = "",
    var dress_id: Int,
    var dress_image_url_list: List<String>? = listOf(),
    var dress_name: String? = "",
    var dress_option1: DressOptionDto? = DressOptionDto(),
    var dress_option2: DressOptionDto? = DressOptionDto(),
    var dress_price: Int,
    var dress_stock: List<DressStockDto>? = listOf(),
    var is_liked:Boolean,
    var store_id: Int,
    var store_name: String? = ""
)
