package com.example.myapplication.ui.main.location.map

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMapBinding
import com.example.myapplication.db.remote.model.StoreCoordDtoList
import com.example.myapplication.db.remote.model.StoreCoordDto
import com.example.myapplication.ui.base.BaseFragment
import com.example.myapplication.ui.main.location.around.AroundFragment
import com.example.myapplication.ui.storecloth.storedetail.StoreActivity
import com.example.myapplication.viewmodel.StoreViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.android.synthetic.main.item_around_recycler.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executors

class MapFragment : BaseFragment<FragmentMapBinding>(R.layout.fragment_map), OnMapReadyCallback {
    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    // bottomsheet(매장 상세 정보)
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    // 마커 저장용
    private var NowMarkers: CopyOnWriteArrayList<Marker> = CopyOnWriteArrayList<Marker>()

    // 카메라가 이동하기 전 화면의 위도 경도 값을 저장할 변수
    private lateinit var before_StoreCoordDtoList: StoreCoordDtoList

    lateinit var storeViewModel: StoreViewModel

    private var clickedMarker: Marker? = null // 클릭된 마커 변수

    override fun init() {
        storeViewModel = ViewModelProvider(requireActivity()).get(StoreViewModel::class.java)
        // 지도 띄우기
        openMap()

        // 플로팅 버튼 클릭시 이벤트 처리
        binding.mapFab.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .add(R.id.location_layout, AroundFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }

    }

    private fun openMap() {
        // 지도 띄우기
        val fm = childFragmentManager
        val mapFragment =
            fm.findFragmentById(R.id.map) as com.naver.maps.map.MapFragment?
                ?: com.naver.maps.map.MapFragment.newInstance().also {
                    fm
                        .beginTransaction()
                        .add(R.id.map, it)
                        .commitAllowingStateLoss()
                }

        mapFragment.getMapAsync(this)

        // 지도 옵션 초기화
        initializePersistentBottomSheet()
    }

