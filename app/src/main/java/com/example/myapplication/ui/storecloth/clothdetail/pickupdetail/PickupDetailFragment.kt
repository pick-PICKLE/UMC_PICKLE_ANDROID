package com.example.myapplication.ui.storecloth.clothdetail.pickupdetail

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.util.Log
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentPickupDetailBinding
import com.example.myapplication.db.remote.model.StoreDetailDto
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.ui.storecloth.clothdetail.ordercomplete.OrderCompleteFragment
import com.example.myapplication.viewmodel.DressViewModel
import com.example.myapplication.viewmodel.OrderViewModel
import com.example.myapplication.viewmodel.ReservationViewModel
import com.example.myapplication.viewmodel.StoreViewModel
import java.util.*
import androidx.lifecycle.Observer
import com.example.myapplication.db.remote.model.DressReservationDto
import com.example.myapplication.db.remote.model.StockQuantityDto
import com.example.myapplication.db.remote.model.order.ClothOrderData

class PickupDetailFragment : BaseFragment<FragmentPickupDetailBinding>(R.layout.fragment_pickup_detail) {
    var chipGroup = ArrayList<TextView>()
    private lateinit var reservationViewModel: ReservationViewModel
    private lateinit var storeViewModel: StoreViewModel
    private lateinit var orderViewModel: OrderViewModel
    private lateinit var dressViewModel: DressViewModel
    private var date: String = ""
    private lateinit var time: String
    private lateinit var dateTime: String
    private var totalPrice: Int = 0

    override fun init() {
        orderViewModel = ViewModelProvider(requireParentFragment()).get(OrderViewModel::class.java)
        dressViewModel = ViewModelProvider(requireActivity()).get(DressViewModel::class.java)
        reservationViewModel = ViewModelProvider(requireActivity()).get(ReservationViewModel::class.java)

        initAppbar(binding.pickupdetailToolbar, "픽업 주문하기", true, false)
        initStore()
        initDate()
        initChip()
        initButton()
        initRecyclerView()
        initTotalPrice()
    }

    private fun initStore(){
        with(binding){
            val dress_detail_data = dressViewModel.dress_detail_data.value
            val storeId = dress_detail_data?.store_id

            if (storeId != null) {
                storeViewModel = ViewModelProvider(requireActivity()).get(StoreViewModel::class.java)
                storeViewModel.get_store_detail_data(storeId,"전체")
                storeViewModel.store_detail_data.observe(viewLifecycleOwner, Observer<StoreDetailDto> { now_storedetail ->
                    if (now_storedetail != null) {
                        binding.pickupdetailTextviewStorename.text = now_storedetail.store_name
                        binding.pickupdetailTextviewAddress.text = now_storedetail.store_address
                        binding.pickupdetailTextviewOperationhours.text = now_storedetail.hours_of_operation
                    } else {
                        Log.d("whatisthis", "store_detail_data, 데이터 없음")
                    }
                })
            }

            storeViewModel = ViewModelProvider(requireActivity()).get(StoreViewModel::class.java)
            val store_detail_data = storeViewModel.store_detail_data.value
            binding.pickupdetailTextviewStorename.text = store_detail_data?.store_name
            binding.pickupdetailTextviewAddress.text = store_detail_data?.store_address
            binding.pickupdetailTextviewOperationhours.text = store_detail_data?.hours_of_operation
        }

    }

    private fun initRecyclerView(){
        with(binding) {
            // 1. 어댑터 생성 및 리사이클러뷰 연결
            val pickupDetailAdapter = PickupDetailAdapter()

//            val pickupDetailDatalist: ArrayList<OrderedClotheData> = ArrayList()
//            pickupDetailDatalist.add(OrderedClotheData("20200204",R.drawable.cardigan1,"ㄴㅁㅇ","옷","8700","검정","M"))
//            pickupDetailDatalist.add(OrderedClotheData("20200204",R.drawable.cardigan1,"ㄴㅁㅇ","옷","8700","검정","M"))
//            pickupDetailDatalist.add(OrderedClotheData("20200204",R.drawable.cardigan1,"ㄴㅁㅇ","옷","8700","검정","M"))
            val order_data = orderViewModel.order_data.value
            val dress_detail_data = dressViewModel.dress_detail_data.value
            pickupdetailRecyclerview.adapter = pickupDetailAdapter
            pickupdetailRecyclerview.layoutManager = LinearLayoutManager(context)
            pickupDetailAdapter.userList = order_data
            pickupDetailAdapter.dressDetail = dress_detail_data
            pickupDetailAdapter.notifyDataSetChanged()


        }
    }

