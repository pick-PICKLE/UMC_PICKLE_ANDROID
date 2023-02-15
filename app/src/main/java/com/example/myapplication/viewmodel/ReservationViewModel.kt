package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.db.remote.ReservationService
import com.example.myapplication.db.remote.model.DressDetailDto
import com.example.myapplication.db.remote.model.DressReservationDto
import com.example.myapplication.db.remote.model.ResultOfSetDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationViewModel : ViewModel() {

    private var _dress_reservation_data = MutableLiveData<DressDetailDto>()
    val dress_reservation_data: LiveData<DressDetailDto> get() = _dress_reservation_data

    fun set_dresses_reservation(dressReservationDto: DressReservationDto){
        ReservationService.reservationService.set_dresses_reservation(dressReservationDto).enqueue(object :
            Callback<ResultOfSetDto> {
            override fun onResponse(call: Call<ResultOfSetDto>, response: Response<ResultOfSetDto>) {
                if(response.isSuccessful) {
                    val resp = response.body()
                    Log.d("isitsuccessful","data:\n"+resp.toString())
                }else{
                    Log.d("fail","실패")
                }
            }

            override fun onFailure(call: Call<ResultOfSetDto>, t: Throwable) {
                Log.d("whatisthis","네트워크 오류가 발생했습니다."+ t.message.toString())
            }
        })
    }


    fun get_dresses_reservation(id: Int){
        ReservationService.reservationService.get_dresses_reservation(id).enqueue(object : Callback<DressDetailDto> {
            override fun onResponse(call: Call<DressDetailDto>, response: Response<DressDetailDto>) {
                if(response.isSuccessful) {
                    val resp = response.body()
                    Log.d("isitsuccessful","data:\n"+resp.toString())
                }else{
                    Log.d("fail","실패")
                }
            }

            override fun onFailure(call: Call<DressDetailDto>, t: Throwable) {
                Log.d("whatisthis","네트워크 오류가 발생했습니다."+ t.message.toString())
            }
        })
    }


}