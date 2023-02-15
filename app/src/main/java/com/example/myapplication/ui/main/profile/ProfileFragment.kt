package com.example.myapplication.ui.main.profile

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentProfileBinding
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.ui.main.profile.inquiry.InquiryFragment
import com.example.myapplication.ui.main.profile.logout.LogoutFragment
import com.example.myapplication.ui.main.profile.myprofile.MyprofileFragment
import com.example.myapplication.ui.main.profile.notice.NoticeFragment
import com.example.myapplication.ui.main.profile.orderstatus.OrderstatusFragment
import com.example.myapplication.ui.main.profile.withdrawal.WithdrawalFragment
import com.example.myapplication.viewmodel.DressViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    lateinit var dressViewModel: DressViewModel

    override fun init() {
        dressViewModel = ViewModelProvider(requireActivity()).get(DressViewModel::class.java)

        hideBottomNavigation(false)
        initAppbar(binding.profileToolbar, "마이페이지", false, true)
        initButton()

        dressViewModel.completeorder.observe(viewLifecycleOwner, Observer {
            binding.profileTextviewCompleteorder.text = it.toString()
        })
//
//        dressViewModel.pickup.observe(viewLifecycleOwner,Observer{
//            binding.profileTextviewPickup.text = it.toString()
//        })
//
//        dressViewModel.pickupconfirm.observe(viewLifecycleOwner,Observer{
//            binding.profileTextviewPickupconfirm.text = it.toString()
//        })
//
//        dressViewModel.purchaseconfirm.observe(viewLifecycleOwner,Observer{
//            binding.profileTextviewPurchaseconfirm.text = it.toString()
//        })
    }
//
    private fun initButton(){
        binding.apply {
            // 마이페이지
            profileTextviewMyprofile.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.profileblank_layout, MyprofileFragment(),"myprofile")
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }

            // 주문현황
            profileInnerlayoutCompleteorder.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.profileblank_layout, OrderstatusFragment(),"completeorder")
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
                dressViewModel.get_dress_resevation_data("주문완료")
              //  dressViewmodel.method("주문완료")
            }
            profileInnerlayoutPickup.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.profileblank_layout, OrderstatusFragment(),"pickup")
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
                dressViewModel.get_dress_resevation_data("픽업중")

                //   dressViewmodel.method("픽업중")

            }
            profileInnerlayoutPickupconfirm.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.profileblank_layout, OrderstatusFragment(),"pickupconfirm")
                    .addToBackStack(null)
                    .commitAllowingStateLoss()

                dressViewModel.get_dress_resevation_data("픽업완료")

                //  dressViewmodel.method("픽업완료")

            }
            profileInnerlayoutPurchaseconfirm.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.profileblank_layout, OrderstatusFragment(),"purchaseconfirm")
                    .addToBackStack(null)
                    .commitAllowingStateLoss()

                dressViewModel.get_dress_resevation_data("구매확정")

                // dressViewmodel.method("구매확정")

            }

            // 공지사항
            profileTextviewAnnouncement.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.profileblank_layout, NoticeFragment(),"notice")
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }

            // 문의사항
            profileTextviewQuestions.setOnClickListener {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.profileblank_layout, InquiryFragment(),"inquiry")
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }

            // 로그아웃
            profileTextviewLogout.setOnClickListener {
                val bottomSheetDialogFragment : BottomSheetDialogFragment = LogoutFragment()
                bottomSheetDialogFragment.show(parentFragmentManager,null)
            }

            // 회원탈퇴
            profileTextviewWithdrawal.setOnClickListener {
                val bottomSheetDialogFragment : BottomSheetDialogFragment = WithdrawalFragment()
                bottomSheetDialogFragment.show(parentFragmentManager,null)
            }
        }
    }
}