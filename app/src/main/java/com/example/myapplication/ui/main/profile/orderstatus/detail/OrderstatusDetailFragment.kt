package com.example.myapplication.ui.main.profile.orderstatus.detail

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentOrderstatusDetailBinding
import com.example.myapplication.db.remote.model.DressOrderDto
import com.example.myapplication.db.remote.model.ReservedDressDto
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.viewmodel.DressViewModel


class OrderstatusDetailFragment : BaseFragment<FragmentOrderstatusDetailBinding>(R.layout.fragment_orderstatus_detail) {
    lateinit var dressViewModel: DressViewModel

    override fun init() {

        dressViewModel = ViewModelProvider(requireActivity()).get(DressViewModel::class.java)

        initAppbar(binding.orderstatusToolbar, "주문 상세보기", true, false)
        hideBottomNavigation(true)
        initRecyclerView()

        binding.cancelButton.setOnClickListener(){
            reviseRecyclerView()
        }

    }

    private fun initRecyclerView(){
        with(binding) {
            // 1. 어댑터 생성 및 리사이클러뷰 연결
            val orderstatusAdapter = OrderstatusDetailAdapter()
            detailRecyclerview.adapter = orderstatusAdapter
            detailRecyclerview.layoutManager = LinearLayoutManager(context)
            dressViewModel.dress_reservation_dress_data.observe(viewLifecycleOwner, Observer {
                orderstatusAdapter.userList = it.get(0).reservedDressList as ArrayList<ReservedDressDto>
                orderstatusAdapter.notifyDataSetChanged()
                detailTextviewOrdernumber.text = it.get(0).dress_reservation_id.toString()
                detailTextviewAddress.text = it.get(0).store_address
                detailTextviewOperationhours.text = it.get(0).hours_of_operation.toString()
                detailTextviewPickupdatetime.text = it.get(0).pickup_datetime
                detailTextviewRequests.text = it.get(0).comment
                detailTextviewTotalprice.text = it.get(0).price
                detailTextviewStorename.text = it.get(0).store_name
            })

        }
    }


    private fun reviseRecyclerView(){

    }
}