package com.example.myapplication.view.search.result

import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentSearchresultBinding
import com.example.myapplication.data.remote.model.UpdateDressLikeDto
import com.example.myapplication.base.BaseFragment
import com.example.myapplication.data.remote.model.search.SearchHistroyData
import com.example.myapplication.view.search.SearchhistoryAdapter
import com.example.myapplication.view.storecloth.clothdetail.ClothActivity
import com.example.myapplication.view.storecloth.storedetail.StoreActivity
import com.example.myapplication.viewmodel.OptionViewModel
import com.example.myapplication.viewmodel.DressViewModel
import com.example.myapplication.viewmodel.SearchViewModel
import com.example.myapplication.widget.utils.*
import com.example.myapplication.widget.utils.Utils.KEY_SEARCH_HISTORY
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchresultFragment : BaseFragment<FragmentSearchresultBinding>(R.layout.fragment_searchresult) {
    val dressViewModel: DressViewModel by activityViewModels<DressViewModel>()
    private lateinit var optionViewModel: OptionViewModel
    @Inject
    lateinit var sharedPreferencesmanager: SharedPreferencesManager

    private lateinit var searchresultAdapter: SearchresultAdapter
    private lateinit var searchhistoryAdapter: SearchhistoryAdapter

    private var searchHistoryDataList = ArrayList<SearchHistroyData>()
    private lateinit var resultlayout: ConstraintLayout
    private lateinit var recordlayout: ConstraintLayout
    private var buttonClick = false

    override fun init() {
        optionViewModel = ViewModelProvider(requireActivity()).get(OptionViewModel::class.java)

        binding.searchresultTextviewSort.setOnClickListener {
            val bottomSheetDialogFragment: BottomSheetDialogFragment = SortFragment()
            bottomSheetDialogFragment.show(parentFragmentManager, null)
        }

        binding.searchresultTextviewCategory.setOnClickListener {
            val bottomSheetDialogFragment: BottomSheetDialogFragment = CategoryFragment()
            bottomSheetDialogFragment.show(parentFragmentManager, null)
        }

        optionViewModel.category_data.observe(this, Observer {
            binding.searchresultTextviewCategory.text = optionViewModel.category_data.value
        })

        optionViewModel.sort_data.observe(this, Observer {
            binding.searchresultTextviewSort.text = optionViewModel.sort_data.value
        })

        optionViewModel.searchword_data.observe(this, Observer { // EditText??? ????????? ?????? ???
            optionViewModel.initOptiondata() // ?????? ????????? ????????? ??????.

            if(it.isNullOrEmpty()){ // ????????? ??? ?????? ??????
                searchresultAdapter.submitList(null) // ?????? ?????? ?????????
                binding.searchresultTextviewResultcount.text = String.format("?????? ?????? 0???")
                resultlayout.visibility = View.INVISIBLE // ?????? ?????? ???????????? ??????

                recordlayout.visibility = View.VISIBLE // ?????? ?????? ????????????
            }
        })

        optionViewModel.search_bt_event.observe(this, EventObserver{
            if(!optionViewModel.searchword_data.value.isNullOrEmpty()){
                Log.d("whatisthis","??!")
                searchHistoryDataList.add(0, SearchHistroyData(optionViewModel.searchword_data.value.toString()))
                sharedPreferencesmanager.setsearchhistoryString(KEY_SEARCH_HISTORY,searchHistoryDataList)
                initRecyclerViewRecord() // ?????? ?????? ????????????.
            }
        })

        resultlayout = binding.searchresultInnerlayoutResult
        recordlayout = binding.searchresultInnerlayoutRecord

        initRecyclerViewResult()
        initRecyclerViewRecord()
    }

    private fun initRecyclerViewResult(){
        searchresultAdapter = SearchresultAdapter(clicklistener = (object : ItemCardClickInterface {
            override fun onItemClothImageClick(id: Int, position: Int) {
                buttonClick = true

                val intent = Intent(getActivity(), ClothActivity::class.java)
                intent.putExtra("cloth_id", id)
                startActivity(intent)
            }

            override fun onItemStoreNameClick(id: Int, position: Int) {
                buttonClick = true

                val intent = Intent(getActivity(), StoreActivity::class.java)
                intent.putExtra("store_id", id)
                startActivity(intent)
            }

            override fun onItemClothFavoriteClick(like: Boolean, id: Int, view: View, position: Int) {
                if (id != 0) {
                    dressViewModel.set_dress_like_data(UpdateDressLikeDto(id))
                }
            }
        }))

        dressViewModel.dress_search_data.observe(viewLifecycleOwner, Observer {
            resultlayout.visibility = View.VISIBLE
            recordlayout.visibility = View.INVISIBLE
            when (it) {
                is NetworkResult.Loading -> {
                }

                is NetworkResult.Error -> {
                    Log.d("whatisthis", "search result data??? ????????????.")
                    searchresultAdapter.submitList(null)
                    binding.searchresultTextviewResultcount.text = String.format("?????? ?????? 0???")
                }

                is NetworkResult.Success -> {
                    searchresultAdapter.submitList(it.data!!.data)
                    binding.searchresultTextviewResultcount.text = String.format("?????? ?????? %d???", (it.data!!.data)!!.size)
                }
            }
        })


        binding.searchresultRecyclerResult.apply {
            layoutManager= GridLayoutManager(this.context,2)
            adapter= searchresultAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        if(buttonClick){
            dressViewModel.get_dress_search_data(
                optionViewModel.category_data.value.toString(),
                optionViewModel.latlng_data.value!!.first,
                optionViewModel.latlng_data.value!!.second,
                optionViewModel.searchword_data.value!!,
                optionViewModel.sort_data.value.toString()
            )
            buttonClick = false
        }
    }

    private fun initRecyclerViewRecord() {
        // ??????????????? ????????? ????????????
        searchHistoryDataList =
            sharedPreferencesmanager.getsearchhistoryString(KEY_SEARCH_HISTORY) as ArrayList<SearchHistroyData>

        // ????????? ????????? recycler??? ?????????, ????????? ?????? ????????? ??????
        searchhistoryAdapter =
            SearchhistoryAdapter(object : SearchhistoryAdapter.ItemClickListener {
                // recycler ????????? ??? ???????????? ???????????? ??? -> ?????? ???????????? ?????????
                override fun onTextItemClick(searchhistory: String, position: Int) {
                    optionViewModel.set_searchword_data(searchhistory)

                    // ?????? ?????? ??? ?????? ?????? ????????????(API ??????)
                    dressViewModel.get_dress_search_data(
                        optionViewModel.category_data.value.toString(),
                        optionViewModel.latlng_data.value!!.first,
                        optionViewModel.latlng_data.value!!.second,
                        optionViewModel.searchword_data.value!!,
                        optionViewModel.sort_data.value.toString()
                    )
                    optionViewModel.onSearchBTEvent()
                    // ?????? ?????? ???????????? ??? ?????????
                    resultlayout.visibility = View.VISIBLE
                    recordlayout.visibility = View.INVISIBLE
                    optionViewModel.onHistoryBTEvent()
                }

                // recycler ????????? ??? x ???????????? ???????????? ??? -> ????????? ??????
                override fun onImageItemClick(view: View, position: Int) {
                    searchHistoryDataList.removeAt(position)
                    // ????????? ???????????? ??????????????? ???
                    sharedPreferencesmanager.setsearchhistoryString(
                        KEY_SEARCH_HISTORY,
                        searchHistoryDataList
                    )
                    // ????????? ?????? ?????????
                    searchhistoryAdapter.notifyDataSetChanged()
                }
            })

        searchhistoryAdapter.userList = searchHistoryDataList
        // ????????? ?????? ?????????
        searchhistoryAdapter.notifyDataSetChanged()

        binding.searchresultRecyclerRecord.apply {
            layoutManager = GridLayoutManager(this.context, 3)
            adapter = searchhistoryAdapter
        }
    }

}
