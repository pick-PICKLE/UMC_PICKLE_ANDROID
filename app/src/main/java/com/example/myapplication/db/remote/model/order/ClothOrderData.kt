package com.example.myapplication.db.remote.model.order

data class ClothOrderData(
    val color: String,
    var size: String,
    var count: Int,
    var clothPrice : Int,
    var coloridx : Int,
    var sizeidx: Int
)
