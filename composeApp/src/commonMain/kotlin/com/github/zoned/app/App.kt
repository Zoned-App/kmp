package com.github.zoned.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.github.zoned.app.data.auth.AuthRepository
import com.github.zoned.app.data.auth.TokenStore
import com.github.zoned.app.screens.AuthViewModel
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
    val context = LocalContext.current
    val tokenStore = TokenStore(context)
    val repository = AuthRepository(tokenStore)
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.Factory(repository, tokenStore)
    )
    val authState by authViewModel.state.collectAsState()

    MaterialTheme {
        AppTheme {
            val backStack = rememberNavBackStack(HomeRoute)

            NavDisplay(backStack = backStack) { route ->
                when (route) {
                    is HomeRoute -> NavEntry(HomeRoute) {
                        HomePage(
                            onGameJoin = { backStack.add(JoinGamePageRoute) },
                            onNavigateToLogin = { backStack.add(LoginRoute) },
                            onNavigateToSignUp = { backStack.add(SignUpRoute) },
                            isLoggedIn = authState.isLoggedIn
                        )
                    }

                    is JoinGamePageRoute -> NavEntry(JoinGamePageRoute) {
                        JoinGamePage(onBack = { backStack.removeLast() })
                    }

                    is LoginRoute -> NavEntry(LoginRoute) {
                        LoginPage(
                            onBack = { backStack.removeLast() },
                            onLoginSuccess = {
                                while (backStack.size > 1) backStack.removeLast()
                            },
                            onNavigateToSignUp = {
                                backStack.removeLast()
                                backStack.add(SignUpRoute)
                            },
                            isLoading = authState.isLoading,
                            error = authState.error,
                            onLogin = { email, password ->
                                authViewModel.login(email, password) {
                                    while (backStack.size > 1) backStack.removeLast()
                                }
                            },
                            onClearError = authViewModel::clearError
                        )
                    }

                    is SignUpRoute -> NavEntry(SignUpRoute) {
                        SignUpPage(
                            onBack = { backStack.removeLast() },
                            onSignUpSuccess = {
                                while (backStack.size > 1) backStack.removeLast()
                            },
                            onNavigateToLogin = {
                                backStack.removeLast()
                                backStack.add(LoginRoute)
                            },
                            isLoading = authState.isLoading,
                            error = authState.error,
                            onSignUp = { username, email, password ->
                                authViewModel.signUp(username, email, password) {
                                    while (backStack.size > 1) backStack.removeLast()
                                }
                            },
                            onClearError = authViewModel::clearError
                        )
                    }

                    else -> NavEntry(route) {
                        HomePage(
                            onGameJoin = { backStack.add(JoinGamePageRoute) },
                            onNavigateToLogin = { backStack.add(LoginRoute) },
                            onNavigateToSignUp = { backStack.add(SignUpRoute) },
                            isLoggedIn = authState.isLoggedIn
                        )
                    }
                }
            }
        }
    }
}