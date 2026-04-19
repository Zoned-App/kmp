package com.github.zoned.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.zoned.app.screens.Home
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute : NavKey

@Composable
@Preview
fun App() {
    MaterialTheme {
        AppTheme {
            val backStack = rememberNavBackStack(HomeRoute)

            NavDisplay(
                backStack = backStack,
            ) { route ->
                when (route) {
                    is HomeRoute -> NavEntry<NavKey>(HomeRoute) { Home() }
                    else -> NavEntry<NavKey>(route) { Home() }
                }
            }
        }
    }
}
