package com.example.myapplication.db.remote.model

data class DressBriefInStoreDTO(
    var dress_id: Int,
    var dress_image_url: String,
    var dress_name: String,
    var dress_price: String,
    var is_liked: Boolean

)