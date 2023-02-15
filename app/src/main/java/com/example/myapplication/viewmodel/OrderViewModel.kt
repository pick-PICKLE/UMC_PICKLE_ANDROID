package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.db.remote.model.order.ClothOptionData
import com.example.myapplication.db.remote.model.order.ClothOrderData

class OrderViewModel : ViewModel() {

    private var _order_data = MutableLiveData<ArrayList<ClothOrderData>>()
    val order_data: LiveData<ArrayList<ClothOrderData>> get() = _order_data

    private var _option_data = MutableLiveData<ClothOptionData>()
    val option_data: LiveData<ClothOptionData> get() = _option_data

    fun set_order_data(clothorderdatalist:ArrayList<ClothOrderData>?) {
        _order_data.value = clothorderdatalist
    }

    fun set_option_data(clothOptionData: ClothOptionData){
        _option_data.value = clothOptionData
    }
}