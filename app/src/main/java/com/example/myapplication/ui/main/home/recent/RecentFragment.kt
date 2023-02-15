package com.example.myapplication.ui.main.home.recent

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentRecentBinding
import com.example.myapplication.db.remote.model.DressHomeDto
import com.example.myapplication.db.remote.model.DressLikeDto
import com.example.myapplication.db.remote.model.DressOverviewDto
import com.example.myapplication.db.remote.model.UpdateDressLikeDto
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.ui.ItemCardClickInterface
import com.example.myapplication.ui.storecloth.clothdetail.ClothActivity
import com.example.myapplication.ui.storecloth.storedetail.StoreActivity
import com.example.myapplication.viewmodel.DressViewModel
import com.example.myapplication.viewmodel.HomeViewModel

class RecentFragment : BaseFragment<FragmentRecentBinding>(R.layout.fragment_recent),
    ItemCardClickInterface {
    lateinit var homeViewModel: HomeViewModel
    private lateinit var dressViewModel: DressViewModel
    lateinit var fragmentadapter: HomeRecommendAdapter

    private var update_islikedata_id: Int? = null
    private var update_list_position: Int? = null
    private var recentData = ArrayList<DressOverviewDto>()

    override fun init() {
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        dressViewModel = ViewModelProvider(requireActivity()).get(DressViewModel::class.java)

        initAppbar(binding.recentToolbar, "최근 본 상품", true, false)
        initRecyclerView()

//        dressViewModel.dress_like_data.observe(viewLifecycleOwner, Observer<List<DressLikeDto>> {
////            if (update_list_position != null) {
////                recentData[update_list_position!!].dress_like =
////                    !recentData[update_list_position!!].dress_like!!
//////                    recentAdapter.submitList(recentData.toMutableList())
//////                    recentAdapter.updateData(recentData)
////                fragmentadapter.notifyItemChanged(update_list_position!!)
////
////                update_list_position = null
////            }
//        })
    }

    private fun initRecyclerView() {
        fragmentadapter = HomeRecommendAdapter(this@RecentFragment)

        homeViewModel.home_recent_data.observe(
            viewLifecycleOwner,
            Observer<List<DressOverviewDto>> { now_home_recentdata ->
                if (now_home_recentdata != null) {
                    recentData = now_home_recentdata as ArrayList<DressOverviewDto>
                    fragmentadapter.submitList(now_home_recentdata)

//                    fragmentadapter.deleteData()
//                    fragmentadapter.updateData(now_home_recentdata as ArrayList<DressOverviewDto>)
//                    fragmentadapter.notifyDataSetChanged()
                } else {
                    Log.d("whatisthis", "now_home_recentdata, 없음")
                    fragmentadapter.submitList(null)

//                    fragmentadapter.deleteData()
                }
            })

        binding.newRecyclerView.apply {
            layoutManager = GridLayoutManager(this.context, 2)
            adapter = fragmentadapter
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

            homeViewModel.get_home_data(
                homeViewModel.home_latlng.value!!.first,
                homeViewModel.home_latlng.value!!.second)

//            update_islikedata_id = id
//            update_list_position = position
        }
    }

}