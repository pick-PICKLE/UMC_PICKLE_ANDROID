package com.example.myapplication.ui.main.home

import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.CardCellBinding

class CardViewHolder(
    private val cardCellBinding: CardCellBinding,
    private val clickListener: ClothesClickListener
): RecyclerView.ViewHolder(cardCellBinding.root) {

    fun bindClothes(clothes: Clothes){

        cardCellBinding.clothImg.setImageResource(clothes.image)
        cardCellBinding.store.text=clothes.store
        cardCellBinding.cloth.text=clothes.name
        cardCellBinding.price.text= clothes.price.toString()

        cardCellBinding.cardviewframe.setOnClickListener{
            clickListener.onClick(clothes)
        }

    }
}