    private fun initDate() {
        with(binding) {
            binding.tvDateDialog.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val year = mcurrentTime.get(Calendar.YEAR)
                val month = mcurrentTime.get(Calendar.MONTH)
                val day = mcurrentTime.get(Calendar.DAY_OF_MONTH)

                val datePicker = DatePickerDialog(requireContext(), R.style.DatePickerTheme, object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                        if(month + 1 < 10) {
                        date = "${year}-0${month + 1}-${day}"
                        binding.tvDateDialog.text = "${year}. 0${month + 1}. ${day}"
                    }
                    else {
                        date = "${year}-${month + 1}-${day}"
                        binding.tvDateDialog.text = "${year}. ${month + 1}. ${day}"
                      }
                    }
                }, year, month, day);
                datePicker.show()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun initChip() {
        chipGroup.add(binding.chip1)
        chipGroup.add(binding.chip2)
        chipGroup.add(binding.chip3)
        chipGroup.add(binding.chip4)
        chipGroup.add(binding.chip5)
        chipGroup.add(binding.chip6)
        chipGroup.add(binding.chip7)
        chipGroup.add(binding.chip8)
        chipGroup.add(binding.chip9)
        chipGroup.add(binding.chip10)
        chipGroup.add(binding.chip11)
        chipGroup.add(binding.chip12)
        chipGroup.add(binding.chip13)
        chipGroup.add(binding.chip14)
        chipGroup.add(binding.chip15)
        chipGroup.add(binding.chip16)

        for (i in 0..chipGroup.size - 1) {
            chipGroup[i].setOnClickListener {
                if(date == "")
                    Toast.makeText(activity, "날짜를 먼저 선택해주세요.", Toast.LENGTH_SHORT).show()
                else {
                    for (j in 0..chipGroup.size - 1) {
                        if (chipGroup[j].background.constantState == resources.getDrawable(R.drawable.chip_background_selected).constantState) {
                            chipGroup[j].setBackgroundResource(R.drawable.chip_background)
                            chipGroup[j].setTextColor(Color.BLACK)
                        }
                        chipGroup[i].setBackgroundResource(R.drawable.chip_background_selected)
                        chipGroup[i].setTextColor(Color.WHITE)
                        time = chipGroup[i].text.toString()

                        binding.ivOrder.setBackgroundResource(R.drawable.green_button_background)
                        binding.ivOrder.setTextColor(Color.WHITE)
                    }
                }
            }
        }
    }

    private fun initButton(){
        with(binding){
            val dress_detail_data = dressViewModel.dress_detail_data.value
            val order_data = orderViewModel.order_data.value as ArrayList<ClothOrderData>

            var temp : ArrayList<StockQuantityDto> = ArrayList<StockQuantityDto>()
            for( i in order_data){

                temp.add(StockQuantityDto(i.count , i.coloridx, i.sizeidx))

            }

            binding.ivOrder.setOnClickListener{
                if (binding.ivOrder.background.constantState != resources.getDrawable(R.drawable.green_button_background).constantState)
                    null
                else {
                    //예약 정보 보내기
                    val comment = binding.pickupdetailEdittextRequest.text.toString()
                    val dress_id = dress_detail_data?.dress_id
                    val pickup_datetime = date + " " + time + ":00"
                    val price = totalPrice
                    val reserved_dress_list = temp.toList()
                    val store_id = dress_detail_data?.store_id

                    Log.d("dress_id", dress_id.toString())
                    Log.d("datetime", pickup_datetime.toString())
                    Log.d("price", price.toString())
                    Log.d("store_id", store_id.toString())

                    val dressReservationDto = DressReservationDto(
                        comment,
                        dress_id!!,
                        pickup_datetime,
                        price,
                        reserved_dress_list,
                        store_id!!
                    )
                    reservationViewModel.set_dresses_reservation(dressReservationDto!!)

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.clothblank_layout, OrderCompleteFragment(), "ordercomplete")
                        .addToBackStack(null)
                        .commitAllowingStateLoss()
                }
            }
        }
    }

    private fun initTotalPrice(){
        with(binding) {
            val order_data = orderViewModel.order_data.value
            if (order_data != null) {
                for (i in 0..order_data.size - 1)
                    totalPrice += order_data.get(i).clothPrice * order_data.get(i).count
            }
            binding.pickupdetailTextviewTotalprice.text = totalPrice.toString() + "원"
        }
    }
}