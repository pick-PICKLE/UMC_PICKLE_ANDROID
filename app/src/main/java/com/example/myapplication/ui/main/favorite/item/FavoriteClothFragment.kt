package com.example.myapplication.ui.main.favorite.item

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentFavoriteItemBinding
import com.example.myapplication.db.remote.model.DressLikeDto
import com.example.myapplication.db.remote.model.UpdateDressLikeDto
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.ui.ItemCardClickInterface
import com.example.myapplication.ui.storecloth.clothdetail.ClothActivity
import com.example.myapplication.ui.storecloth.storedetail.StoreActivity
import com.example.myapplication.viewmodel.DressViewModel
import com.example.myapplication.viewmodel.HomeViewModel


class FavoriteClothFragment :
    BaseFragment<FragmentFavoriteItemBinding>(R.layout.fragment_favorite_item),
    ItemCardClickInterface {

    lateinit var homeViewModel:HomeViewModel
    lateinit var dressViewModel: DressViewModel
    lateinit var fragmentadapter: FavoriteItemAdapter

    override fun init() {
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        dressViewModel = ViewModelProvider(requireActivity()).get(DressViewModel::class.java)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        fragmentadapter = FavoriteItemAdapter(this)

        dressViewModel.dress_like_data.observe(viewLifecycleOwner, Observer<List<DressLikeDto>> { dresslikedata ->
            if(dresslikedata!=null){
                fragmentadapter.submitList(dresslikedata.toMutableList())
            }else{
                fragmentadapter.submitList(null)
            }
        })

        binding.favoriteItemRecyclerView.apply {
            layoutManager = GridLayoutManager(this.context, 2)
            adapter = fragmentadapter
        }
    }

    override fun onItemClothImageClick(id: Int, position: Int) {
        val intent = Intent(context, ClothActivity::class.java)
        intent.putExtra("cloth_id", id)
        startActivity(intent)
    }

    override fun onItemStoreNameClick(id: Int, position: Int) {
        val intent = Intent(context, StoreActivity::class.java)
        intent.putExtra("store_id", id)
        startActivity(intent)
    }


    override fun onItemClothFavoriteClick(like:Boolean, id: Int, view : View, position: Int) {
        dressViewModel.set_dress_like_data(UpdateDressLikeDto(id))
        homeViewModel.get_home_data(
            homeViewModel.home_latlng.value!!.first,
            homeViewModel.home_latlng.value!!.second)
        dressViewModel.get_dress_like_data()

    }

}