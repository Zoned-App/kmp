package com.github.zoned.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.zoned.app.logic.AndroidLocation
import com.github.zoned.app.logic.AndroidNotifications

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        Permissions.notifications = AndroidNotifications(this.applicationContext, this.activityResultRegistry)
        Permissions.location = AndroidLocation(this.applicationContext, this.activityResultRegistry)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}