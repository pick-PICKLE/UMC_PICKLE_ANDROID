package com.example.myapplication.db.remote.model

data class DressSearchDto(
    var data: List<DressSearchResultDto>? = null,
    var success: Boolean? = null
)

data class DressSearchResultDto(
    var dress_id: Int,
    var dress_image_url: String? = null,
    var dress_name: String? = null,
    var dress_price: String? = null,
    var is_liked: Boolean? = null,
    var store_id: Int,
    var store_name: String? = null
)