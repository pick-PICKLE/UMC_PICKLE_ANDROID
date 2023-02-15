package com.example.myapplication.ui.storecloth.clothdetail

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentClothBlankBinding
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.viewmodel.DressViewModel


class ClothBlankFragment : BaseFragment<FragmentClothBlankBinding>(R.layout.fragment_cloth_blank) {

    override fun savedatainit() {
        childFragmentManager
            .beginTransaction()
            .replace(R.id.clothblank_layout, ClothDetailFragment(), "clothdetail")
            .commitAllowingStateLoss()
    }

    override fun init() {
    }

}