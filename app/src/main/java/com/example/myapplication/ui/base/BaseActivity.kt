package com.example.myapplication.ui.base

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.util.LocationPermissionUtils
import com.example.myapplication.util.LocationPopupUtils
import com.example.myapplication.viewmodel.HomeViewModel

abstract class BaseActivity<T : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int
) : AppCompatActivity() {
    private var _binding: T? = null
    val binding get() = _binding!!

    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val PERMISSIONS_REQUEST_CODE = 100
    private lateinit var locatioNManager: LocationManager
    private var lat_lng: Pair<Double, Double>? = null
    private lateinit var homeViewModel: HomeViewModel

    protected open fun savedatainit() {}
    protected abstract fun init()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutResId)
        binding.lifecycleOwner = this

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        if (savedInstanceState == null) {
            savedatainit()
        }
        init()
    }

    protected fun requestLocationData() {
        if (!LocationPermissionUtils.isPermissionGranted(this)) {
            LocationPopupUtils.dialogLocationDisclosures(this,
                title = getString(R.string.title_location_disclosures),
                message = getString(R.string.msg_explanation_location_permission),
                getString(R.string.action_deny),
                getString(R.string.action_accept),
                onClickNeg = {
                    // Continue run app no permission.
                },
                onClickPos = {
                    requestLocationPermission()
                })
        } else {
            getLocation()
        }
    }

    private fun requestLocationPermission() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        requestLocationPermissionLauncher.launch(permissions)
    }

    //위치 권한
    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            var isGranted = true
            permissions.entries.forEach {
                if (it.value == false) {
                    isGranted = false
                    return@registerForActivityResult
                }
            }
            if (isGranted) {
                getLocation()
                // Check background permission android Q
//                checkPermissionAndroidQ()
            } else {
                // Continue run app no permission.
            }

        }

    private fun checkPermissionAndroidQ() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (LocationPermissionUtils.isBackgroundLocationGranted(this)) {
                // Continue run app flow
            } else {
                LocationPermissionUtils.openSettingBackgroundMode(requestPermissionAndroidQ)
            }
        } else {
            // Continue run app flow
        }
    }

    private val requestPermissionAndroidQ =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { _: Boolean ->
            // We just receive action when user close screen setting background mode.
            // Continue run app flow
        }

    protected fun getLocation() {

        locatioNManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var userLocation: Location = getLatLng()

        if (userLocation != null) {
            homeViewModel.set_home_latlng(userLocation.latitude, userLocation.longitude)

//            latitude = userLocation.latitude
//            longitude = userLocation.longitude
//            Log.d("CheckCurrentLocation", "현재 내 위치 값: ${latitude}, ${longitude}")
//            var mGeoCoder =  Geocoder(applicationContext, Locale.KOREAN)
//            var mResultList: List<Address>? = null
//            try{
//                mResultList = mGeoCoder.getFromLocation(
//                    latitude!!, longitude!!, 1
//                )
//            }catch(e: IOException){
//                e.printStackTrace()
//            }
//
//            if(mResultList != null){
//                Log.d("CheckCurrentLocation", mResultList[0].getAddressLine(0))
//            }
        }
    }

    private fun getLatLng(): Location {
        var currentLatLng: Location? = null
        var hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        var hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED ||
            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
        ) {
            val locatioNProvider = LocationManager.GPS_PROVIDER
            currentLatLng = locatioNManager?.getLastKnownLocation(locatioNProvider)
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    REQUIRED_PERMISSIONS[0]
                )
            ) {
                Toast.makeText(this, "앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
            currentLatLng = getLatLng()
        }
        return currentLatLng!!
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}