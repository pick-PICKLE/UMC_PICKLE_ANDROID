package com.example.myapplication.ui.main.home

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.db.remote.model.*
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.ui.ItemCardClickInterface
import com.example.myapplication.ui.main.home.newclothe.NewFragment
import com.example.myapplication.ui.main.home.recent.HomeRecommendAdapter
import com.example.myapplication.ui.main.home.recent.RecentFragment
import com.example.myapplication.ui.storecloth.clothdetail.ClothActivity
import com.example.myapplication.ui.storecloth.storedetail.StoreActivity
import com.example.myapplication.viewmodel.DressViewModel
import com.example.myapplication.viewmodel.HomeViewModel
import com.example.myapplication.viewmodel.StoreViewModel
import com.smarteist.autoimageslider.SliderView

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home),
    ItemCardClickInterface {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var dressViewModel: DressViewModel
    private lateinit var storeViewModel: StoreViewModel

    private lateinit var recentAdapter: HomeRecentAdapter
    private lateinit var newAdapter: HomeNewAdapter
    private lateinit var recommendAdapter: HomeRecommendAdapter

    private lateinit var imageList: ArrayList<Int>
    private var recentData = ArrayList<DressOverviewDto>()
    private var newData = ArrayList<DressOverviewDto>()
    private var recommendData = ArrayList<DressOverviewDto>()
    private var update_islikedata_id: Int? = null
    private var update_list_position: Int? = null

    override fun init() {
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        dressViewModel = ViewModelProvider(requireActivity()).get(DressViewModel::class.java)
        storeViewModel = ViewModelProvider(requireActivity()).get(StoreViewModel::class.java)

        initAppbar(binding.homeToolbar, "홈", false, true)
        initSlideView()
        initRecyclerView()

        binding.homeTextviewRecentmore.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .add(R.id.home_base_layout, RecentFragment(), "recent")
                .addToBackStack(null)
                .commitAllowingStateLoss()
            homeViewModel.get_home_recent_data()
        }

        binding.homeTextviewNewmore.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.home_base_layout, NewFragment(), "new")
                .addToBackStack(null)
                .commitAllowingStateLoss()
            homeViewModel.get_home_new_data(homeViewModel.home_latlng.value!!.first,homeViewModel.home_latlng.value!!.second)
        }

