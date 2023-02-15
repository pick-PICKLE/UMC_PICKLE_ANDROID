package com.example.myapplication.ui.main.profile.orderstatus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemOrderstatusRecyclerBinding
import com.example.myapplication.db.remote.model.DressOrderListDto

class OrderstatusAdapter(clicklistener: OrderstatusAdapter.OrderstatusClickListener) :
    RecyclerView.Adapter<OrderstatusAdapter.ViewHolder>() {

    var userList = ArrayList<DressOrderListDto>()

    interface OrderstatusClickListener {

        fun onItemImageClick(view: View, position: Int)
        fun onItemDetailClick(reservationid: Int, position: Int)
        fun onItemInnerlayoutClick(reservationid: Int, position: Int)
    }

    var clicklistener: OrderstatusAdapter.OrderstatusClickListener = clicklistener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemOrderstatusRecyclerBinding.inflate(
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

    inner class ViewHolder(val binding: ItemOrderstatusRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setUser(user: DressOrderListDto?) {
            with(binding) {
                orderstatusInnerlayout.setOnClickListener {
                    clicklistener.onItemInnerlayoutClick(user!!.dress_reservation_id, absoluteAdapterPosition)
                }

                orderstatusImageProduct.setOnClickListener {
                    clicklistener.onItemImageClick(it, absoluteAdapterPosition)
                }

                orderstatusTextviewDetail.setOnClickListener {
                    clicklistener.onItemDetailClick(user!!.dress_reservation_id, absoluteAdapterPosition)
                }
                Glide.with(this@ViewHolder.itemView)
                    .load(user?.dress_image_url)
                    .into(orderstatusImageProduct)
                orderstatusTextviewDate.text = user?.order_time
                orderstatusTextviewMarketname.text = user?.store_name
                orderstatusTextviewClothename.text = user?.dress_name
                orderstatusTextviewClotheprice.text = user?.price
            }
        }
    }
}