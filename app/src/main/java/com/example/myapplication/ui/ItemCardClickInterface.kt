package com.example.myapplication.ui

import android.view.View
import android.widget.ImageView
import android.widget.ListAdapter

interface ItemCardClickInterface {
    fun onItemClothImageClick(id:Int, position: Int)
    fun onItemStoreNameClick(id:Int, position: Int)
    fun onItemClothFavoriteClick(like:Boolean, id:Int, view : View, position: Int)
}