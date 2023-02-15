package com.example.myapplication.db.remote

import android.util.Log
import com.example.myapplication.ApplicationClass
import com.example.myapplication.db.remote.model.ResultOfSetDto
import com.example.myapplication.db.remote.model.UserProfileDto
import com.example.myapplication.db.remote.model.UserProfileEditDto
import com.example.myapplication.db.remote.remotedata.AuthRequest
import com.example.myapplication.db.remote.remotedata.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

object UserService {

    interface UserInterface {
        @GET("users/my-profile")
        fun get_user_profile_data(): Call<UserProfileDto>

        @PUT("user/profile")
        fun set_user_profile_data(@Body userUpdateDtoDto: UserProfileEditDto): Call<ResultOfSetDto>
    }
    val userService = ApplicationClass.retrofit.create(UserInterface::class.java)
}