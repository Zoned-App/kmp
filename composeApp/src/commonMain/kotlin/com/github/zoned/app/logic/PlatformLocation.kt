package com.github.zoned.app.logic

import com.github.zoned.app.data.PermissionStatus
import kotlinx.coroutines.flow.StateFlow

interface PlatformLocation {
    val permissionStatus: StateFlow<PermissionStatus>
    fun notificationsEnabled()
    fun requestPermission()
}