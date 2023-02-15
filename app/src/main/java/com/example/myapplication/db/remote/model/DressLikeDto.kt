package com.example.myapplication.db.remote.model

data class DressLikeDto(
    val dress_id: Int,
    var image: String?=null,
    var dress_name: String?=null,
    var price: String?=null,
    val store_name: String
)