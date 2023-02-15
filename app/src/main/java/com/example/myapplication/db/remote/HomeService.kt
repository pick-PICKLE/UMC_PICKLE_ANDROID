package com.example.myapplication.db.remote

import com.example.myapplication.ApplicationClass
import com.example.myapplication.db.remote.model.DressHomeDto
import com.example.myapplication.db.remote.model.DressOverviewDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

object HomeService {

    interface HomeInterface {
        @GET("home")
        fun get_home_data(
            @Query("lat") lat: Double,
            @Query("lng") lng: Double
        ): Call<DressHomeDto>

        @GET("home/new")
        fun get_home_new_data(
            @Query("lat") lat: Double,
            @Query("lng") lng: Double
        ): Call<List<DressOverviewDto>>

        @GET("home/recent")
        fun get_home_recent_data(
        ): Call<List<DressOverviewDto>>

        @GET("home/recommendation")
        fun get_home_recommendation_data(
            @Query("lat") lat: Double,
            @Query("lng") lng: Double
        ): Call<List<DressOverviewDto>>
    }

    val homeService = ApplicationClass.retrofit.create(HomeService.HomeInterface::class.java)

}