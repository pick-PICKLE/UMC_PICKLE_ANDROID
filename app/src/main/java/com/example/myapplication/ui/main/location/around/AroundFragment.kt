package com.example.myapplication.ui.main.location.around

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAroundBinding
import com.example.myapplication.db.remote.model.StoreCoordDtoList
import com.example.myapplication.db.remote.model.UpdateDressLikeDto
import com.example.myapplication.db.remote.model.UpdateStoreLikeDto
import com.example.myapplication.ui.ItemListClickInterface
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.ui.main.profile.orderstatus.OrderListDivider
import com.example.myapplication.ui.storecloth.storedetail.StoreActivity
import com.example.myapplication.viewmodel.HomeViewModel
import com.example.myapplication.viewmodel.StoreViewModel
import kotlinx.android.synthetic.main.item_around_recycler.*


class AroundFragment : BaseFragment<FragmentAroundBinding>(R.layout.fragment_around) {
    lateinit var storeViewModel: StoreViewModel
    lateinit var homeViewModel: HomeViewModel

    override fun init() {
        // 플로팅 버튼 이벤트 처리
        storeViewModel = ViewModelProvider(requireActivity()).get(StoreViewModel::class.java)
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        binding.aroundFab.setOnClickListener {
            parentFragmentManager
                .popBackStackImmediate(null, 0)
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(binding) {
            // 1. 어댑터 생성 및 리사이클러뷰 연결
            val aroundAdapter =
                AroundAdapter(clicklistener = (object : ItemListClickInterface {
                    override fun onItemMarketFavoriteClick(like: Boolean, id: Int, view: View, position: Int) {
                        storeViewModel.set_store_like_data(UpdateStoreLikeDto(false,id))
                        homeViewModel.get_home_data(
                            homeViewModel.home_latlng.value!!.first,
                            homeViewModel.home_latlng.value!!.second
                        )
                        storeViewModel.get_store_like_data()
                    }

                    override fun onItemMarketLayoutClick(id: Int, position: Int) {
                        val intent = Intent(context, StoreActivity::class.java)
                        intent.putExtra("store_id", id)
                        startActivity(intent)
                    }
                }))

            storeViewModel.store_near_data.observe(viewLifecycleOwner, Observer<StoreCoordDtoList> { now_StoreCoordDtoList ->
                    if(now_StoreCoordDtoList != null){
                        aroundAdapter.submitList(now_StoreCoordDtoList.toMutableList())
                    }else{
                        Log.d("whatisthis", "store_near_data, 데이터 없음")
                    }
            })

            storeViewModel.update_store_like_data.observe(
                viewLifecycleOwner,
                Observer<UpdateStoreLikeDto>{ update_storelikedata ->
                    if(update_storelikedata!=null){
                        storeViewModel.get_store_near_data(storeViewModel.screen_latlng.value!!.first, storeViewModel.screen_latlng.value!!.second)
                    }else{
                        Log.d("whatisthis", "update_store_like_data, 데이터 없음")
                    }
                }
            )

            aroundRecyclerview.adapter = aroundAdapter
            aroundRecyclerview.layoutManager = LinearLayoutManager(context)
            aroundRecyclerview.addItemDecoration(
                OrderListDivider(
                    0f,
                    0f,
                    4f,
                    4f,
                    Color.TRANSPARENT
                )
            )

        }
    }
}