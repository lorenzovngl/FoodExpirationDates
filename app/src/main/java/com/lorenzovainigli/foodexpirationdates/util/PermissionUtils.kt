package com.lorenzovainigli.foodexpirationdates.util

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

object PermissionUtils {

    fun requestPermission(activity: ComponentActivity, permission:String){
        var permissionGranted = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionGranted = let {
                ContextCompat.checkSelfPermission(
                    activity, permission
                )
            } == PackageManager.PERMISSION_GRANTED
        }
        val requestPermissionLauncher =
            activity.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                permissionGranted = isGranted
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}