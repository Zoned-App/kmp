package com.github.zoned.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.github.zoned.app.Back
import com.github.zoned.app.Permissions
import com.github.zoned.app.data.PermissionStatus
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import zoned.composeapp.generated.resources.Res
import zoned.composeapp.generated.resources.check_24px
import zoned.composeapp.generated.resources.location_on_24px
import zoned.composeapp.generated.resources.notifications_active_24px
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun JoinGamePage(onBack: () -> Unit, onJoin: (String) -> Unit) {
    val codeFieldState = rememberTextFieldState()
    val validCode = codeFieldState.text.length == 6

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = {
            Text(
                "Join Game", style = MaterialTheme.typography.headlineLargeEmphasized
            )
        }, navigationIcon = { IconButton(onClick = onBack) { Back() } })
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.padding(PaddingValues(16.dp, 0.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Enter game code:", style = MaterialTheme.typography.titleMedium,
                )
                CodeInput(state = codeFieldState)
                GameReadiness(
                    validCode = validCode,
                    onContinue = { onJoin(codeFieldState.text.toString()) }
                )
                Button(
                    onClick = { onJoin(codeFieldState.text.toString()) },
                    enabled = validCode,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Join Game") }
            }
        }
    }
}

@Composable
fun CodeInput(state: TextFieldState) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    val codeLen = 6

    LaunchedEffect(Unit) {
        delay(100.milliseconds)
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    LaunchedEffect(state.text.length) {
        if (state.text.length == codeLen) {
            keyboardController?.hide()
            localFocusManager.clearFocus()
        }
    }

    BasicTextField(
        state,
        modifier = Modifier.focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Go
        ),
        inputTransformation = InputTransformation.maxLength(codeLen)
            .then(InputTransformation.allCaps(Locale.current))
            .then(InputTransformation {
                val filtered = asCharSequence().filter { it != ' ' }
                if (filtered.length != length) {
                    replace(0, length, filtered)
                }
            }),
        lineLimits = TextFieldLineLimits.SingleLine,
        decorator = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(codeLen) { index ->
                    CodeBox(
                        char = state.text.getOrNull(index)?.toString() ?: "",
                        isFocused = state.text.length == index
                    )
                }
            }
        },
        onKeyboardAction = {
            if (state.text.length == codeLen) {
                keyboardController?.hide()
                localFocusManager.clearFocus()
            }
        }
    )
}

@Composable
fun CodeBox(char: String, isFocused: Boolean) {
    val borderColor = if (isFocused) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.outlineVariant

    Box(
        modifier = Modifier
            .size(width = 48.dp, height = 56.dp)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun GameReadiness(validCode: Boolean, onContinue: () -> Unit) {
    val notificationStatus by Permissions.notifications.permissionStatus.collectAsState()
    val locationStatus by Permissions.location.permissionStatus.collectAsState()

    LaunchedEffect(Unit) {
        Permissions.notifications.notificationsEnabled()
        Permissions.location.notificationsEnabled()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Game Readiness",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Ensure your device is ready to join the zone.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        PermissionCard(
            title = "Notifications",
            description = "Required for live game updates while your phone is in your pocket.",
            status = notificationStatus,
            icon = Res.drawable.notifications_active_24px,
            onRequest = { Permissions.notifications.requestPermission() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        PermissionCard(
            title = "Location",
            description = "Used to track zone boundaries and interactive features.",
            status = locationStatus,
            icon = Res.drawable.location_on_24px,
            onRequest = { Permissions.location.requestPermission() }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onContinue,
            enabled = notificationStatus == PermissionStatus.Allowed
                    && locationStatus == PermissionStatus.Allowed
                    && validCode,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Enter Lobby", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PermissionCard(
    title: String,
    description: String,
    status: PermissionStatus,
    icon: DrawableResource,
    onRequest: () -> Unit
) {
    val statusColor = when (status) {
        PermissionStatus.Allowed -> Color(0xFF4CAF50)
        PermissionStatus.Denied -> MaterialTheme.colorScheme.error
        PermissionStatus.Checking -> MaterialTheme.colorScheme.outline
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.size(32.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(description, style = MaterialTheme.typography.bodySmall)
            }

            when (status) {
                PermissionStatus.Denied -> {
                    TextButton(onClick = onRequest) {
                        Text("FIX")
                    }
                }
                PermissionStatus.Allowed -> {
                    Icon(
                        painter = painterResource(Res.drawable.check_24px),
                        contentDescription = "Ready",
                        tint = statusColor
                    )
                }
                PermissionStatus.Checking -> {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Serializable
data object JoinGamePageRoute : NavKey