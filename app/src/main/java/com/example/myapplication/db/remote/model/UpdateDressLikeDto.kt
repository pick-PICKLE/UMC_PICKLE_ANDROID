package com.example.myapplication.db.remote.model

data class UpdateDressLikeDto(
    var dress_id: Int,
    var is_like: Boolean?=false
)