    // 네이버 map api
    override fun onMapReady(map: NaverMap) {
        // 지도 초기 설정
        initMapSetting(map)

        // 화면 움직임이 멈췄을 때 -> 화면에 보이는 부분의 데이터 받아오기
        naverMap.addOnCameraIdleListener {
            // 지금 바운더리에서 새롭게 보여지는 Marker들은 추가한다.
            // 지금 바운더리로 새롭게 데이터 요청 *************
            // Retrofit 주변 매장 전체 데이터 가져와서 NowMarker 갱신
            storeViewModel.set_screen_latlng(naverMap.cameraPosition.target.latitude,naverMap.cameraPosition.target.longitude)
            updateStore(
                naverMap.cameraPosition.target.latitude,
                naverMap.cameraPosition.target.longitude
            )
        }

        naverMap.setOnMapClickListener { pointF, latLng ->
            if (clickedMarker != null) { // 클릭된 마커가 있을 경우
                clickedMarker?.icon =
                    OverlayImage.fromResource(R.drawable.icon_map_small_pin) // 클릭되지 않은 마커로 변경
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                clickedMarker = null // 클릭된 마커 지우기
            } else { // 클릭된 마커가 없을 경우
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    // 초기 지도 기본 세팅
    private fun initMapSetting(map: NaverMap) {
        naverMap = map
        // mapType
        naverMap.mapType = NaverMap.MapType.Basic

        // 확대 축소
        naverMap.maxZoom = 18.0 // 확대
        naverMap.minZoom = 12.0 // 축소

        // 지도 옵션 설정
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_TRANSIT, true) // 대중교통 표시
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true) // 건물 표시
        naverMap.isIndoorEnabled = true // 실내지도 표시

        // ui 설정
        val uiSettings = naverMap.uiSettings
        uiSettings.isIndoorLevelPickerEnabled = true // 실내 지도 층수 버튼
        uiSettings.isZoomControlEnabled = true // 확대 축소 버튼
        uiSettings.isCompassEnabled = true  // 나침반 버튼 -> 위치를 변경하기 위해서
        uiSettings.isLogoClickEnabled = true // 로고 버튼 -> 이건 무조건 해줘야 하나봄
        uiSettings.logoGravity = (Gravity.RIGHT) // 로고 위치
        uiSettings.setLogoMargin(4, 4, 4, 4) // 로고 마진
        uiSettings.isLocationButtonEnabled = false // 현재 위치 버튼 -> 위치를 변경하기 위해서

        // 위치버튼 갱신
        binding.mapLocationbutton.map = naverMap
        naverMap.locationTrackingMode = LocationTrackingMode.Face

        // 현재 위치 설정
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource

        // 초기 위치 설정
        val cameraUpdate =
            CameraUpdate.scrollTo((LatLng(37.5581, 126.9260))).animate(CameraAnimation.Easing)

        // 제일 처음 LatLng(37.5666102, 126.9783881)에서 한번 대기하고, 그 다음에 초기 설정한 위치 LatLng(37.4979921, 127.028046)로 이동한다.
        naverMap.moveCamera(cameraUpdate)
    }

    // 주변 1km 매장 정보 얻어오기
    private fun updateStore(lat: Double, lng: Double) {
        storeViewModel.get_store_near_data(lat, lng)
        storeViewModel.store_near_data.observe(
            viewLifecycleOwner,
            Observer<StoreCoordDtoList> { now_StoreCoordDtoList ->
                if (now_StoreCoordDtoList != null) {
                    if (::before_StoreCoordDtoList.isInitialized) { // 이전 데이터 기록이 있을 경우
                        // 이전 데이터와 겹치지 않는 현재 데이터만 추가하기
                        var new_data = StoreCoordDtoList()
                        for (data in now_StoreCoordDtoList) {
                            var flag: Boolean = false

                            for (old_data_temp in before_StoreCoordDtoList) {
                                if (old_data_temp.store_id == data.store_id) {
                                    flag = true
                                    break
                                }
                            }
                            if (flag == false) { // 이전 데이터와 겹치지 않는 현재 데이터가 발견
                                new_data.add(data) // 따로 저장
                            }
                        }

                        if (new_data.size > 0) {
                            updateMarker(new_data) // 그 마커만 update
                        } else {
                        }

                        // 현재 데이터와 겹치지 않는 이전 데이터만 지우기
                        for (data in before_StoreCoordDtoList) {
                            var flag: Boolean = false
                            for (new_data_temp in now_StoreCoordDtoList) {
                                if (new_data_temp.store_id == data.store_id) {
                                    flag = true
                                    break
                                }
                            }
                            if (flag == false) { // 이전 데이터와 겹치지 않는 현재 데이터가 발견
                                freeMarker(data) // 그 마커만 지우기
                            }
                        }
                    } else { // 이전 데이터 기록이 없을 경우(최초)
                        updateMarker(now_StoreCoordDtoList)
                    }
                    before_StoreCoordDtoList = now_StoreCoordDtoList // 이전 데이터 기록 갱신
                } else {
                    Log.d("whatisthis", "11네트워크 오류가 발생했습니다.")
                }
            })
    }

    // 마커 그리기
    private fun updateMarker(storelist: StoreCoordDtoList) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            // 백그라운드 쓰레드
            for (storeData in storelist) { // List 중 하나 선택
                val marker = Marker() // 마커 생성
                marker.position = LatLng(storeData.latitude, storeData.longitude) // 마커 위치 설정
                marker.icon = OverlayImage.fromResource(R.drawable.icon_map_small_pin) // 마커 아이콘 설정
                marker.width = Marker.SIZE_AUTO
                marker.height = Marker.SIZE_AUTO
                marker.minZoom = 13.3 // Marker가 보이는 최대 줌 정하기
                marker.captionText = storeData.store_name
                marker.isHideCollidedCaptions = true // 겹치는 캡션 자동 숨김 처리
                marker.isHideCollidedSymbols = true //  마커와 겹치는 지도 심벌을 자동으로 숨기도록 지정
                //marker.setHideCollidedMarkers(false);
                marker.tag = storeData.store_id
                NowMarkers?.add(marker)

                // Marker 클릭시
                marker.setOnClickListener { overlay ->
                    if (clickedMarker != null) { // 클릭된 마커가 있음
                        if (clickedMarker == overlay as Marker) { // 같은 마커를 클릭 시
                            clickedMarker?.icon =
                                OverlayImage.fromResource(R.drawable.icon_map_small_pin) // 클릭되지 않은 마커로 변경
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            clickedMarker = null // 클릭된 마커 지우기
                        } else { // 다른 마커를 클릭 시
                            clickedMarker?.icon =
                                OverlayImage.fromResource(R.drawable.icon_map_small_pin) // 클릭되지 않은 마커로 변경
                            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            clickMarker(overlay as Marker, storelist) // 마커 클릭 이벤트 처리
                        }
                    } else { // 클릭된 마커가 없음
                        clickMarker(overlay as Marker, storelist) // 마커 클릭 이벤트 처리
                    }
                    true
                }
            }
            handler.post {
                // 메인 스레드
                for (marker in NowMarkers!!) {
                    marker.map = naverMap
                }
            }
        }
    }

    // 마커 클릭 시
    private fun clickMarker(overlay: Marker, storelist: StoreCoordDtoList) {
        clickedMarker = overlay  // 클릭된 마커 갱신
        clickedMarker?.icon = OverlayImage.fromResource(R.drawable.icon_map_pin) // 클릭된 마커로 변경
        for (i in storelist) {
            if (i.store_id == clickedMarker?.tag) {
                Glide.with(this@MapFragment)
                    .load(i.store_img) //이미지
                    .into(binding.mapImage) //보여줄 위치

                if (i.store_like == true) {
                    //화면에 보여주기
                    Glide.with(this@MapFragment)
                        .load(R.drawable.icon_favorite_filledpink) //이미지
                        .into(binding.mapImageFavorite) //보여줄 위치
                } else {
                    //화면에 보여주기
                    Glide.with(this@MapFragment)
                        .load(R.drawable.icon_favorite_line) //이미지
                        .into(binding.mapImageFavorite) //보여줄 위치
                }

                binding.mapTextviewName.text = i.store_name
                binding.mapTextviewAddress.text =
                    i.address
                binding.mapTextviewOperationhours.text =
                    i.hoursOfOperation

                // 버튼 클릭시 상세 페이지로 이동
                binding.mapInnerlayout.setOnClickListener {
                    val intent = Intent(getActivity(), StoreActivity::class.java)
                    intent.putExtra("store_id", i.store_id)
                    startActivity(intent)
                }
                break
            }
        }
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    // 마커 지우기
    private fun freeMarker(invisible_stores: StoreCoordDto) {
        for (marker in NowMarkers) {
            if (marker.tag == invisible_stores.store_id) {
                marker.map = null // 마커들 지도에서 삭제
                NowMarkers.remove(marker) // 목록에서 제거
                break
            }
        }
    }

    // 위치 권한 여부
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions,
                grantResults
            )
        ) {
            if (!locationSource.isActivated) { // 권한 거부됨
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    // bottomsheet(매장 상세 정보)
    private fun initializePersistentBottomSheet() {
        val dm = resources.displayMetrics
        // BottomSheetBehavior에 layout 설정
        bottomSheetBehavior = BottomSheetBehavior.from(binding.mapBottomsheetlayout)
        // 미리보기 높이 = 현재 위치 버튼 크기 + 로고 크기 + 마진 크기
        bottomSheetBehavior.peekHeight = 0

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // BottomSheetBehavior state에 따른 이벤트
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

}