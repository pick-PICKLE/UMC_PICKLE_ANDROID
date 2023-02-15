package com.example.myapplication.db.remote.model

data class UpdateStoreLikeDto(
    var is_like: Boolean?=false,
    var store_id: Int
    )