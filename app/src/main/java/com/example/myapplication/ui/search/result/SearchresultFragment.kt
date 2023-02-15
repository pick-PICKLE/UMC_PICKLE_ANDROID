package com.example.myapplication.ui.search.result

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSearchresultBinding
import com.example.myapplication.db.remote.model.DressLikeDto
import com.example.myapplication.db.remote.model.DressSearchResultDto
import com.example.myapplication.db.remote.model.UpdateDressLikeDto
import com.example.myapplication.ui.ItemCardClickInterface
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.ui.storecloth.clothdetail.ClothActivity
import com.example.myapplication.ui.storecloth.storedetail.StoreActivity
import com.example.myapplication.viewmodel.DressViewModel
import com.example.myapplication.viewmodel.SearchViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SearchresultFragment : BaseFragment<FragmentSearchresultBinding>(R.layout.fragment_searchresult) {
    private lateinit var dressViewModel: DressViewModel
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var fragmentadapter: SearchresultAdapter

    private var searchdata = ArrayList<DressSearchResultDto>()
    private var update_islikedata_id: Int? = null
    private var update_list_position: Int? = null

    override fun init() {
        dressViewModel = ViewModelProvider(requireActivity()).get(DressViewModel::class.java)
        searchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)

        binding.searchresultTextviewSort.setOnClickListener {
            val bottomSheetDialogFragment: BottomSheetDialogFragment = SortFragment()
            bottomSheetDialogFragment.show(parentFragmentManager, null)
        }

        binding.searchresultTextviewCategory.setOnClickListener {
            val bottomSheetDialogFragment: BottomSheetDialogFragment = CategoryFragment()
            bottomSheetDialogFragment.show(parentFragmentManager, null)
        }

        initRecyclerView()

        dressViewModel.dress_like_data.observe(viewLifecycleOwner, Observer<List<DressLikeDto>> {
            if (update_list_position != null) {
                if (searchdata[update_list_position!!].dress_id == update_islikedata_id) {
                    searchdata[update_list_position!!].is_liked = !searchdata[update_list_position!!].is_liked!!
//                    recentAdapter.submitList(recentData.toMutableList())
//                    recentAdapter.updateData(recentData)
                    fragmentadapter.notifyItemChanged(update_list_position!!)
                }
                update_list_position = null
            }
        })
    }

    private fun initRecyclerView(){
        fragmentadapter = SearchresultAdapter(clickInterface = (object : ItemCardClickInterface {
            override fun onItemClothImageClick(id: Int, position: Int) {
                val intent = Intent(getActivity(), ClothActivity::class.java)
                intent.putExtra("cloth_id", id)
                startActivity(intent)
            }

            override fun onItemStoreNameClick(id: Int, position: Int) {
                val intent = Intent(getActivity(), StoreActivity::class.java)
                intent.putExtra("store_id", id)
                startActivity(intent)
            }

            override fun onItemClothFavoriteClick(like: Boolean, id: Int, view: View, position: Int) {
                if (id != 0) {
                    dressViewModel.set_dress_like_data(UpdateDressLikeDto(id))
                    dressViewModel.get_dress_like_data()
                    update_islikedata_id = id
                    update_list_position = position
                }
            }
        }))

        dressViewModel.dress_search_data.observe(viewLifecycleOwner, Observer { searchresultdata->
            if(searchresultdata!=null){
                searchdata = searchresultdata.data as ArrayList<DressSearchResultDto>
                fragmentadapter.deleteData()
                fragmentadapter.updateData(searchresultdata.data as ArrayList<DressSearchResultDto>)
                fragmentadapter.notifyDataSetChanged()
                binding.searchresultTextviewResultcount.text = String.format("검색 결과 %d개", (searchresultdata.data as ArrayList<DressSearchResultDto>).size)
            }else{
                Log.d("whatisthis", "search result data가 없습니다.")
            }
        })


        binding.searchresultRecycler.apply {
            layoutManager= GridLayoutManager(this.context,2)
            adapter= fragmentadapter
        }

    }
}
