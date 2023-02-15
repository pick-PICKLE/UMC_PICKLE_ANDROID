package com.example.myapplication.ui.storecloth.clothdetail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityClothBinding
import com.example.myapplication.ui.base.BaseActivity
import com.example.myapplication.viewmodel.DressViewModel
import com.example.myapplication.viewmodel.HomeViewModel

class ClothActivity : BaseActivity<ActivityClothBinding>(R.layout.activity_cloth) {
    private lateinit var dressViewModel: DressViewModel
    private lateinit var homeViewModel: HomeViewModel

    override fun savedatainit() {
        dressViewModel = ViewModelProvider(this).get(DressViewModel::class.java)
        dressViewModel.get_dress_detail_data(intent.getIntExtra("cloth_id",0))

        // lat, lng 정보를 얻기 위해서
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        requestLocationData()

        supportFragmentManager
            .beginTransaction()
            .add(binding.clothLayout.id, ClothBlankFragment(), "clothblank")
            .commitAllowingStateLoss()
    }

    override fun init() {
    }
}