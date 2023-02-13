package com.example.myapplication.ui.storecloth.storedetail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityStoreBinding
import com.example.myapplication.db.remote.model.StoreDetailDto
import com.example.myapplication.db.remote.model.UpdateStoreLikeDto
import com.example.myapplication.ui.base.BaseActivity
import com.example.myapplication.ui.ItemCardClickInterface
import com.example.myapplication.ui.search.SearchActivity
import com.example.myapplication.ui.storecloth.clothdetail.ClothActivity
import com.example.myapplication.viewmodel.StoreViewModel


//<<<<<<< HEAD
//class StoreActivity : BaseActivity<ActivityStoreBinding>(R.layout.activity_store),
//    ItemCardClickInterface {
//=======
class StoreActivity : BaseActivity<ActivityStoreBinding>(R.layout.activity_store), ItemCardClickInterface {
    var chipGroup = ArrayList<TextView>()
//>>>>>>> main
    lateinit var storeViewModel: StoreViewModel
    lateinit var storedetailAdapter: StoreDetailAdapter
    private lateinit var toolbar: Toolbar
    private var store_like_state: Boolean? = false
    private var storeIdData: Int? = null

    override fun init() {
        // 뷰모델 선언
        storeViewModel = ViewModelProvider(this).get(StoreViewModel::class.java)
        var df : Int = intent.getIntExtra("store_id", 0)
        Log.d("whatisthis",df.toString())
        storeViewModel.get_store_detail_data(df, "전체")

        storedetailAdapter = StoreDetailAdapter(this)

        // 매장 상세 정보 요청
        storeViewModel.store_detail_data.observe(this, Observer<StoreDetailDto> { now_storedetail ->
            if (now_storedetail != null) {
                Glide.with(this)
                    .load(now_storedetail.store_image_url) //이미지
                    .into(binding.storeImageviewImage) //보여줄 위치

                if (now_storedetail.is_liked!!) {
                    Glide.with(this)
                        .load(R.drawable.icon_favorite_filledpink) //이미지
                        .into(binding.storeImageviewFavorite) //보여줄 위치
                } else {
                    Glide.with(this)
                        .load(R.drawable.icon_favorite_line) //이미지
                        .into(binding.storeImageviewFavorite)  //보여줄 위치
                }

                binding.storeTextviewStorename.text = now_storedetail.store_name
                binding.storeTextviewAddress.text = now_storedetail.store_address
                binding.storeTextviewOperationhours.text = now_storedetail.hours_of_operation

                storeIdData = now_storedetail.storeId
                store_like_state = now_storedetail.is_liked

                storedetailAdapter.submitList(now_storedetail.store_dress_list?.toMutableList())
            } else {
                Log.d("whatisthis", "store_detail_data, 데이터 없음")
            }
        })

        // 어댑터 연결
        binding.storeRecyclerview.apply {
            layoutManager = GridLayoutManager(this.context, 2)
            adapter = storedetailAdapter
        }

        binding.storeImageviewFavorite.setOnClickListener {
            // 임시로 이미지만 변경 -> store_detail_data를 전부 다시 요청하면 너무 비효율적
            if (store_like_state == true) {
                Glide.with(this)
                    .load(R.drawable.icon_favorite_line) //이미지
                    .into(binding.storeImageviewFavorite)  //보여줄 위치
                store_like_state = false
            } else {
                Glide.with(this)
                    .load(R.drawable.icon_favorite_filledpink) //이미지
                    .into(binding.storeImageviewFavorite)  //보여줄 위치
                store_like_state = true
            }
            storeViewModel.set_store_like_data(UpdateStoreLikeDto(false, storeIdData!!))

//            // 좋아요 정보 갱신
//            if (storeIdData != null) {
//                storeViewModel.set_store_like_data(UpdateStoreLikeDto(false,storeIdData!!))
//            }
        }

        binding.storeRecyclerview.run {
            val spanCount = 2
            val space = 30
            addItemDecoration(GridSpaceItemDecoration(spanCount, space))
        }
        // 앱바 설정
        initAppbar(R.menu.menu_appbar)
        initChip()
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
                    android.R.id.home -> { // 뒤로가기 버튼을 눌렀을 때
                        finish()
                        true
                    }
                    R.id.search -> {
                        val intent = Intent(this@StoreActivity, SearchActivity::class.java)
//                        intent.putExtra("lat_lng", id)
                        startActivity(intent)
                        true
                    }
                    R.id.notification -> { // 알림 버튼을 눌렀을 때
                        true
                    }
                    else -> false
                }
            }
        })
    }

    override fun onItemClothImageClick(id: Int, position: Int) {
        val intent = Intent(this, ClothActivity::class.java)
        intent.putExtra("cloth_id", id)
        startActivity(intent)
    }

    override fun onItemStoreNameClick(id: Int, position: Int) {
    }

    // 옷 좋아요 클릭 시
    override fun onItemClothFavoriteClick(like: Boolean, id: Int, view : View, position: Int) {
//        if (like) {
//            Glide.with(this)
//                .load(R.drawable.icon_favorite_line) //이미지
//                .into(view as ImageView) //보여줄 위치
//            // 좋아요 정보 갱신 요청
//        }
//        else {
//            Glide.with(this)
//                .load(R.drawable.icon_favorite_filledpink) //이미지
//                .into(view as ImageView)  //보여줄 위치
//            // 좋아요 정보 갱신 요청
//        }
    }

    @SuppressLint("ResourceAsColor")
    private fun initChip() {
        chipGroup.add(binding.chip1)
        chipGroup.add(binding.chip2)
        chipGroup.add(binding.chip3)
        chipGroup.add(binding.chip4)
        chipGroup.add(binding.chip5)
        chipGroup.add(binding.chip6)

        for (i in 0..chipGroup.size - 1) {
            chipGroup[i].setOnClickListener {
                for (j in 0..chipGroup.size - 1) {
                    if (chipGroup[j].background.constantState == resources.getDrawable(R.drawable.chip_background_selected).constantState) {
                        chipGroup[j].setBackgroundResource(R.drawable.chip_background)
                        chipGroup[j].setTextColor(Color.BLACK)
                    }
                    chipGroup[i].setBackgroundResource(R.drawable.chip_background_selected)
                    chipGroup[i].setTextColor(Color.WHITE)
                }
            }
        }
    }

}