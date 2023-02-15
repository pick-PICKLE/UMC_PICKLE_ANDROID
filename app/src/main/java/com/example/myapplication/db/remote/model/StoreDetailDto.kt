package com.example.myapplication.db.remote.model

data class StoreDetailDto(
    var hours_of_operation: String? = null,
    var is_liked : Boolean ?= false,
    var storeId: Int,
    var store_address: String? = null,
    var store_dress_list: List<DressBriefInStoreDTO>? = null,
    var store_image_url: String? = null,
    var store_name: String? = null,
    var store_open_day: String? = null
)