package com.example.myapplication.db.remote

import retrofit2.Call
import com.example.myapplication.ApplicationClass
import com.example.myapplication.db.remote.model.DressDetailDto
import com.example.myapplication.db.remote.model.DressReservationDto
import com.example.myapplication.db.remote.model.ResultOfSetDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

object ReservationService {

    interface ReservationInterface {

        @POST("dresses/reservation")
        fun set_dresses_reservation(
            @Body dressReservationDto: DressReservationDto
        ): Call<ResultOfSetDto>

        @GET("dresses/reservation/{id}")
        fun get_dresses_reservation(
            @Path("id") id: Int
        ): Call<DressDetailDto>
    }

    val reservationService = ApplicationClass.retrofit.create(ReservationService.ReservationInterface::class.java)
}