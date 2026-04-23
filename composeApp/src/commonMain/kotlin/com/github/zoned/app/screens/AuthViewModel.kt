package com.github.zoned.app.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.github.zoned.app.data.auth.AuthRepository
import com.github.zoned.app.data.auth.AuthResult
import com.github.zoned.app.data.auth.TokenStore
import com.github.zoned.app.data.auth.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────
//  UI state
// ─────────────────────────────────────────────

data class AuthUiState(
    val isLoggedIn: Boolean = false,
    val user: UserDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

// ─────────────────────────────────────────────
//  ViewModel
// ─────────────────────────────────────────────

class AuthViewModel(
    private val repository: AuthRepository,
    private val tokenStore: TokenStore
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    init {
        // Restore session if token exists on disk
        viewModelScope.launch {
            val token = tokenStore.token.first()
            if (token != null) {
                _state.update { it.copy(isLoggedIn = true) }
            }
        }
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = repository.login(email, password)) {
                is AuthResult.Success -> {
                    _state.update { it.copy(isLoggedIn = true, user = result.data, isLoading = false) }
                    onSuccess()
                }
                is AuthResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    fun signUp(username: String, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = repository.signUp(username, email, password)) {
                is AuthResult.Success -> {
                    _state.update { it.copy(isLoggedIn = true, user = result.data, isLoading = false) }
                    onSuccess()
                }
                is AuthResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _state.update { AuthUiState() }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    // ── Factory ───────────────────────────────────────────────────────────────

    class Factory(
        private val repository: AuthRepository,
        private val tokenStore: TokenStore
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            AuthViewModel(repository, tokenStore) as T
    }
}