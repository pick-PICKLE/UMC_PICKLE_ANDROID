package com.example.myapplication.ui.main.home.recent

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemCardRecyclerBinding
import com.example.myapplication.db.remote.model.DressOverviewDto
import com.example.myapplication.ui.ItemCardClickInterface

class HomeRecommendAdapter(clicklistener: ItemCardClickInterface) :
    ListAdapter<DressOverviewDto, HomeRecommendAdapter.MyViewHolder>(HomeRecommendDiffUtil) {

    var clicklistener: ItemCardClickInterface = clicklistener

    inner class MyViewHolder(val binding: ItemCardRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recommend_clothes: DressOverviewDto) {
            var temp_favorite : Boolean = recommend_clothes?.dress_like!!

            if (temp_favorite == false) {
                //화면에 보여주기
                Glide.with(this@MyViewHolder.itemView)
                    .load(R.drawable.icon_favorite_whiteline) //이미지
                    .into(binding.cardImageviewFavorite) //보여줄 위치
            } else {
                //화면에 보여주기
                Glide.with(this@MyViewHolder.itemView)
                    .load(R.drawable.icon_favorite_filledpink) //이미지
                    .into(binding.cardImageviewFavorite) //보여줄 위치
            }

            Glide.with(this.itemView)
                .load(recommend_clothes.dress_default_img) //이미지
                .into(binding.cardImageviewImage) //보여줄 위치

            binding.cardTextviewStorename.text = recommend_clothes.store_name
            binding.cardTextviewClothename.text = recommend_clothes.dress_name
            binding.cardTextviewClotheprice.text = recommend_clothes.dress_price

            binding.cardCardviewFrame.setOnClickListener {
                clicklistener.onItemClothImageClick(recommend_clothes.dress_id, absoluteAdapterPosition)
            }

            binding.cardTextviewStorename.setOnClickListener {
                clicklistener.onItemStoreNameClick(recommend_clothes.store_id, absoluteAdapterPosition)
            }

            binding.cardImageviewFavorite.setOnClickListener {
                clicklistener.onItemClothFavoriteClick(temp_favorite, recommend_clothes.dress_id, binding.cardImageviewFavorite, absoluteAdapterPosition)
//                changeFavoriteImage(this@MyViewHolder.itemView, recommend_clothes.dress_like!!, binding.cardImageviewFavorite)
            }
        }
    }

    private fun changeFavoriteImage(view: View, like:Boolean, imageview: ImageView){
        if (like == false) {
            //화면에 보여주기
            Glide.with(view)
                .load(R.drawable.icon_favorite_whiteline) //이미지
                .into(imageview) //보여줄 위치
        } else {
            //화면에 보여주기
            Glide.with(view)
                .load(R.drawable.icon_favorite_filledpink) //이미지
                .into(imageview) //보여줄 위치
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemCardRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object HomeRecommendDiffUtil : DiffUtil.ItemCallback<DressOverviewDto>() {
    override fun areItemsTheSame(oldItem: DressOverviewDto, newItem: DressOverviewDto): Boolean {
        return (oldItem.dress_id == newItem.dress_id && oldItem.dress_like == newItem.dress_like)
    }

    override fun areContentsTheSame(oldItem: DressOverviewDto, newItem: DressOverviewDto): Boolean {
        return oldItem == newItem
    }
}