package com.example.myapplication.ui.main.favorite

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFavoriteBaseBinding
import com.example.myapplication.db.remote.model.DressLikeDto
import com.example.myapplication.db.remote.model.UpdateDressLikeDto
import com.example.myapplication.db.remote.model.UpdateStoreLikeDto
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.ui.main.favorite.item.FavoriteClothFragment
import com.example.myapplication.ui.main.favorite.store.FavoriteStoreFragment
import com.example.myapplication.viewmodel.DressViewModel
import com.example.myapplication.viewmodel.HomeViewModel
import com.example.myapplication.viewmodel.StoreViewModel
import com.google.android.material.tabs.TabLayoutMediator

class FavoriteBaseFragment : BaseFragment<FragmentFavoriteBaseBinding>(R.layout.fragment_favorite_base) {
    lateinit var dressViewModel: DressViewModel
    lateinit var storeViewModel: StoreViewModel

    override fun init() {
        dressViewModel = ViewModelProvider(requireActivity()).get(DressViewModel::class.java)
        storeViewModel = ViewModelProvider(requireActivity()).get(StoreViewModel::class.java)
        dressViewModel.get_dress_like_data()
        storeViewModel.get_store_like_data()

        dressViewModel.update_dress_like_data.observe(viewLifecycleOwner, Observer<UpdateDressLikeDto> { update_dresslikedata ->
            if(update_dresslikedata!=null){
                dressViewModel.get_dress_like_data()

            }else{
                Log.d("whatisthis", "dress_like_data, 데이터 없음")
            }
        })

        storeViewModel.update_store_like_data.observe(viewLifecycleOwner, Observer<UpdateStoreLikeDto> { update_storelikedata ->
            if(update_storelikedata!=null){
                storeViewModel.get_store_like_data()

            }else{
                Log.d("whatisthis", "store_like_data, 데이터 없음")
            }
        })

        initAppbar(binding.favoritebaseToolbar,"찜 목록",false,true)
        initViewPager()
    }

    private fun initViewPager() {
        //ViewPager2 Adapter 셋팅
        var internalViewpagerAdapter = FavoriteBaseAdapter(this)
        internalViewpagerAdapter.addFragment(FavoriteClothFragment())
        internalViewpagerAdapter.addFragment(FavoriteStoreFragment())

        //Adapter 연결
        binding.favoriteViewpager.apply {
            adapter = internalViewpagerAdapter

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }

        //ViewPager, TabLayout 연결
        TabLayoutMediator(binding.favoriteTablayout, binding.favoriteViewpager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "아이템"
                }
                1 -> {
                    tab.text = "스토어"
                }
            }
        }.attach()
    }
}



