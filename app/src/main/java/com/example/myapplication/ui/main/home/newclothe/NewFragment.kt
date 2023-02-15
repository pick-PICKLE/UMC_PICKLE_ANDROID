package com.example.myapplication.ui.main.home.newclothe

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentNewBinding
import com.example.myapplication.db.remote.model.DressHomeDto
import com.example.myapplication.db.remote.model.DressLikeDto
import com.example.myapplication.db.remote.model.DressOverviewDto
import com.example.myapplication.db.remote.model.UpdateDressLikeDto
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.ui.ItemCardClickInterface
import com.example.myapplication.ui.main.home.recent.HomeRecommendAdapter
import com.example.myapplication.ui.storecloth.clothdetail.ClothActivity
import com.example.myapplication.ui.storecloth.storedetail.StoreActivity
import com.example.myapplication.viewmodel.DressViewModel
import com.example.myapplication.viewmodel.HomeViewModel


class NewFragment : BaseFragment<FragmentNewBinding>(R.layout.fragment_new),
    ItemCardClickInterface {
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var dressViewModel: DressViewModel
    private lateinit var fragmentadapter: HomeRecommendAdapter

    private var update_islikedata_id: Int? = null
    private var update_list_position: Int? = null
    private var newData = ArrayList<DressOverviewDto>()

    override fun init() {
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        dressViewModel = ViewModelProvider(requireActivity()).get(DressViewModel::class.java)

        initAppbar(binding.newToolbar, "NEW", true, false)
        initRecyclerView()

//        dressViewModel.dress_like_data.observe(viewLifecycleOwner, Observer<List<DressLikeDto>> {
////            if (update_list_position != null) {
////                newData[update_list_position!!].dress_like =
////                    !newData[update_list_position!!].dress_like!!
//////                    newAdapter.submitList(newData.toMutableList())
////                fragmentadapter.notifyItemChanged(update_list_position!!)
////
////                update_list_position = null
////            }
//        })
    }

    private fun initRecyclerView() {
        fragmentadapter = HomeRecommendAdapter(this@NewFragment)

        homeViewModel.home_new_data.observe(
            viewLifecycleOwner,
            Observer<List<DressOverviewDto>> { now_home_newdata ->
                if (now_home_newdata != null) {
                    newData = now_home_newdata as ArrayList<DressOverviewDto>
                    fragmentadapter.submitList(now_home_newdata)
//                    fragmentadapter.deleteData()
//                    fragmentadapter.updateData(now_home_newdata as ArrayList<DressOverviewDto>)
//                    fragmentadapter.notifyDataSetChanged()
                } else {
                    Log.d("whatisthis", "now_home_newdata, 없음")
//                    fragmentadapter.deleteData()
                    fragmentadapter.submitList(null)
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