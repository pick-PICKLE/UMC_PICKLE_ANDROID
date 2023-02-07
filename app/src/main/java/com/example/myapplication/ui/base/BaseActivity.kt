package com.example.myapplication.ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int
) : AppCompatActivity() {
    private var _binding: T? = null
    val binding get() = _binding!!

    protected open fun savedatainit(){}
    protected abstract fun init()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.lifecycleOwner = this

        if(savedInstanceState==null){
            savedatainit()
        }

        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}