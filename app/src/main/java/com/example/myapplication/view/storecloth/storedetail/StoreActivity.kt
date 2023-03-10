package com.example.myapplication.view.storecloth.storedetail

import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityStoreBinding
import com.example.myapplication.data.remote.model.UpdateDressLikeDto
import com.example.myapplication.data.remote.model.UpdateStoreLikeDto
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.repository.DressRepository
import com.example.myapplication.repository.StoreRepository
import com.example.myapplication.view.search.SearchActivity
import com.example.myapplication.view.storecloth.clothdetail.ClothActivity
import com.example.myapplication.viewmodel.*
import com.example.myapplication.viewmodel.factory.StoreViewModelFactory
import com.example.myapplication.widget.utils.EventObserver
import com.example.myapplication.widget.utils.ItemCardClickInterface
import com.example.myapplication.widget.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreActivity : BaseActivity<ActivityStoreBinding>(R.layout.activity_store),
    ItemCardClickInterface {
    val homeViewModel: HomeViewModel by viewModels<HomeViewModel>()
    val storeViewModel: StoreViewModel by viewModels<StoreViewModel>()
    val dressViewModel: DressViewModel by viewModels<DressViewModel>()
    private lateinit var optionViewModel: OptionViewModel

    private lateinit var storedetailAdapter: StoreDetailAdapter
    private var store_id : Int ?=null

    private lateinit var toolbar: Toolbar
    private var store_like_state: Boolean? = false
    private var storeIdData: Int? = null
    private var latlng: Pair<Double, Double> = Pair(37.5581, 126.9260)
    private var buttonClick : Boolean = false

    override fun init() {
        // ????????? ??????
        optionViewModel = ViewModelProvider(this).get(OptionViewModel::class.java)

        binding.clothkindvm = optionViewModel
        store_id = intent.getIntExtra("store_id", 0)
        storeViewModel.get_store_detail_data(store_id!!, binding.chip1.text.toString())

        // lat, lng ????????? ?????? ?????????
        getLocation()
        homeViewModel.set_home_latlng(lat_lng!!)
        homeViewModel.home_latlng.observe(this, Observer {
            latlng = it
        })

        storedetailAdapter = StoreDetailAdapter(this)

        // ?????? ?????? ?????? ??????
        storeViewModel.store_detail_data.observe(this, Observer {
            when (it) {
                is NetworkResult.Loading -> {
                }
                is NetworkResult.Error -> {
                    Log.d("whatisthis", "StoreActivity : ???????????????")
                }
                is NetworkResult.Success -> {
                    Glide.with(this)
                        .load(it.data!!.store_image_url) //?????????
                        .into(binding.storeImageviewImage) //????????? ??????

                    if (it.data!!.is_liked!!) {
                        Glide.with(this)
                            .load(R.drawable.icon_favorite_filledpink) //?????????
                            .into(binding.storeImageviewFavorite) //????????? ??????
                    } else {
                        Glide.with(this)
                            .load(R.drawable.icon_favorite_line) //?????????
                            .into(binding.storeImageviewFavorite)  //????????? ??????
                    }

                    binding.storeTextviewStorename.text = it.data!!.store_name
                    binding.storeTextviewAddress.text = it.data!!.store_address
                    binding.storeTextviewOperationhours.text = it.data!!.hours_of_operation

                    storeIdData = it.data!!.storeId
                    store_like_state = it.data!!.is_liked

                    storedetailAdapter.submitList(it.data!!.store_dress_list?.toMutableList())
                }
            }
        })

        // ????????? ??????
        binding.storeRecyclerview.apply {
            layoutManager = GridLayoutManager(this.context, 2)
            adapter = storedetailAdapter
        }

        binding.storeImageviewFavorite.setOnClickListener {
            // ????????? ???????????? ?????? -> store_detail_data??? ?????? ?????? ???????????? ?????? ????????????
            if (store_like_state == true) {
                Glide.with(this)
                    .load(R.drawable.icon_favorite_line) //?????????
                    .into(binding.storeImageviewFavorite)  //????????? ??????
                store_like_state = false
            } else {
                Glide.with(this)
                    .load(R.drawable.icon_favorite_filledpink) //?????????
                    .into(binding.storeImageviewFavorite)  //????????? ??????
                store_like_state = true
            }
            storeViewModel.set_store_like_data(UpdateStoreLikeDto(false, storeIdData!!))
        }

        binding.storeRecyclerview.run {
            val spanCount = 2
            val space = 30
            addItemDecoration(GridSpaceItemDecoration(spanCount, space))
        }
        // ?????? ??????
        initAppbar(R.menu.menu_appbar)
        initChip()

    }

    override fun onResume() {
        super.onResume()
        if(buttonClick){
            storeViewModel.get_store_detail_data(store_id!!, binding.chip1.text.toString())
            buttonClick = false
        }
    }

    private fun initAppbar(menuRes: Int) {
        toolbar = binding.toolbar.toolbarToolbar

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(menuRes, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> { // ???????????? ????????? ????????? ???
                        finish()
                        true
                    }
                    R.id.search -> {
                        val intent = Intent(this@StoreActivity, SearchActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.notification -> { // ?????? ????????? ????????? ???
                        true
                    }
                    else -> false
                }
            }
        })
    }

    override fun onItemClothImageClick(id: Int, position: Int) {
        buttonClick = true
        val intent = Intent(this, ClothActivity::class.java)
        intent.putExtra("cloth_id", id)
        startActivity(intent)
    }

    override fun onItemStoreNameClick(id: Int, position: Int) {
    }

    // ??? ????????? ?????? ???
    override fun onItemClothFavoriteClick(like: Boolean, id: Int, view: View, position: Int) {
        if (id != 0) {
            dressViewModel.set_dress_like_data(UpdateDressLikeDto(id))
        }
    }

    private fun initChip() {
        optionViewModel.apply {
            clothkind_bt_event.observe(this@StoreActivity, EventObserver {
                if(clothkind_data.value==null){
                    optionViewModel.set_clothkind_data(binding.chip1) // ?????????
                }
                it as TextView

                if(it!=clothkind_data.value!!){
                    clothkind_data.value!!.setTextColor(ContextCompat.getColor(it.context, R.color.unselected_storeoption_text))
                    clothkind_data.value!!.setBackgroundResource(R.drawable.chip_background)
                    it.setTextColor(ContextCompat.getColor(it.context, R.color.selected_storeoption_text))
                    it.setBackgroundResource(R.drawable.chip_background_selected)
                }
                optionViewModel.set_clothkind_data(it)

                storeViewModel.get_store_detail_data(store_id!!, it.text.toString())
            })
        }
    }
}