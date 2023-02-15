package com.example.myapplication.ui.main.home

import android.service.autofill.UserData
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemHomecardRecyclerBinding
import com.example.myapplication.db.remote.model.DressOverviewDto
import com.example.myapplication.ui.ItemCardClickInterface


//class HomeRecentAdapter(clickInterface: ItemCardClickInterface): RecyclerView.Adapter<HomeRecentAdapter.MyViewHolder>(){
//
//    private var itemList = ArrayList<DressOverviewDto>()
//
//    fun updateData(new_data : ArrayList<DressOverviewDto>){
//        itemList = new_data
//    }
//    fun deleteData(){
//        itemList.clear()
//    }
//
//    private var clicklistener: ItemCardClickInterface = clickInterface
//
//    inner class MyViewHolder(val binding: ItemHomecardRecyclerBinding): RecyclerView.ViewHolder(binding.root) {
//        fun bind(recent_clothes: DressOverviewDto) {
//            if (recent_clothes?.dress_like == false) {
//                //화면에 보여주기
//                Glide.with(this@MyViewHolder.itemView)
//                    .load(R.drawable.icon_favorite_whiteline) //이미지
//                    .into(binding.homecardImageviewFavorite) //보여줄 위치
//            } else {
//                //화면에 보여주기
//                Glide.with(this@MyViewHolder.itemView)
//                    .load(R.drawable.icon_favorite_filledpink) //이미지
//                    .into(binding.homecardImageviewFavorite) //보여줄 위치
//            }
//
//            Glide.with(this.itemView)
//                .load(recent_clothes.dress_default_img) //이미지
//                .into(binding.homecardImageviewImage) //보여줄 위치
//
//            binding.homecardTextviewStorename.text = recent_clothes.store_name
//            binding.homecardTextviewClothename.text = recent_clothes.dress_name
//            binding.homecardTextviewClotheprice.text = recent_clothes.dress_price
//
//            binding.homecardCardviewFrame.setOnClickListener {
//                clicklistener.onItemClothImageClick(
//                    recent_clothes.dress_id,
//                    absoluteAdapterPosition
//                )
//            }
//
//            binding.homecardTextviewStorename.setOnClickListener {
//                clicklistener.onItemStoreNameClick(recent_clothes.store_id, absoluteAdapterPosition)
//            }
//
//            binding.homecardImageviewFavorite.setOnClickListener {
//                if ( recent_clothes?.dress_like!! == true) {
//                    //화면에 보여주기
//                    Glide.with(this@MyViewHolder.itemView)
//                        .load(R.drawable.icon_favorite_whiteline) //이미지
//                        .into(binding.homecardImageviewFavorite) //보여줄 위치
//                    recent_clothes?.dress_like = false
//                } else {
//                    //화면에 보여주기
//                    Glide.with(this@MyViewHolder.itemView)
//                        .load(R.drawable.icon_favorite_filledpink) //이미지
//                        .into(binding.homecardImageviewFavorite) //보여줄 위치
//                    recent_clothes?.dress_like = true
//                }
//                notifyItemChanged(absoluteAdapterPosition)
//
//                clicklistener.onItemClothFavoriteClick(
//                    recent_clothes?.dress_like!!,
//                    recent_clothes.dress_id,
//                    binding.homecardImageviewFavorite,
//                    absoluteAdapterPosition
//                )
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val listItemBinding = ItemHomecardRecyclerBinding.inflate(inflater, parent, false)
//        return MyViewHolder(listItemBinding)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        holder.bind(itemList[position])
//    }
//
//    override fun getItemCount(): Int {
//        return itemList.size
//    }
//}

class HomeRecentAdapter(clicklistener: ItemCardClickInterface) :
   ListAdapter<DressOverviewDto, HomeRecentAdapter.MyViewHolder>(HomeRecentDiffUtil) {

    private var clicklistener: ItemCardClickInterface = clicklistener

    inner class MyViewHolder(val binding: ItemHomecardRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recent_clothes: DressOverviewDto) {
            var temp_favorite : Boolean = recent_clothes?.dress_like!!
            if (temp_favorite== false) {
                //화면에 보여주기
                Glide.with(this@MyViewHolder.itemView)
                    .load(R.drawable.icon_favorite_whiteline) //이미지
                    .into(binding.homecardImageviewFavorite) //보여줄 위치
            } else {
                //화면에 보여주기
                Glide.with(this@MyViewHolder.itemView)
                    .load(R.drawable.icon_favorite_filledpink) //이미지
                    .into(binding.homecardImageviewFavorite) //보여줄 위치
            }

            Glide.with(this.itemView)
                .load(recent_clothes.dress_default_img) //이미지
                .into(binding.homecardImageviewImage) //보여줄 위치

            binding.homecardTextviewStorename.text = recent_clothes.store_name
            binding.homecardTextviewClothename.text = recent_clothes.dress_name
            binding.homecardTextviewClotheprice.text = recent_clothes.dress_price

            binding.homecardCardviewFrame.setOnClickListener {
                clicklistener.onItemClothImageClick(recent_clothes.dress_id , absoluteAdapterPosition)
            }

            binding.homecardTextviewStorename.setOnClickListener {
                clicklistener.onItemStoreNameClick(recent_clothes.store_id, absoluteAdapterPosition)
            }

            binding.homecardImageviewFavorite.setOnClickListener{
                if ( recent_clothes?.dress_like!! == true) {
                    //화면에 보여주기
                    Glide.with(this@MyViewHolder.itemView)
                        .load(R.drawable.icon_favorite_whiteline) //이미지
                        .into(binding.homecardImageviewFavorite) //보여줄 위치
                    recent_clothes?.dress_like = false
                } else {
                    //화면에 보여주기
                    Glide.with(this@MyViewHolder.itemView)
                        .load(R.drawable.icon_favorite_filledpink) //이미지
                        .into(binding.homecardImageviewFavorite) //보여줄 위치
                    recent_clothes?.dress_like = true
                }
                clicklistener.onItemClothFavoriteClick(temp_favorite ,recent_clothes.dress_id, binding.homecardImageviewFavorite, absoluteAdapterPosition)
//                temp_favorite = changeFavoriteImage(this@MyViewHolder.itemView, temp_favorite!!, binding.homecardImageviewFavorite)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemHomecardRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object HomeRecentDiffUtil : DiffUtil.ItemCallback<DressOverviewDto>() {
    override fun areItemsTheSame(oldItem: DressOverviewDto, newItem: DressOverviewDto): Boolean {
        return (oldItem == newItem)
    }

    override fun areContentsTheSame(oldItem: DressOverviewDto, newItem: DressOverviewDto): Boolean {
        return oldItem.dress_id == newItem.dress_id && oldItem.dress_like == newItem.dress_like
    }
}