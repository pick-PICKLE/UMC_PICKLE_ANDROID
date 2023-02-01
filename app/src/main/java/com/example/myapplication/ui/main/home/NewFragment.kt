package com.example.myapplication.ui.main.home

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentNewBinding
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.ui.store.ClothActivity
import com.example.myapplication.ui.store.StoreActivity


//<<<<<<< HEAD
//class NewFragment : BaseFragment<FragmentNewBinding>(R.layout.fragment_new) {
//=======
class NewFragment : BaseFragment<FragmentNewBinding>(R.layout.fragment_new) ,ClothesClickListener{
//>>>>>>> main

    override fun init() {
        rcView()
    }

    private fun addClothes(){
        val clothes1=Clothes(
            R.drawable.one,
            "store1",
            "옷1",
            30000
        )
        clothesList.add(clothes1)
        newclothesList.add(clothes1)

        val clothes2=Clothes(
            R.drawable.two,
            "store2",
            "옷2",
            30000
        )
        clothesList.add(clothes2)
        newclothesList.add(clothes1)

        val clothes3=Clothes(
            R.drawable.two,
            "store1",
            "옷3",
            30000
        )
        clothesList.add(clothes3)
        newclothesList.add(clothes1)

        val clothes4=Clothes(
            R.drawable.one,
            "store1",
            "옷4",
            30000
        )
        clothesList.add(clothes4)
        newclothesList.add(clothes1)

        val clothes5=Clothes(
            R.drawable.two,
            "store1",
            "옷5",
            30000
        )
        clothesList.add(clothes5)
        newclothesList.add(clothes1)

    }

    private fun rcView(){
        addClothes()
        binding.newRecyclerView.apply {
            layoutManager= GridLayoutManager(this.context,2)
//<<<<<<< HEAD
//            adapter=CardViewAdapter(newclothesList)
//=======
            adapter=CardViewAdapter(newclothesList,this@NewFragment)

//>>>>>>> main
        }
    }

    override fun onClick(clothes: Clothes) {
     //   val intent = Intent(context, ClothActivity::class.java)
      //  startActivity(intent)
    }

    override fun onClickStore(string: String) {
        val intent = Intent(getActivity(), StoreActivity::class.java)
        startActivity(intent)
    }
}