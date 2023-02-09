package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.db.remote.HomeService
import com.example.myapplication.db.remote.model.HomeModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private var _home_data = MutableLiveData<HomeModel>()
    val home_data: LiveData<HomeModel> get() = _home_data

    fun get_home_data(lat: Double, lng: Double) {
        HomeService.homeService.get_home_data(lat, lng).enqueue(object : Callback<HomeModel> {
            override fun onResponse(call: Call<HomeModel>, response: Response<HomeModel>) {
                if (response.isSuccessful) {
                    _home_data.postValue(response.body())
                } else {
                    _home_data.postValue(null)
                }
            }

            override fun onFailure(call: Call<HomeModel>, t: Throwable) {
                Log.d("whatisthis", "네트워크 오류가 발생했습니다." + t.message.toString())
                _home_data            }
        })
    }

}