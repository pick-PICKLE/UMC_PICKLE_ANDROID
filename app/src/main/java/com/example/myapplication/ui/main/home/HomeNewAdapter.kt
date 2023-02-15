package com.example.myapplication.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemHomecardRecyclerBinding
import com.example.myapplication.db.remote.model.DressOverviewDto
import com.example.myapplication.ui.ItemCardClickInterface


//class HomeNewAdapter(clickInterface: ItemCardClickInterface): RecyclerView.Adapter<HomeNewAdapter.MyViewHolder>(){
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
//            if (recent_clothes?.dress_like!! == false) {
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
////                temp_favorite = changeFavoriteImage(this@MyViewHolder.itemView, temp_favorite!!, binding.homecardImageviewFavorite)
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

class HomeNewAdapter(clicklistener: ItemCardClickInterface) :
    ListAdapter<DressOverviewDto, HomeNewAdapter.MyViewHolder>(HomeNewDiffUtil) {

    var clicklistener: ItemCardClickInterface = clicklistener

    inner class MyViewHolder(val binding: ItemHomecardRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(new_clothes: DressOverviewDto) {

            if (new_clothes.dress_like == false) {
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
                .load(new_clothes.dress_default_img) //이미지
                .into(binding.homecardImageviewImage) //보여줄 위치

            binding.homecardTextviewStorename.text = new_clothes.store_name
            binding.homecardTextviewClothename.text = new_clothes.dress_name
            binding.homecardTextviewClotheprice.text = new_clothes.dress_price

            binding.homecardCardviewFrame.setOnClickListener {
                clicklistener.onItemClothImageClick(new_clothes.dress_id, absoluteAdapterPosition)
            }

            binding.homecardTextviewStorename.setOnClickListener {
                clicklistener.onItemStoreNameClick(new_clothes.store_id, absoluteAdapterPosition)
            }

            binding.homecardImageviewFavorite.setOnClickListener {
                if ( new_clothes?.dress_like!! == true) {
                    //화면에 보여주기
                    Glide.with(this@MyViewHolder.itemView)
                        .load(R.drawable.icon_favorite_whiteline) //이미지
                        .into(binding.homecardImageviewFavorite) //보여줄 위치
                    new_clothes?.dress_like = false
                } else {
                    //화면에 보여주기
                    Glide.with(this@MyViewHolder.itemView)
                        .load(R.drawable.icon_favorite_filledpink) //이미지
                        .into(binding.homecardImageviewFavorite) //보여줄 위치
                    new_clothes?.dress_like = true
                }
                clicklistener.onItemClothFavoriteClick(new_clothes.dress_like!!, new_clothes.dress_id, binding.homecardImageviewFavorite, absoluteAdapterPosition)
//                temp_favorite = changeFavoriteImage(this@MyViewHolder.itemView, temp_favorite!!, binding.homecardImageviewFavorite)
            }
        }
    }

    private fun changeFavoriteImage(view: View, like:Boolean, imageview: ImageView) : Boolean{
        if (like == true) {
            //화면에 보여주기
            Glide.with(view)
                .load(R.drawable.icon_favorite_whiteline) //이미지
                .into(imageview) //보여줄 위치
            return false
        } else {
            //화면에 보여주기
            Glide.with(view)
                .load(R.drawable.icon_favorite_filledpink) //이미지
                .into(imageview) //보여줄 위치
            return true
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

object HomeNewDiffUtil : DiffUtil.ItemCallback<DressOverviewDto>() {
    override fun areItemsTheSame(oldItem: DressOverviewDto, newItem: DressOverviewDto): Boolean {
        return (oldItem == newItem)
    }

    override fun areContentsTheSame(oldItem: DressOverviewDto, newItem: DressOverviewDto): Boolean {
        return oldItem.dress_id == newItem.dress_id && oldItem.dress_like == newItem.dress_like
    }
}