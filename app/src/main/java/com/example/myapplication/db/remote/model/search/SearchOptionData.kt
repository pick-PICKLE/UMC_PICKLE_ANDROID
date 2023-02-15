package com.example.myapplication.db.remote.model.search

import com.example.myapplication.db.remote.model.DressOptionDto

data class SearchOptionData(
    var category : String ?= "전체",
    var sort: String ?= "좋아요 많은 순",
)
