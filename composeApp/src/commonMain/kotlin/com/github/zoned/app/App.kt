package com.github.zoned.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.zoned.app.screens.HomePage
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import com.github.zoned.app.screens.HomeRoute
import com.github.zoned.app.screens.JoinGamePage
import com.github.zoned.app.screens.JoinGamePageRoute

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
                    is HomeRoute -> NavEntry(HomeRoute) { HomePage(onGameJoin = { backStack.add(JoinGamePageRoute) }) }
                    is JoinGamePageRoute -> NavEntry(JoinGamePageRoute) { JoinGamePage(onBack = { backStack.removeLast() }) }
                    else -> NavEntry(route) { HomePage(onGameJoin = { backStack.add(JoinGamePageRoute) }) }
                }
            }
        }
    }
}
