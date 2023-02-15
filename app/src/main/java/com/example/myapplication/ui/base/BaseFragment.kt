package com.example.myapplication.ui.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.ToolbarBinding
import com.example.myapplication.ui.search.SearchActivity
import com.example.myapplication.viewmodel.DressViewModel
import com.example.myapplication.viewmodel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.naver.maps.geometry.LatLng

abstract class BaseFragment<T : ViewDataBinding>(
    @LayoutRes val layoutResId: Int
) : Fragment() {
    private var _binding: T? = null
    protected val binding get() = _binding!!

    // 뒤로가기 버튼을 눌렀을 때를 위한 callback 변수
    private lateinit var callback: OnBackPressedCallback
    private lateinit var homeViewModel: HomeViewModel

    // 툴바 변수들
    protected lateinit var toolbar: Toolbar
    protected lateinit var toolbarmenusearch: MenuItem
    protected lateinit var toolbarmenunotification: MenuItem

    // 검색시 필요한 위경도 데이터 Default 값(홍대)
    protected var lat : Double = 37.5581
    protected var lng : Double = 126.9260

    protected open fun savedatainit(){
    }

    protected abstract fun init()

    protected fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // 하단 바 숨길때
    protected fun hideBottomNavigation(bool: Boolean) {
        if(requireActivity().findViewById<BottomNavigationView>(R.id.second_bottomNavigationView)!=null){
            val bottom: BottomNavigationView =
                requireActivity().findViewById(R.id.second_bottomNavigationView)
            if (bool == true) {
                bottom.visibility = View.GONE
            } else {
                bottom.visibility = View.VISIBLE
            }
        }
    }

    // 뒤로가기 버튼을 눌렀을 때
    protected fun backpress() {
        // 최상위 Fragment가 아닐 때만 뒤로가기 버튼 활성화
        // -> 최상위 Fragment는 bottomnavigationview에 올라가는 것이기 때문에 뒤로가기 버튼을 활성화 하면
        // Activity의 supportfragmentmanger의 stack에서 popbackstack 하게 된다.( 단, 메뉴 선택 기록을 기록했을 경우 )
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (parentFragmentManager.backStackEntryCount != 0) {
                    parentFragmentManager
                        .popBackStackImmediate(null, 0)
                } else if (toolbarmenusearch.isActionViewExpanded) { // 검색창을 눌렀을 경우
                    toolbarmenusearch.collapseActionView()
                } else {
                    requireActivity().finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    protected fun initSubAppbar(name: String, backbtn: Boolean, item: Boolean) {
        if(name == "홈"){
            toolbar.setLogo(R.drawable.icon_logo8)
        }else{
            toolbar.setTitle(name)
        }
        if (backbtn) {
            // 뒤로가기 버튼 추가
            toolbar.setNavigationIcon(R.drawable.icon_appbar)
        } else {
            // 뒤로가기 버튼 지우기
            toolbar.setNavigationIcon(null)
        }
        toolbarmenunotification.setVisible(item)
        toolbarmenusearch.setVisible(item)
    }

    protected fun initAppbar(
        toolbarBinding: ToolbarBinding,
        firstlayoutname: String,
        backbtn: Boolean,
        item : Boolean
    ) {
        // 툴바, 툴바 검색 기록 레이아웃, 툴바 메뉴 연결
        toolbar = toolbarBinding.toolbarToolbar
        // 툴바 메뉴 추가
        toolbar.inflateMenu(R.menu.menu_appbar)
        toolbarmenunotification = toolbar.menu.findItem(R.id.notification)
        toolbarmenusearch = toolbar.menu.findItem(R.id.search)

        initSubAppbar(firstlayoutname, backbtn, item)
        // 툴바 뒤로가기 버튼 클릭 시 효과
        toolbar.setNavigationOnClickListener {
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStackImmediate(null, 0)
            }else{
                requireActivity().onBackPressed()
            }
        }

        backpress()

        toolbarmenusearch.setOnMenuItemClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            intent.putExtra("lat_lng", Pair(lat,lng))
            startActivity(intent)
            true
        }
        toolbarmenunotification.setOnMenuItemClickListener {
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        if(savedInstanceState == null){
            savedatainit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        init()
        // 검색시 필요한 데이터
        homeViewModel.home_latlng.observe(viewLifecycleOwner, Observer<Pair<Double, Double>> {
            if (it != null) {
                this.lat = it.first
                this.lng = it.second
            } else {
                Log.d("whatisthis", "lat_lng, 데이터 없음")
            }
        })
//        backpress() -> 백 버튼을 baseFragment에 넣으면 baseFragment를 상속하는 fragment들을 호출할 때마다 back callback이 초기화 된다.
        // 그래서 backbutton 이벤트 처리시 사용되는 toolbarmenusearch 변수가 초기화 되어 있지 않아 에러를 발생하게 된다.
        // parentFragment에서 한번만 초기화되면 되므로 backpress()메소드를 parentFragment에서 한번만 호출하는 initAppbar()메소드 안에 넣는다.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 프래그먼트가 종료되면 callback 변수 제거
    override fun onDetach() {
        super.onDetach()
        if (::callback.isInitialized) {
            callback.remove()
        }
    }

    // Activity에서 fragment들을 바꿀때마다 각각의 fragment들에서 초기화된 toolbarmenusearch도 같이 바껴야한다.
    // 하지만 hide show로 바꾸었기 때문에 Activity는 toolbarmenusearch값을 가장 마지막으로 초기화 시킨 fragment의 값으로 저장한다.(초기화 가능횟수는 하단바 fragment 갯수로 제한된다.)
    // 그래서 초기화된 fragment들은 hide show가 바뀔 때마다 toolbarmenusearch값을 갱신시키지 못한다.
    // fragment들의 hide show 상태를 구분할 수 있는 메소드에 backpress 이벤트를 다시 초기화시켜 toolbarmenusearch값을 현재 보고 있는 fragment의 toolbarmenusearch값으로 갱신한다.
}