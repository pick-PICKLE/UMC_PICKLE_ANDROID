package com.example.myapplication.ui.storecloth.clothdetail

import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentClothDetailBinding
import com.example.myapplication.db.remote.model.DressDetailDto
import com.example.myapplication.db.remote.model.UpdateDressLikeDto
import com.example.myapplication.db.remote.model.order.ClothOptionData
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.ui.storecloth.clothdetail.order.OrderBottomSheetFragment
import com.example.myapplication.ui.storecloth.storedetail.StoreActivity
import com.example.myapplication.viewmodel.DressViewModel
import com.example.myapplication.viewmodel.OrderViewModel


class ClothDetailFragment : BaseFragment<FragmentClothDetailBinding>(R.layout.fragment_cloth_detail) {
    lateinit var dressViewModel: DressViewModel
    lateinit var dressdetailAdapter : ClothDetailAdapter

    private lateinit var orderViewModel: OrderViewModel

    private lateinit var dressimage_viewpager: ViewPager2
    // 픽업하기 버튼 눌렀을 경우 옷 옵션 선택을 위한 옷 정보(가격, 색상, 사이즈 종류 정보) 저장
    private var optiondata : ClothOptionData ?= null
    // 매장 상페 페이지로 넘어갈 경우 매장 상세 페이지로 넘어가기 위한 store_id 저장
    private var store_id : Int = 0
    private var update_islikedata_id : Int ?= 0
    private var is_likedata :Boolean ?= false

    override fun init() {
        initAppbar(binding.clothdetailToolbar, "", true, true)

        dressViewModel = ViewModelProvider(requireActivity()).get(DressViewModel::class.java)
        dressdetailAdapter = ClothDetailAdapter()

        dressViewModel.dress_detail_data.observe(viewLifecycleOwner, Observer<DressDetailDto> { now_dressdetail ->
            if (now_dressdetail != null) {
                binding.clothdetailTextviewStorename.text = now_dressdetail.store_name
                binding.clothdetailTextviewClothname.text = now_dressdetail.dress_name
                binding.clothdetailTextviewClothprice.text = "${now_dressdetail.dress_price}원"
                binding.clothdetailTextviewContents.text = now_dressdetail.comment
                is_likedata = now_dressdetail.is_liked
                if(now_dressdetail.is_liked == false){
                    Glide.with(this)
                        .load(R.drawable.icon_favorite_line) //이미지
                        .into(binding.clothdetailImageviewFavorite) //보여줄 위치
                }else{
                    Glide.with(this)
                        .load(R.drawable.icon_favorite_filledpink) //이미지
                        .into(binding.clothdetailImageviewFavorite) //보여줄 위치
                }

                dressdetailAdapter.submitList(now_dressdetail.dress_image_url_list?.toMutableList())
                // 매장 상페 페이지로 넘어갈 경우 매장 상세 페이지로 넘어가기 위한 store_id 저장
                store_id = now_dressdetail.store_id
                // 픽업하기 버튼 눌렀을 경우 옷 옵션 선택을 위한 옷 정보(가격, 색상, 사이즈 종류 정보) 저장
                optiondata = ClothOptionData(now_dressdetail.dress_price, now_dressdetail.dress_option1, now_dressdetail.dress_option2)
                // 좋아요 버튼을 눌렀을 때 이벤트 처리를 위해
                update_islikedata_id = now_dressdetail.dress_id

            } else {
                Log.d("whatisthis", "dress_detail_data, 데이터 없음")
            }
        })
        //어댑터 설정
        dressimage_viewpager = binding.clothdetailViewpager
        dressimage_viewpager.adapter = dressdetailAdapter
        dressimage_viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        orderViewModel = ViewModelProvider(requireParentFragment()).get(OrderViewModel::class.java)
        //indicator 설정
        val indicator = binding.clothdetailIndicator
        indicator.setViewPager(dressimage_viewpager)

        initButton()
    }

    private fun initButton(){
        binding.apply {
            clothdetailTextviewOrder.setOnClickListener {
                if(optiondata != null){
                    val bottomSheet = OrderBottomSheetFragment()
                    orderViewModel.set_option_data(optiondata!!)
                    bottomSheet.show(parentFragmentManager, bottomSheet.tag)
                }
                orderViewModel.set_order_data(null)
            }

            clothdetailTextviewStorename.setOnClickListener{
                if(store_id!=0){
                    val intent = Intent(getActivity(), StoreActivity::class.java)
                    intent.putExtra("store_id", store_id)
                    startActivity(intent)
                }
            }

            clothdetailImageviewFavorite.setOnClickListener{
                dressViewModel.set_dress_like_data(UpdateDressLikeDto(update_islikedata_id!!))
                if(is_likedata == true){
                    Glide.with(this@ClothDetailFragment)
                        .load(R.drawable.icon_favorite_line) //이미지
                        .into(binding.clothdetailImageviewFavorite) //보여줄 위치
                    is_likedata = false
                }else{
                    Glide.with(this@ClothDetailFragment)
                        .load(R.drawable.icon_favorite_filledpink) //이미지
                        .into(binding.clothdetailImageviewFavorite) //보여줄 위치
                    is_likedata = true
                }
            }

        }
    }

}