//        dressViewModel.dress_like_data.observe(viewLifecycleOwner, Observer<List<DressLikeDto>> {
////            recentAdapter.notifyDataSetChanged()
////
////            newAdapter.notifyDataSetChanged()
////
////            recommendAdapter.notifyDataSetChanged()
//
////            if (update_list_position != null) {
////
////                if ((recentData.size >= update_list_position!! +1) &&recentData[update_list_position!!].dress_id == update_islikedata_id) {
////                    recentData[update_list_position!!].dress_like = !recentData[update_list_position!!].dress_like!!
//////                    recentAdapter.submitList(recentData.toMutableList())
//////                    recentAdapter.updateData(recentData)
////                    recentAdapter.notifyItemChanged(update_list_position!!)
////                } else if ((newData.size >= update_list_position!! +1) && newData[update_list_position!!].dress_id == update_islikedata_id) {
////                    newData[update_list_position!!].dress_like = !newData[update_list_position!!].dress_like!!
//////                    newAdapter.submitList(newData.toMutableList())
////                    newAdapter.notifyItemChanged(update_list_position!!)
////                } else if((recommendData.size >= update_list_position!! +1) && recommendData[update_list_position!!].dress_id == update_islikedata_id){
////                    recommendData[update_list_position!!].dress_like = !recommendData[update_list_position!!].dress_like!!
//////                    recommendAdapter.submitList(recommendData.toMutableList())
////                    recommendAdapter.notifyItemChanged(update_list_position!!)
////                }
////                update_list_position = null
////            }
//        })
    }


    private fun initSlideView() {
        imageList = ArrayList()
        imageList.add(R.drawable.slider_home1)
        imageList.add(R.drawable.slider_home2)
        imageList.add(R.drawable.slider_home3)
        imageList.add(R.drawable.slider_home4)
        imageList.add(R.drawable.slider_home5)
        lateinit var sliderView: SliderView
        sliderView = binding.slider
        lateinit var sliderAdapter: SliderAdapter
        sliderAdapter = SliderAdapter(imageList)
        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
        sliderView.setSliderAdapter(sliderAdapter)
        sliderView.scrollTimeInSec = 3
        sliderView.isAutoCycle = true
        sliderView.startAutoCycle()
    }

    private fun initRecyclerView() {
        recentAdapter = HomeRecentAdapter(this@HomeFragment)
        newAdapter = HomeNewAdapter(this@HomeFragment)
        recommendAdapter = HomeRecommendAdapter(this@HomeFragment)

        homeViewModel.home_data.observe(
            viewLifecycleOwner,
            Observer<DressHomeDto> { now_homeModel ->
                if (now_homeModel != null) {
//                    newAdapter.submitList(null)
//                    recommendAdapter.submitList(null)

                    recentAdapter.submitList(now_homeModel.recentView!!.toMutableList())
//                    recentAdapter.notifyDataSetChanged()
                    newAdapter.submitList(now_homeModel.newDresses!!.toMutableList())
//                    newAdapter.notifyDataSetChanged()
                    recommendAdapter.submitList(now_homeModel.recDresses!!.toMutableList())
//                    recommendAdapter.notifyDataSetChanged()


//                    recentData = now_homeModel.recentView as ArrayList<DressOverviewDto>
//                    newData = now_homeModel.newDresses as ArrayList<DressOverviewDto>
//                    recommendData = now_homeModel.recDresses as ArrayList<DressOverviewDto>

//                    recentAdapter.deleteData()
//                    recentAdapter.updateData(now_homeModel.recentView as ArrayList<DressOverviewDto>)
//                    recentAdapter.notifyDataSetChanged()

//                    newAdapter.deleteData()
//                    newAdapter.updateData(now_homeModel.newDresses as ArrayList<DressOverviewDto>)
//                    newAdapter.notifyDataSetChanged()

//                    recommendAdapter.deleteData()
//                    recommendAdapter.updateData(now_homeModel.recDresses as ArrayList<DressOverviewDto>)
//                    recommendAdapter.notifyDataSetChanged()

                } else {
                    Log.d("whatisthis", "home_data, 없음")
//                    recentAdapter.deleteData()
//                    newAdapter.deleteData()
//                    recommendAdapter.deleteData()

                    recentAdapter.submitList(null)
                    newAdapter.submitList(null)
                    recommendAdapter.submitList(null)
                }
            })

        binding.homeRecyclerviewRecent.apply {
            layoutManager =
                LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recentAdapter
        }

        binding.homeRecyclerviewNew.apply {
            layoutManager =
                LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = newAdapter
        }

        binding.homeRecyclerviewRecommend.apply {
            layoutManager = GridLayoutManager(this.context, 2)
            adapter = recommendAdapter
        }

    }

    override fun onItemClothImageClick(id: Int, position: Int) {
        val intent = Intent(getActivity(), ClothActivity::class.java)
        intent.putExtra("cloth_id", id)
        startActivity(intent)
    }

    override fun onItemStoreNameClick(id: Int, position: Int) {
        val intent = Intent(getActivity(), StoreActivity::class.java)
        intent.putExtra("store_id", id)
        startActivity(intent)
    }

    override fun onItemClothFavoriteClick(like: Boolean, id: Int, view: View, position: Int) {
        if (id != 0) {
            dressViewModel.set_dress_like_data(UpdateDressLikeDto(id))
            dressViewModel.get_dress_like_data()

//            homeViewModel.get_home_data(
//                homeViewModel.home_latlng.value!!.first,
//                homeViewModel.home_latlng.value!!.second)
//            update_islikedata_id = id
//            update_list_position = position
        }
    }


}