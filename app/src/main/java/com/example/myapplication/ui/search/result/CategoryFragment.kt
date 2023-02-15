package com.example.myapplication.ui.search.result

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentCategoryBinding
import com.example.myapplication.viewmodel.CategorySortViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CategoryFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentCategoryBinding?= null
    val binding get() = _binding!!

    private lateinit var categorySortViewModel: CategorySortViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_TITLE, R.style.AppBottomSheetDialogTheme2)

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categorySortViewModel = ViewModelProvider(requireActivity()).get(CategorySortViewModel::class.java)
        binding.categoryTextviewAll.setOnClickListener {
            categorySortViewModel.set_category_data("전체")
        }
        binding.categoryTextviewOuter.setOnClickListener{
            categorySortViewModel.set_category_data("아우터")
        }
        binding.categoryTextviewTop.setOnClickListener{
            categorySortViewModel.set_category_data("상의")
        }
        binding.categoryTextviewBottom.setOnClickListener{
            categorySortViewModel.set_category_data("하의")
        }
        binding.categoryTextviewOnepiece.setOnClickListener{
            categorySortViewModel.set_category_data("원피스")
        }
        binding.categoryTextviewOther.setOnClickListener{
            categorySortViewModel.set_category_data("기타")
        }

//        binding.categoryTextviewConfirm.setOnClickListener{
//            categorySortViewModel.set_sort_data("좋아요많은순")
//        }
    }
}