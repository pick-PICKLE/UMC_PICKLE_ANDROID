package com.example.myapplication.view

import android.view.View

interface ItemListClickInterface {
    fun onItemMarketFavoriteClick(like:Boolean, id:Int, view : View, position: Int)
    fun onItemMarketLayoutClick(id:Int, position: Int)
}