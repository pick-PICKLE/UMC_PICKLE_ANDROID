package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.db.remote.model.search.SearchOptionData

class SearchViewModel : ViewModel() {
    private var _option_data = MutableLiveData<SearchOptionData>()
    val option_data: LiveData<SearchOptionData> get() = _option_data

    fun set_order_data(searchOptionData: SearchOptionData) {
        _option_data.value = searchOptionData
    }
}