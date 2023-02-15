package com.example.myapplication.ui.search.result

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSortBinding
import com.example.myapplication.viewmodel.CategorySortViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
class SortFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSortBinding ?= null
    val binding get() = _binding!!

    private lateinit var categorySortViewModel: CategorySortViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSortBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setStyle(STYLE_NO_TITLE, R.style.AppBottomSheetDialogTheme2)

        return super.onCreateDialog(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categorySortViewModel = ViewModelProvider(requireActivity()).get(CategorySortViewModel::class.java)
        binding.sortTextviewNew.setOnClickListener {
            categorySortViewModel.set_sort_data("거리순")
        }
        binding.sortTextviewPrice.setOnClickListener{
            categorySortViewModel.set_sort_data("가격순")
        }
        binding.sortTextviewDistance.setOnClickListener{
            categorySortViewModel.set_sort_data("최신순")
        }
        binding.sortTextviewFavorite.setOnClickListener{
            categorySortViewModel.set_sort_data("좋아요많은순")
        }
    }
}