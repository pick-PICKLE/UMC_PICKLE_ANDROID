package com.example.myapplication.ui.storecloth.clothdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemClothdetailViewpagerBinding
import com.example.myapplication.db.remote.model.order.ClothOrderData

class ClothDetailAdapter() :
    ListAdapter<String, ClothDetailAdapter.MyViewHolder>(DressDetailDiffUtil) {

    inner class MyViewHolder(val binding: ItemClothdetailViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dress_image_url: String) {
            Glide.with(this.itemView)
                .load(dress_image_url) //이미지
                .into(binding.clothdetailItemImageview) //보여줄 위치
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemClothdetailViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object DressDetailDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem.hashCode()  == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}