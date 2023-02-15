package com.example.myapplication.db.remote.model

data class UserProfileDto(
    var data: UserProfileDetailDto? = null,
    var success: Boolean? = null
)

data class UserProfileDetailDto(
    var email: String? = null,
    var image: String,
    var name: String,
    var userId: Int? = null
)