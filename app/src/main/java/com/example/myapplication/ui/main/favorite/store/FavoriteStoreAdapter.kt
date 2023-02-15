package com.example.myapplication.ui.main.favorite.store

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemAroundRecyclerBinding
import com.example.myapplication.db.remote.model.StoreLikeDto
import com.example.myapplication.ui.ItemListClickInterface
import kotlin.math.abs

class FavoriteStoreAdapter(clicklistener: ItemListClickInterface) :
    ListAdapter<StoreLikeDto, FavoriteStoreAdapter.MyViewHolder>(FavoriteStoreDiffUtil) {

    var clicklistener: ItemListClickInterface = clicklistener

    inner class MyViewHolder(val binding: ItemAroundRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: StoreLikeDto) {
            with(binding) {
                Glide.with(this@MyViewHolder.itemView)
                    .load(R.drawable.icon_favorite_filledpink) //이미지
                    .into(marketFavorite) //보여줄 위치

                Glide.with(this@MyViewHolder.itemView)
                    .load(user.imageUrl) //이미지
                    .into(marketImage) //보여줄 위치

                marketName.text = user?.name.toString()
                marketAddress.text = user?.address.toString()
                marketOperationhours.text = "${user?.store_open_day}, ${user?.hours_of_operation}"

                marketLayout.setOnClickListener {
                    clicklistener.onItemMarketLayoutClick(user.store_id, absoluteAdapterPosition)
                }
                marketFavorite.setOnClickListener {
                    clicklistener.onItemMarketFavoriteClick(true, user.store_id, it, absoluteAdapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemAroundRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object FavoriteStoreDiffUtil : DiffUtil.ItemCallback<StoreLikeDto>() {
    override fun areItemsTheSame(oldItem: StoreLikeDto, newItem: StoreLikeDto): Boolean {
        return oldItem  == newItem
    }

    override fun areContentsTheSame(oldItem: StoreLikeDto, newItem: StoreLikeDto): Boolean {
        return (oldItem.store_id == newItem.store_id)
    }
}