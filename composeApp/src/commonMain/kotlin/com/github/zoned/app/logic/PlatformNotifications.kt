package com.github.zoned.app.logic

import com.github.zoned.app.data.PermissionStatus
import kotlinx.coroutines.flow.StateFlow

interface PlatformNotifications {
    val permissionStatus: StateFlow<PermissionStatus>
    fun notificationsEnabled()
    fun requestPermission()
    fun showNotification()
    fun clearNotification()
}