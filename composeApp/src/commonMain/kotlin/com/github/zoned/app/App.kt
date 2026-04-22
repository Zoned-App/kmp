package com.github.zoned.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.github.zoned.app.screens.HomePage
import com.github.zoned.app.screens.HomeRoute
import com.github.zoned.app.screens.JoinGamePage
import com.github.zoned.app.screens.JoinGamePageRoute
import com.github.zoned.app.screens.LoginPage
import com.github.zoned.app.screens.LoginRoute
import com.github.zoned.app.screens.SignUpPage
import com.github.zoned.app.screens.SignUpRoute

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
                    is HomeRoute -> NavEntry(HomeRoute) {
                        HomePage(
                            onGameJoin = { backStack.add(JoinGamePageRoute) },
                            onNavigateToLogin = { backStack.add(LoginRoute) },
                            onNavigateToSignUp = { backStack.add(SignUpRoute) },
                            isLoggedIn = false // replace with your auth state
                        )
                    }

                    is JoinGamePageRoute -> NavEntry(JoinGamePageRoute) {
                        JoinGamePage(onBack = { backStack.removeLast() })
                    }

                    is LoginRoute -> NavEntry(LoginRoute) {
                        LoginPage(
                            onBack = { backStack.removeLast() },
                            onLoginSuccess = {
                                // Pop everything back to HomeRoute
                                while (backStack.size > 1) backStack.removeLast()
                            },
                            onNavigateToSignUp = {
                                backStack.removeLast()
                                backStack.add(SignUpRoute)
                            }
                        )
                    }

                    is SignUpRoute -> NavEntry(SignUpRoute) {
                        SignUpPage(
                            onBack = { backStack.removeLast() },
                            onSignUpSuccess = {
                                // Pop everything back to HomeRoute
                                while (backStack.size > 1) backStack.removeLast()
                            },
                            onNavigateToLogin = {
                                backStack.removeLast()
                                backStack.add(LoginRoute)
                            }
                        )
                    }

                    else -> NavEntry(route) {
                        HomePage(
                            onGameJoin = { backStack.add(JoinGamePageRoute) },
                            onNavigateToLogin = { backStack.add(LoginRoute) },
                            onNavigateToSignUp = { backStack.add(SignUpRoute) },
                            isLoggedIn = false
                        )
                    }
                }
            }
        }
    }
}