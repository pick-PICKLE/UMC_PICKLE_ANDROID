package com.example.myapplication.ui.main.favorite.item

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemCardRecyclerBinding
import com.example.myapplication.db.remote.model.DressLikeDto
import com.example.myapplication.ui.ItemCardClickInterface

class FavoriteItemAdapter(clicklistener: ItemCardClickInterface) :
    ListAdapter<DressLikeDto, FavoriteItemAdapter.MyViewHolder>(FavoriteItemDiffUtil) {

    var clicklistener: ItemCardClickInterface = clicklistener

    inner class MyViewHolder(val binding: ItemCardRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(like_cloth: DressLikeDto) {
            Glide.with(this@MyViewHolder.itemView)
                .load(R.drawable.icon_favorite_filledpink) //이미지
                .into(binding.cardImageviewFavorite) //보여줄 위치

            Glide.with(this.itemView)
                .load(like_cloth.image) //이미지
                .into(binding.cardImageviewImage) //보여줄 위치

            binding.cardTextviewStorename.text = like_cloth.store_name
            binding.cardTextviewClothename.text = like_cloth.dress_name
            binding.cardTextviewClotheprice.text = like_cloth.price

            binding.cardCardviewFrame.setOnClickListener {
                clicklistener.onItemClothImageClick(like_cloth.dress_id, absoluteAdapterPosition)
            }

            binding.cardImageviewFavorite.setOnClickListener {
                clicklistener.onItemClothFavoriteClick(
                    true,
                    like_cloth.dress_id,
                    this.itemView,
                    absoluteAdapterPosition
                )
            }
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

object FavoriteItemDiffUtil : DiffUtil.ItemCallback<DressLikeDto>() {
    override fun areItemsTheSame(oldItem: DressLikeDto, newItem: DressLikeDto): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: DressLikeDto, newItem: DressLikeDto): Boolean {
        return oldItem.dress_id == newItem.dress_id
    }
}