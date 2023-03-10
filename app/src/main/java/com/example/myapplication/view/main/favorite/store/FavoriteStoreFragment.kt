package com.example.myapplication.view.main.favorite.store


import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFavoriteStoreBinding
import com.example.myapplication.data.remote.model.StoreLikeDto
import com.example.myapplication.data.remote.model.UpdateStoreLikeDto
import com.example.myapplication.widget.utils.ItemListClickInterface
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.view.main.profile.orderstatus.OrderListDivider
import com.example.myapplication.view.storecloth.storedetail.StoreActivity
import com.example.myapplication.viewmodel.HomeViewModel
import com.example.myapplication.viewmodel.StoreViewModel
import com.example.myapplication.widget.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteStoreFragment :
    BaseFragment<FragmentFavoriteStoreBinding>(R.layout.fragment_favorite_store),
    ItemListClickInterface {

    val homeViewModel: HomeViewModel by activityViewModels<HomeViewModel>()
    val storeViewModel: StoreViewModel by activityViewModels<StoreViewModel>()

    lateinit var favoritestoredapter: FavoriteStoreAdapter

    override fun init() {
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        storeViewModel.get_store_like_data()
    }

    private fun initRecyclerView() {
        with(binding) {
//             1. 어댑터 생성 및 리사이클러뷰 연결
            favoritestoredapter =
                FavoriteStoreAdapter(this@FavoriteStoreFragment)

            storeViewModel.store_like_data.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is NetworkResult.Loading -> {
                    }

                    is NetworkResult.Error -> {
                        favoritestoredapter.submitList(null)
                        Log.d("whatisthis", "_store_like_data, 데이터 없음")
                    }

                    is NetworkResult.Success -> {
                        favoritestoredapter.submitList(it.data!!.toMutableList())
                    }
                }

            })

            binding.favoriteStoreRecyclerview.adapter = favoritestoredapter
            binding.favoriteStoreRecyclerview.layoutManager = LinearLayoutManager(context)
            binding.favoriteStoreRecyclerview.addItemDecoration(
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
//        storelikedata.removeAt(position)
        storeViewModel.set_store_like_data(UpdateStoreLikeDto(false, id))
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