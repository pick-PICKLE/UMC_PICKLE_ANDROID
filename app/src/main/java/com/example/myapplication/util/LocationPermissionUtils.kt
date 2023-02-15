package com.example.myapplication.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat


object LocationPermissionUtils {

    /*
    * Check Manifest.permission.ACCESS_FINE_LOCATION or Manifest.permission.ACCESS_COARSE_LOCATION
    * Granted or not
    * */
    fun isPermissionGranted(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                )
    }

    /*
    * Check gps is enable
    * */
    fun isGpsEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /*
    * Intent open system setting gps
    * */
    fun openSettingGps(context: Context, result: ActivityResultLauncher<Intent>) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        result.launch(intent)
    }

    /*
    * From android Q, we need request user select background access
    * Redirect to setting page.
    * */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun isBackgroundLocationGranted(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !isAccessBackgroundGrantedAllTime(
                context
            )
        ) {
            return false
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun openSettingBackgroundMode(result: ActivityResultLauncher<String>) {
        result.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun isAccessBackgroundGrantedAllTime(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

}