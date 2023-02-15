package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.db.remote.model.DressDetailDto

class CategorySortViewModel : ViewModel() {
    private var _category_data = MutableLiveData<String>()
    val category_data: LiveData<String> get() = _category_data

    private var _sort_data = MutableLiveData<String>()
    val sort_data: LiveData<String> get() = _sort_data

    fun set_category_data(category: String) {
        _category_data.value = category
    }

    fun set_sort_data(sort: String) {
        _sort_data.value = sort
    }
}