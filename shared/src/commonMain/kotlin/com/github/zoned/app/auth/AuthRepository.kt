package com.github.zoned.app.data.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// ─────────────────────────────────────────────
//  Models
// ─────────────────────────────────────────────

@Serializable
data class UserDto(
    val id: Int,
    val username: String,
    val email: String,
    val createdAt: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val user: UserDto
)

@Serializable
data class ApiError(
    val error: String
)

// ─────────────────────────────────────────────
//  Ktor HTTP client
// ─────────────────────────────────────────────

val httpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    install(Logging) {
        level = LogLevel.BODY
    }
}

// ─────────────────────────────────────────────
//  Token storage (DataStore)
// ─────────────────────────────────────────────

private val Context.dataStore by preferencesDataStore(name = "auth")

class TokenStore(private val context: Context) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
    }

    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }

    suspend fun save(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }

    suspend fun clear() {
        context.dataStore.edit { it.remove(TOKEN_KEY) }
    }
}

// ─────────────────────────────────────────────
//  Result wrapper
// ─────────────────────────────────────────────

sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>()
    data class Error(val message: String) : AuthResult<Nothing>()
}

// ─────────────────────────────────────────────
//  Repository
// ─────────────────────────────────────────────

class AuthRepository(private val tokenStore: TokenStore) {

    // Change to your LAN IP when on a physical device.
    // 10.0.2.2 maps to host localhost on the Android emulator.
    private val baseUrl = "http://10.0.2.2:3000"

    suspend fun signUp(username: String, email: String, password: String): AuthResult<UserDto> {
        return try {
            val response = httpClient.post("$baseUrl/auth/signup") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("username" to username, "email" to email, "password" to password))
            }
            if (response.status.isSuccess()) {
                val body = response.body<AuthResponse>()
                tokenStore.save(body.token)
                AuthResult.Success(body.user)
            } else {
                val err = response.body<ApiError>()
                AuthResult.Error(err.error)
            }
        } catch (e: Exception) {
            AuthResult.Error("Could not reach server: ${e.message}")
        }
    }

    suspend fun login(email: String, password: String): AuthResult<UserDto> {
        return try {
            val response = httpClient.post("$baseUrl/auth/login") {
                contentType(ContentType.Application.Json)
                setBody(mapOf("email" to email, "password" to password))
            }
            if (response.status.isSuccess()) {
                val body = response.body<AuthResponse>()
                tokenStore.save(body.token)
                AuthResult.Success(body.user)
            } else {
                val err = response.body<ApiError>()
                AuthResult.Error(err.error)
            }
        } catch (e: Exception) {
            AuthResult.Error("Could not reach server: ${e.message}")
        }
    }

    suspend fun logout() {
        tokenStore.clear()
    }
}