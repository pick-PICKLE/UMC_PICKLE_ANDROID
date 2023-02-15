package com.example.myapplication.ui.main.favorite.store


import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFavoriteStoreBinding
import com.example.myapplication.db.remote.model.StoreLikeDto
import com.example.myapplication.db.remote.model.UpdateDressLikeDto
import com.example.myapplication.db.remote.model.UpdateStoreLikeDto
import com.example.myapplication.ui.ItemListClickInterface
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.ui.main.profile.orderstatus.OrderListDivider
import com.example.myapplication.ui.storecloth.storedetail.StoreActivity
import com.example.myapplication.viewmodel.HomeViewModel
import com.example.myapplication.viewmodel.StoreViewModel
import kotlinx.android.synthetic.main.fragment_favorite_store.*


class FavoriteStoreFragment :
    BaseFragment<FragmentFavoriteStoreBinding>(R.layout.fragment_favorite_store),
    ItemListClickInterface {

    lateinit var homeViewModel:HomeViewModel
    lateinit var storeViewModel: StoreViewModel
    lateinit var favoritestoredapter: FavoriteStoreAdapter
    lateinit var storelikedata : ArrayList<StoreLikeDto>


    override fun init() {
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        storeViewModel = ViewModelProvider(requireActivity()).get(StoreViewModel::class.java)
        storelikedata = ArrayList<StoreLikeDto>()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        with(binding) {
//             1. 어댑터 생성 및 리사이클러뷰 연결
            favoritestoredapter =
                FavoriteStoreAdapter(this@FavoriteStoreFragment)

            storeViewModel.store_like_data.observe(
                viewLifecycleOwner,
                Observer<List<StoreLikeDto>> { now_storelikedata ->
                    if(now_storelikedata!=null){
                        favoritestoredapter.submitList(now_storelikedata.toMutableList())
                        storelikedata = now_storelikedata as ArrayList<StoreLikeDto>
                    }else{
                        favoritestoredapter.submitList(null)
                        Log.d("whatisthis", "_store_like_data, 데이터 없음")
                    }
                })

            storeViewModel.update_store_like_data.observe(
                viewLifecycleOwner,
                Observer<UpdateStoreLikeDto>{ update_storelikedata ->
                    if(update_storelikedata!=null){
                        favoritestoredapter.submitList(storelikedata.toMutableList())
                    }else{
                        Log.d("whatisthis", "update_store_like_data, 데이터 없음")
                    }
                }
            )

            favorite_store_recyclerview.adapter = favoritestoredapter
            favorite_store_recyclerview.layoutManager = LinearLayoutManager(context)
            favorite_store_recyclerview.addItemDecoration(
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

    override fun onItemMarketFavoriteClick(like: Boolean, id: Int, view: View, position: Int) {
        storelikedata.removeAt(position)
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

}