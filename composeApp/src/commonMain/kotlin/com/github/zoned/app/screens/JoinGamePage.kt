package com.github.zoned.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.github.zoned.app.Back
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun JoinGamePage(onBack: () -> Unit) {
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
                .padding(paddingValues), contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier.padding(PaddingValues(16.dp, 0.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        "Enter game code:", style = MaterialTheme.typography.titleMedium,
                    )
                }
                item { CodeInput(state = codeFieldState) }
                item {
                    Button(
                        onClick = {}, enabled = validCode, modifier = Modifier.fillMaxWidth()
                    ) { Text("Join Game") }
                }
            }
        }
    }
}

@Composable
fun CodeInput(state: TextFieldState) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val codeLen = 6

    LaunchedEffect(Unit) {
        delay(100.milliseconds)
        focusRequester.requestFocus()
        keyboardController?.show()
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
        inputTransformation = InputTransformation.maxLength(codeLen).then(InputTransformation.allCaps(Locale.current)),
        lineLimits = TextFieldLineLimits.SingleLine,
        decorator = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(codeLen) { index ->
                    CodeBox(
                        char = state.text.getOrNull(index)?.toString() ?: "", state.text.length == index
                    )
                }
            }
        },
        onKeyboardAction = {
            if (state.text.length == codeLen) {

            }
        })
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
            text = char, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSurface
        )
    }
}

data object JoinGamePageRoute : NavKey {}
