package com.example.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.db.remote.model.DressOrderListDto
import com.example.myapplication.db.remote.DressService
import com.example.myapplication.db.remote.model.DressDetailDto
import com.example.myapplication.db.remote.model.DressLikeDto
import com.example.myapplication.db.remote.model.DressSearchDto
import com.example.myapplication.db.remote.model.DressOrderDto
import com.example.myapplication.db.remote.model.UpdateDressLikeDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DressViewModel : ViewModel() {

    private var _dress_detail_data = MutableLiveData<DressDetailDto>()
    val dress_detail_data: LiveData<DressDetailDto> get() = _dress_detail_data

    private var _dress_like_data = MutableLiveData<List<DressLikeDto>>()
    val dress_like_data: LiveData<List<DressLikeDto>> get() = _dress_like_data

    private var _dress_search_data = MutableLiveData<DressSearchDto>()
    val dress_search_data: LiveData<DressSearchDto> get() = _dress_search_data

    private var _update_dress_like_data = MutableLiveData<UpdateDressLikeDto>()
    val update_dress_like_data: LiveData<UpdateDressLikeDto> get() = _update_dress_like_data

    private var _dress_resevation_data = MutableLiveData<List<DressOrderListDto>>()
    val dress_reservation_data: LiveData<List<DressOrderListDto>> get() = _dress_resevation_data

    private var _dress_reservation_dress_data = MutableLiveData<List<DressOrderDto>>()
    val dress_reservation_dress_data: LiveData<List<DressOrderDto>> get() = _dress_reservation_dress_data

    private var _completeorder = MutableLiveData<Int>()
    val completeorder: LiveData<Int> get() = _completeorder
    private var _pickup = MutableLiveData<Int>()
    val pickup: LiveData<Int> get() = _pickup
    private var _pickupconfirm = MutableLiveData<Int>()
    val pickupconfirm: LiveData<Int> get() = _pickupconfirm
    private var _purchaseconfirm = MutableLiveData<Int>()
    val purchaseconfirm: LiveData<Int> get() = _purchaseconfirm

    fun set_completeorder(size:Int){
        _completeorder.value = size
    }

    fun set_pickup(size:Int){

        _pickup.value = size
    }

    fun set_pickupconfirm(size:Int){

        _pickupconfirm.value = size
    }

    fun set_purchaseconfirm(size:Int){
        _purchaseconfirm.value = size
    }


    fun get_dress_detail_data(id: Int) {
        DressService.dressService.get_dress_detail_data(id).enqueue(object : Callback<DressDetailDto> {
                override fun onResponse(
                    call: Call<DressDetailDto>,
                    response: Response<DressDetailDto>
                ) {
                    if (response.isSuccessful) {
                        _dress_detail_data.value = (response.body())
                    } else {
                        _dress_detail_data.postValue(null)
                        Log.d("whatisthis", "_dress_detail_data, response 못받음")
                    }
                }

                override fun onFailure(call: Call<DressDetailDto>, t: Throwable) {
                    Log.d(
                        "whatisthis",
                        "get_dress_detail_data, 네트워크 오류가 발생했습니다." + t.message.toString()
                    )
                }
            })
    }

    fun set_dress_like_data(updateDressLikeDto: UpdateDressLikeDto) {
        DressService.dressService.set_dress_like_data(updateDressLikeDto).enqueue(object :
            Callback<UpdateDressLikeDto> {
            override fun onResponse(
                call: Call<UpdateDressLikeDto>,
                response: Response<UpdateDressLikeDto>
            ) {
                if (response.isSuccessful) {
                    _update_dress_like_data.value = response.body()
                } else {
                    Log.d("whatisthis", "좋아요 실패")
                }
            }

            override fun onFailure(call: Call<UpdateDressLikeDto>, t: Throwable) {
                Log.d("whatisthis", "set_dress_like_data, 네트워크 오류가 발생했습니다." + t.message.toString())
            }
        })
    }

    fun get_dress_like_data() {
        DressService.dressService.get_dress_like_data()
            .enqueue(object : Callback<List<DressLikeDto>> {
                override fun onResponse(
                    call: Call<List<DressLikeDto>>,
                    response: Response<List<DressLikeDto>>
                ) {
                    if (response.isSuccessful) {
                        _dress_like_data.postValue(response.body())
                    } else {
                        _dress_like_data.postValue(null)
                        Log.d("whatisthis", "_dress_like_data, response 못받음")
                    }
                }

                override fun onFailure(call: Call<List<DressLikeDto>>, t: Throwable) {
                    Log.d(
                        "whatisthis",
                        "get_dress_like_data, 네트워크 오류가 발생했습니다." + t.message.toString()
                    )
                }
            })
    }

//    @GET("dresses/orders/{dress_reservation_id}")
//    fun get_dress_reservation_dress_data(
//        @Path("dress_reservation_id")dress_reservation_id:Int
//    ):Call<DressOrderDto>

    //의상 주문 상세 내역 조회
    fun get_dress_reservation_dress_data(dress_reservation_id: Int) {
        DressService.dressService.get_dress_reservation_dress_data(dress_reservation_id)
            .enqueue(object :
                Callback<List<DressOrderDto>> {
                override fun onResponse(
                    call: Call<List<DressOrderDto>>,
                    response: Response<List<DressOrderDto>>
                ) {
                    if (response.isSuccessful) {
                        _dress_reservation_dress_data.value = response.body()
                    } else {
                        Log.d("whatisthis", " 실패")
                    }
                }

                override fun onFailure(call: Call<List<DressOrderDto>>, t: Throwable) {
                    Log.d(
                        "whatisthis",
                        "get_dress_reservation_dress_data, 네트워크 오류가 발생했습니다." + t.message.toString()
                    )
                }
            })
    }//

    fun get_dress_resevation_data(status: String) {
        DressService.dressService.get_dress_resevation_data(status)
            .enqueue(object :
                Callback<List<DressOrderListDto>> {
                override fun onResponse(
                    call: Call<List<DressOrderListDto>>,
                    response: Response<List<DressOrderListDto>>
                ) {
                    if (response.isSuccessful) {
                        _dress_resevation_data.value = response.body()
                    } else {
                        Log.d("whatisthis", " 실패")
                    }
                }

                override fun onFailure(call: Call<List<DressOrderListDto>>, t: Throwable) {
                    Log.d(
                        "whatisthis",
                        "get_dress_reservation_data, 네트워크 오류가 발생했습니다." + t.message.toString()
                    )
                }
            })
    }

    fun get_dress_search_data(category: String,lat: Double,lng: Double,name: String,sort: String) {
        DressService.dressService.get_dress_search_data(category,lat,lng,name,sort).enqueue(object : Callback<DressSearchDto> {
            override fun onResponse(
                call: Call<DressSearchDto>,
                response: Response<DressSearchDto>
            ) {
                if (response.isSuccessful) {
                    _dress_search_data.postValue(response.body())
                } else {
                    _dress_search_data.postValue(null)
                    Log.d("whatisthis","_dress_search_data, response 못받음")
                }
            }

            override fun onFailure(call: Call<DressSearchDto>, t: Throwable) {
                Log.d("whatisthis", "get_dress_search_data, 네트워크 오류가 발생했습니다." + t.message.toString())
            }
        })
    }

}