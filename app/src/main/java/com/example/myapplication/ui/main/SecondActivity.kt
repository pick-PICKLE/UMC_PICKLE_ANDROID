package com.example.myapplication.ui.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivitySecondBinding
import com.example.myapplication.ui.base.BaseActivity
import com.example.myapplication.ui.main.chat.ChatFragment
import com.example.myapplication.ui.main.favorite.FavoriteBaseFragment
import com.example.myapplication.ui.main.home.HomeBaseFragment
import com.example.myapplication.ui.main.location.LocationFragment
import com.example.myapplication.ui.main.profile.ProfileBlankFragment
import com.example.myapplication.util.LocationPermissionUtils
import com.example.myapplication.util.LocationPopupUtils
import com.example.myapplication.viewmodel.DressViewModel
import com.example.myapplication.viewmodel.HomeViewModel
import com.example.myapplication.viewmodel.StoreViewModel

class SecondActivity : BaseActivity<ActivitySecondBinding>(R.layout.activity_second) {
    private lateinit var currentFragmenttag: String

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var dressViewModel: DressViewModel
    private lateinit var storeViewModel: StoreViewModel

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentFragmenttag = savedInstanceState.get("currentfragment").toString()
    }

    override fun savedatainit() {
        supportFragmentManager
            .beginTransaction()
            .add(binding.secondFramelayout.id, HomeBaseFragment(), "homebase")
            .commitAllowingStateLoss()
        currentFragmenttag = "homebase" // 현재 보고 있는 fragmet의 Tag
    }

    override fun onResume() {
        super.onResume()
        dressViewModel.get_dress_like_data()
        storeViewModel.get_store_like_data()
        // 홈화면 데이터 갱신
        requestLocationData()
        dressViewModel.get_dress_resevation_data("주문완료")
        dressViewModel.dress_reservation_data.observe(this, Observer {
            dressViewModel.set_completeorder(it.size)
        })
    }

    override fun init() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        dressViewModel = ViewModelProvider(this).get(DressViewModel::class.java)
        storeViewModel = ViewModelProvider(this).get(StoreViewModel::class.java)

        // 네비게이션 버튼의 테마색으로 변하는 것을 막기 위해서
        binding.secondBottomNavigationView.itemIconTintList = null

        // 네비게이션 버튼 클릭시 프래그먼트 전환
        binding.secondBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> { // 첫 번째 fragment
                    changeFragment("homebase", HomeBaseFragment())
                }
                R.id.menu_favorite -> { // 두 번째 fragment
                    changeFragment("favorite", FavoriteBaseFragment())
                }
                R.id.menu_map -> { // 세 번째 fragment
                    changeFragment("location", LocationFragment())
                }
                R.id.menu_chat -> { // 세 번째 fragment
                    changeFragment("chat", ChatFragment())
                }
                R.id.menu_profileblank -> {
                    changeFragment("profileblank", ProfileBlankFragment())
                }
            }
            true
        }

        // 홈화면 데이터 갱신
        homeViewModel.home_latlng.observe(this, Observer<Pair<Double, Double>> {
            if (it != null) {
                homeViewModel.get_home_data(it!!.first, it!!.second)
            } else {
                Log.d("whatisthis", "home_latlng, 데이터 없음")
            }
        })
    }

    private fun changeFragment(tag: String, fragment: Fragment) {
        // supportFragmentManager에 "first"라는 Tag로 저장된 fragment 있는지 확인
        if (supportFragmentManager.findFragmentByTag(tag) == null) { // Tag가 없을 때 -> 없을 리가 없다.
            supportFragmentManager
                .beginTransaction()
                .hide(supportFragmentManager.findFragmentByTag(currentFragmenttag)!!)
                .add(binding.secondFramelayout.id, fragment, tag)
                .commitAllowingStateLoss()

        } else { // Tag가 있을 때
            // 먼저 currentFragmenttag에 저장된 '이전 fragment Tag'를 활용해 이전 fragment를 hide 시킨다.
            // supportFragmentManager에 저장된 "first"라는 Tag를 show 시킨다.
            supportFragmentManager
                .beginTransaction()
                .hide(supportFragmentManager.findFragmentByTag(currentFragmenttag)!!)
                .show(supportFragmentManager.findFragmentByTag(tag)!!)
                .commitAllowingStateLoss()
        }
        // currentFragmenttag에 '현재 fragment Tag' "first"를 저장한다.
        currentFragmenttag = tag
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("currentfragment", currentFragmenttag)
    }
}