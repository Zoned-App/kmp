package com.github.zoned.app.logic

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.github.zoned.app.data.PermissionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

const val LOBBY_NOTIF_CHANNEL: String = "ZONED_LOBBY_CHANNEL"

class AndroidNotifications(val context: Context, registry: ActivityResultRegistry) : PlatformNotifications {
    private val _permissionStatus = MutableStateFlow(PermissionStatus.Checking)
    override val permissionStatus = _permissionStatus.asStateFlow()
    private val permissionLauncher =
        registry.register("notif_perm_key", ActivityResultContracts.RequestPermission()) { isGranted ->
            _permissionStatus.value = if (isGranted) PermissionStatus.Allowed else PermissionStatus.Denied
        }

    init {
        NotificationChannelCompat.Builder(LOBBY_NOTIF_CHANNEL, NotificationManagerCompat.IMPORTANCE_LOW)
    }

    override fun notificationsEnabled() {
        val areEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()

        _permissionStatus.value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            )
            if (areEnabled && permissionCheck == PackageManager.PERMISSION_GRANTED) {
                PermissionStatus.Allowed
            } else {
                PermissionStatus.Denied
            }
        } else {
            if (areEnabled) PermissionStatus.Allowed else PermissionStatus.Denied
        }
    }

    override fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun showNotification() {

    }

    override fun clearNotification() {

    }
}