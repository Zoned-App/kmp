package com.github.zoned.app.logic

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.github.zoned.app.data.PermissionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AndroidLocation(val context: Context, registry: ActivityResultRegistry) : PlatformLocation {
    private val _permissionStatus = MutableStateFlow(PermissionStatus.Checking)
    override val permissionStatus = _permissionStatus.asStateFlow()
    private val locationLauncher =
        registry.register("location_perm_key", ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineGrained = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false

            if (fineGrained) {
                _permissionStatus.value = PermissionStatus.Allowed
            } else {
                _permissionStatus.value = PermissionStatus.Denied
            }
        }

    override fun notificationsEnabled() {
        val isGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        _permissionStatus.value = if (isGranted) PermissionStatus.Allowed else PermissionStatus.Denied
    }

    override fun requestPermission() {
        locationLauncher.launch(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }
}