package com.example.myapplication.ui.main.profile.orderstatus.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemOrderstatusdetailRecyclerBinding
import com.example.myapplication.db.remote.model.DressOrderDto
import com.example.myapplication.db.remote.model.ReservedDressDto

class OrderstatusDetailAdapter() : RecyclerView.Adapter<OrderstatusDetailAdapter.ViewHolder>() {
//
    var userList = ArrayList<ReservedDressDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemOrderstatusdetailRecyclerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList?.get(position)

        holder.setUser(user)
    }

    override fun getItemCount(): Int {
        return userList?.size ?: 0
    }

    inner class ViewHolder(val binding: ItemOrderstatusdetailRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setUser(user: ReservedDressDto?) {
            with(binding) {
                Glide.with(this@ViewHolder.itemView)
                    .load(user?.dress_imgurl)
                    .into(orderstatusdetailImageProduct)
                orderstatusdetailTextviewClothename.text =user?.dress_name
                orderstatusdetailTextviewClothecolorsize.text = user?.dress_option_detail
                orderstatusdetailTextviewClotheprice.text = user?.select_price
            }
        }
    }
}