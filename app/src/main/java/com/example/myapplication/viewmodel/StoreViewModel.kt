package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.db.remote.StoreService
import com.example.myapplication.db.remote.model.StoreCoordDtoList
import com.example.myapplication.db.remote.model.StoreDetailDto
import com.example.myapplication.db.remote.model.StoreLikeDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreViewModel : ViewModel() {

    private var _store_near_data = MutableLiveData<StoreCoordDtoList>()
    val store_near_data: LiveData<StoreCoordDtoList> get() = _store_near_data

    private var _store_detail_data = MutableLiveData<StoreDetailDto>()
    val store_detail_data: LiveData<StoreDetailDto> get() = _store_detail_data

    private var _store_like_data = MutableLiveData<List<StoreLikeDto>>()
    val store_like_data: LiveData<List<StoreLikeDto>> get() = _store_like_data

    fun get_store_like_data() {
        StoreService.storeService.get_store_like_data().enqueue(object : Callback<List<StoreLikeDto>> {
            override fun onResponse(call: Call<List<StoreLikeDto>>, response: Response<List<StoreLikeDto>>) {
                if (response.isSuccessful) {
                    _store_like_data.postValue(response.body())
                } else {
                    _store_like_data.postValue(null)
                    Log.d("whatisthis", "_store_like_data, response 못받음")
                }
            }

            override fun onFailure(call: Call<List<StoreLikeDto>>, t: Throwable) {
                Log.d("whatisthis", "get_store_like_data, 네트워크 오류가 발생했습니다." + t.message.toString())
                _store_like_data.postValue(null)
            }
        })
    }


    fun get_store_near_data(lat: Double, lng: Double) {
        StoreService.storeService.get_store_near_data(lat, lng).enqueue(object : Callback<StoreCoordDtoList> {
            override fun onResponse(call: Call<StoreCoordDtoList>, response: Response<StoreCoordDtoList>) {
                if (response.isSuccessful) {
                    _store_near_data.postValue(response.body())
                } else {
                    _store_near_data.postValue(null)
                    Log.d("whatisthis", "_store_near_data, response 못받음")
                }
            }

            override fun onFailure(call: Call<StoreCoordDtoList>, t: Throwable) {
                Log.d("whatisthis", "get_store_near_data, 네트워크 오류가 발생했습니다." + t.message.toString())
                _store_near_data.postValue(null)
            }
        })
    }

    fun get_store_detail_data(id: Int, category: String) {
        StoreService.storeService.get_store_detail_data(id, category)
            .enqueue(object : Callback<StoreDetailDto> {
                override fun onResponse(
                    call: Call<StoreDetailDto>,
                    response: Response<StoreDetailDto>
                ) {
                    if (response.isSuccessful) {
                        _store_detail_data.postValue(response.body())
                    } else {
                        _store_detail_data.postValue(null)
                        Log.d("whatisthis", "_store_detail_data, response 못받음")
                    }
                }

                override fun onFailure(call: Call<StoreDetailDto>, t: Throwable) {
                    Log.d("whatisthis", "get_store_detail_data, 네트워크 오류가 발생했습니다." + t.message.toString())
                    _store_detail_data.postValue(null)
                }
            })
    }

}