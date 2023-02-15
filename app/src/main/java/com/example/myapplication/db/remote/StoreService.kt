package com.example.myapplication.db.remote

import com.example.myapplication.ApplicationClass
import com.example.myapplication.db.remote.model.*
import retrofit2.Call
import retrofit2.http.*

object StoreService { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.

    interface StoreInterface {
        @GET("stores/near")
        fun get_store_near_data(
            @Query("lat") lat: Double,
            @Query("lng") lng: Double
        ): Call<StoreCoordDtoList>

        @GET("stores/detail/{store_id}")
        fun get_store_detail_data(
            @Path(value = "store_id") id: Int,
            @Query("category") category: String
        ): Call<StoreDetailDto>

        @GET("stores/like-list")
        fun get_store_like_data(
        ): Call<List<StoreLikeDto>>

        // 한 의상에 대한 좋아요 정보 수정
        @POST("stores/like")
        fun set_store_like_data(
            @Body updateStoreLikeDto: UpdateStoreLikeDto
        ): Call<UpdateStoreLikeDto>

    }
    val storeService = ApplicationClass.retrofit.create(StoreService.StoreInterface::class.java)
}