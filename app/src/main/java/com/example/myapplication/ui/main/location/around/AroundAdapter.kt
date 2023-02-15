package com.example.myapplication.ui.main.location.around

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemAroundRecyclerBinding
import com.example.myapplication.db.remote.model.StoreCoordDto
import com.example.myapplication.db.remote.model.StoreDetailDto
import com.example.myapplication.db.remote.model.StoreLikeDto
import com.example.myapplication.ui.ItemListClickInterface
import com.example.myapplication.ui.main.favorite.store.FavoriteStoreDiffUtil

//
//class AroundAdapter(clicklistener: AroundAdapter.ClothesClickListener) :
//    RecyclerView.Adapter<AroundAdapter.ViewHolder>() {
//
//    var userList: ArrayList<StoreCoordDto>? = null
//
//    var clicklistener: AroundAdapter.ClothesClickListener = clicklistener
//
//    interface ClothesClickListener {
//        fun onItemMarketFavoriteClick(view: View, position: Int)
//        fun onItemMarketLayoutClick(view: View, position: Int)
//    }
//
//    fun updatedata(data: ArrayList<StoreCoordDto>) {
//        userList = data
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val binding =
//            ItemAroundRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val user = userList?.get(position)
//
//        holder.setUser(user, position)
//    }
//
//    override fun getItemCount(): Int {
//        return userList?.size ?: 0
//    }
//
//    inner class ViewHolder(val binding: ItemAroundRecyclerBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun setUser(user: StoreCoordDto?, position: Int) {
//            with(binding) {
//                if (user?.store_like == false) {
//                    //화면에 보여주기
//                    Glide.with(this@ViewHolder.itemView)
//                        .load(R.drawable.icon_favorite_line) //이미지
//                        .into(marketFavorite) //보여줄 위치
//                } else {
//                    //화면에 보여주기
//                    Glide.with(this@ViewHolder.itemView)
//                        .load(R.drawable.icon_favorite_filledpink) //이미지
//                        .into(marketFavorite) //보여줄 위치
//                }
//
//                Glide.with(this@ViewHolder.itemView)
//                    .load(user?.store_img) //이미지
//                    .into(marketImage) //보여줄 위치
//                marketName.text = user?.store_name.toString()
//                marketAddress.text = user?.address.toString()
//                marketOperationhours.text = user?.hoursOfOperation.toString()
//
//                marketLayout.setOnClickListener {
//                    clicklistener.onItemMarketLayoutClick(it, absoluteAdapterPosition)
//                    notifyDataSetChanged()
//                }
//                marketFavorite.setOnClickListener {
//                    clicklistener.onItemMarketFavoriteClick(it, absoluteAdapterPosition)
//                    notifyDataSetChanged()
//
//                }
//            }
//        }
//    }
//}
class AroundAdapter(clicklistener: ItemListClickInterface) :
    ListAdapter<StoreCoordDto, AroundAdapter.MyViewHolder>(AroundDiffUtil) {

    var clicklistener: ItemListClickInterface = clicklistener

    inner class MyViewHolder(val binding: ItemAroundRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: StoreCoordDto) {
            with(binding) {
                if (user.store_like == false) {
                    //화면에 보여주기
                    Glide.with(this@MyViewHolder.itemView)
                        .load(R.drawable.icon_favorite_line) //이미지
                        .into(marketFavorite) //보여줄 위치
                } else {
                    //화면에 보여주기
                    Glide.with(this@MyViewHolder.itemView)
                        .load(R.drawable.icon_favorite_filledpink) //이미지
                        .into(marketFavorite) //보여줄 위치
                }


                Glide.with(this@MyViewHolder.itemView)
                    .load(user.store_img) //이미지
                    .into(marketImage) //보여줄 위치

                marketName.text = user?.store_name.toString()
                marketAddress.text = user?.address.toString()
                marketOperationhours.text = "${user?.open_day}, ${user?.hoursOfOperation}"

                marketLayout.setOnClickListener {
                    clicklistener.onItemMarketLayoutClick(user.store_id, absoluteAdapterPosition)
                }
                marketFavorite.setOnClickListener {
                    if (user?.store_like!! == true) {
                        //화면에 보여주기
                        Glide.with(this@MyViewHolder.itemView)
                            .load(R.drawable.icon_favorite_line) //이미지
                            .into(marketFavorite) //보여줄 위치
                        user?.store_like = false
                    } else {
                        //화면에 보여주기
                        Glide.with(this@MyViewHolder.itemView)
                            .load(R.drawable.icon_favorite_filledpink) //이미지
                            .into(marketFavorite) //보여줄 위치
                        user?.store_like = true
                    }
                    clicklistener.onItemMarketFavoriteClick(
                        true,
                        user.store_id,
                        it,
                        absoluteAdapterPosition
                    )
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

object AroundDiffUtil : DiffUtil.ItemCallback<StoreCoordDto>() {
    override fun areItemsTheSame(oldItem: StoreCoordDto, newItem: StoreCoordDto): Boolean {
        return oldItem  == newItem
    }

    override fun areContentsTheSame(oldItem: StoreCoordDto, newItem: StoreCoordDto): Boolean {
        return (oldItem.store_id == newItem.store_id && oldItem.store_like == newItem.store_like)
    }
}
