package com.example.myapplication.ui.main.profile

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentProfileBlankBinding
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.viewmodel.DressViewModel
import com.example.myapplication.viewmodel.UserViewModel

class ProfileBlankFragment :
    BaseFragment<FragmentProfileBlankBinding>(R.layout.fragment_profile_blank) {
    private lateinit var userViewModel: UserViewModel
    lateinit var dressViewModel: DressViewModel

    override fun savedatainit() {
        childFragmentManager
            .beginTransaction()
            .replace(R.id.profileblank_layout, ProfileFragment(), "profile")
            .commitAllowingStateLoss()
    }

    override fun init() {
        dressViewModel = ViewModelProvider(requireActivity()).get(DressViewModel::class.java)
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        userViewModel.get_user_profile_data()

        dressViewModel.get_dress_resevation_data("주문완료")
        dressViewModel.dress_reservation_data.observe(viewLifecycleOwner, Observer {
            dressViewModel.set_completeorder(it.size)
        })
        dressViewModel.get_dress_resevation_data("픽업중")
        dressViewModel.dress_reservation_data.observe(viewLifecycleOwner, Observer {
            dressViewModel.set_pickup(it.size)
        })
        dressViewModel.get_dress_resevation_data("픽업완료")
        dressViewModel.dress_reservation_data.observe(viewLifecycleOwner, Observer {
            dressViewModel.set_pickupconfirm(it.size)
        })
        dressViewModel.get_dress_resevation_data("구매확정")
        dressViewModel.dress_reservation_data.observe(viewLifecycleOwner, Observer {
            dressViewModel.set_purchaseconfirm(it.size)
        })
    }

//    override fun onHiddenChanged(hidden: Boolean) {
//        super.onHiddenChanged(hidden)
//        if(!hidden){
//            dressViewModel.get_dress_resevation_data("주문완료")
//            dressViewModel.dress_reservation_data.observe(viewLifecycleOwner, Observer {
//                dressViewModel.set_completeorder(it.size.toInt())
//            })
//            dressViewModel.get_dress_resevation_data("픽업중")
//            dressViewModel.dress_reservation_data.observe(viewLifecycleOwner, Observer {
//                dressViewModel.set_pickup(it.size.toInt())
//            })
//            dressViewModel.get_dress_resevation_data("픽업완료")
//            dressViewModel.dress_reservation_data.observe(viewLifecycleOwner, Observer {
//                dressViewModel.set_pickupconfirm(it.size.toInt())
//            })
//            dressViewModel.get_dress_resevation_data("구매확정")
//            dressViewModel.dress_reservation_data.observe(viewLifecycleOwner, Observer {
//                dressViewModel.set_purchaseconfirm(it.size.toInt())
//            })
//        }
//    }
}