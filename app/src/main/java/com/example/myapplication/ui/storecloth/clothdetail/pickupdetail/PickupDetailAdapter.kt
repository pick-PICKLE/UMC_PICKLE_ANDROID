package com.example.myapplication.ui.storecloth.clothdetail.pickupdetail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemOrderstatusdetailRecyclerBinding
import com.example.myapplication.db.remote.model.DressDetailDto
import com.example.myapplication.db.remote.model.order.ClothOrderData

class PickupDetailAdapter : RecyclerView.Adapter<PickupDetailAdapter.ViewHolder>() {

    var userList: ArrayList<ClothOrderData>? = null
    var dressDetail: DressDetailDto?= null

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

        fun setUser(user: ClothOrderData?) {
            with(binding) {
                Glide.with(this.view)
                    .load(dressDetail?.dress_image_url_list?.get(0)) //이미지
                    .into(binding.orderstatusdetailImageProduct) //보여줄 위치
               // orderstatusdetailImageProduct.setImageURI(Uri.parse(dressDetail?.dress_image_url_list?.get(0)))
                orderstatusdetailTextviewClothename.text = dressDetail?.dress_name
                orderstatusdetailTextviewClothecolorsize.text =
                    "${user?.color} / ${user?.size} / ${user?.count}개"
                orderstatusdetailTextviewClotheprice.text = user?.clothPrice.toString()
            }
        }
    }
}