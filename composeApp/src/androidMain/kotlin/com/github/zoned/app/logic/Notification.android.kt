package com.github.zoned.app.logic

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.github.zoned.app.data.PermissionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

const val LOBBY_NOTIF_CHANNEL: String = "ZONED_LOBBY_CHANNEL"
const val LOBBY_NOTIF_ID: Int = 1001
const val ACTION_DISMISS = "com.github.zoned.app.NOTIFICATION_DISMISSED"

class AndroidNotifications(val context: Context, registry: ActivityResultRegistry) : PlatformNotifications {
    private val _permissionStatus = MutableStateFlow(PermissionStatus.Checking)
    override val permissionStatus = _permissionStatus.asStateFlow()
    private val permissionLauncher =
        registry.register("notif_perm_key", ActivityResultContracts.RequestPermission()) { isGranted ->
            _permissionStatus.value = if (isGranted) PermissionStatus.Allowed else PermissionStatus.Denied
        }

    private val dismissReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ACTION_DISMISS) {
                // Re-post notification immediately to prevent dismissal on Android 14+
                showNotification()
            }
        }
    }

    init {
        val channel = NotificationChannelCompat.Builder(LOBBY_NOTIF_CHANNEL, NotificationManagerCompat.IMPORTANCE_LOW)
            .setName("Lobby Updates")
            .setDescription("Notifications for game lobby updates")
            .build()
        NotificationManagerCompat.from(context).createNotificationChannel(channel)

        ContextCompat.registerReceiver(
            context,
            dismissReceiver,
            IntentFilter(ACTION_DISMISS),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        val deleteIntent = Intent(ACTION_DISMISS).apply {
            setPackage(context.packageName)
        }
        val pendingDeleteIntent = PendingIntent.getBroadcast(
            context,
            0,
            deleteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, LOBBY_NOTIF_CHANNEL)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Zoned Lobby")
            .setContentText("You are currently in a game lobby. Waiting for the host to start.")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true) // Makes it persistent
            .setDeleteIntent(pendingDeleteIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(LOBBY_NOTIF_ID, notification)
        } catch (e: SecurityException) {
            // Permission was revoked
        }
    }

    override fun clearNotification() {
        NotificationManagerCompat.from(context).cancel(LOBBY_NOTIF_ID)
    }
}
