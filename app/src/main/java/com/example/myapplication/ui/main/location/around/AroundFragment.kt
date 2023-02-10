package com.example.myapplication.ui.main.location.around

import android.graphics.Color
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAroundBinding
import com.example.myapplication.db.remote.model.StoreCoordDtoList
import com.example.myapplication.db.remote.model.StoreDetailDto
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.ui.main.profile.orderstatus.OrderListDivider
import com.example.myapplication.viewmodel.StoreViewModel


class AroundFragment : BaseFragment<FragmentAroundBinding>(R.layout.fragment_around) {
    lateinit var storeViewModel: StoreViewModel
    private var nearstoredata: StoreCoordDtoList? = null
    private var StoreDetailDtolist = ArrayList<StoreDetailDto>()

    override fun init() {
        // 플로팅 버튼 이벤트 처리
        storeViewModel = ViewModelProvider(requireActivity()).get(StoreViewModel::class.java)

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
                AroundAdapter(clicklistener = (object : AroundAdapter.ClothesClickListener {
                    override fun onItemMarketFavoriteClick(view: View, position: Int) {
//                            if(StoreDetailDtolist[position].market_around_favorite==false){
//                                //화면에 보여주기
//                                Glide.with(this@AroundFragment)
//                                    .load(R.drawable.icon_favorite_filledpink) //이미지
//                                    .into(market_favorite) //보여줄 위치
//                                StoreDetailDtolist[position].market_around_favorite = true
//                            }else{
//                                //화면에 보여주기
//                                Glide.with(this@AroundFragment)
//                                    .load(R.drawable.icon_favorite_line) //이미지
//                                    .into(market_favorite) //보여줄 위치
//                                StoreDetailDtolist[position].market_around_favorite = false
//                            }
                    }

                    override fun onItemMarketLayoutClick(view: View, position: Int) {

                    }
                }))
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
            aroundAdapter.updatedata(StoreDetailDtolist)

//            if (storeViewModel.store_near_data.value != null) {
//                nearstoredata = storeViewModel.store_near_data.value!!
//
//                for (i in nearstoredata!!) {
//                    storeViewModel.get_store_detail_data(i.id, "전체")
//                    storeViewModel.store_detail_data.observe(
//                        viewLifecycleOwner,
//                        Observer<StoreDetailDto> {
//                            if (it != null) {
//                                StoreDetailDtolist.add(it)
//                                aroundAdapter.updatedata(StoreDetailDtolist)
//
//                            } else {
//                                Log.d("whatisthis", "1네트워크 오류가 발생했습니다.")
//                            }
//                        })
//                }
//            } else {
//                nearstoredata = null
//            }


//
//            // 1. 어댑터 생성 및 리사이클러뷰 연결
//            val aroundAdapter = AroundAdapter(clicklistener = (object : AroundAdapter.ClothesClickListener{
//                override fun onItemMarketFavoriteClick(view: View, position: Int) {
//                    if(marketAroundDataTemp[position].market_around_favorite==false){
//                        //화면에 보여주기
//                        Glide.with(this@AroundFragment)
//                            .load(R.drawable.icon_favorite_filledpink) //이미지
//                            .into(market_favorite) //보여줄 위치
//                        marketAroundDataTemp[position].market_around_favorite = true
//                    }else{
//                        //화면에 보여주기
//                        Glide.with(this@AroundFragment)
//                            .load(R.drawable.icon_favorite_line) //이미지
//                            .into(market_favorite) //보여줄 위치
//                        marketAroundDataTemp[position].market_around_favorite = false
//                    }
//                }
//
//                override fun onItemMarketLayoutClick(view: View, position: Int) {
//
//                }
//            }))
//
//            aroundRecyclerview.adapter = aroundAdapter
//            aroundRecyclerview.layoutManager = LinearLayoutManager(context)
//            aroundRecyclerview.addItemDecoration(OrderListDivider(0f,0f,4f,4f, Color.TRANSPARENT))
//            aroundAdapter.userList = marketAroundDataTemp
//            aroundAdapter.notifyDataSetChanged()
        }
    }
}