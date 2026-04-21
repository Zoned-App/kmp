package com.github.zoned.app

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import zoned.composeapp.generated.resources.Res
import zoned.composeapp.generated.resources.arrow_back_24px
import zoned.composeapp.generated.resources.logo_colored

@Composable
fun Logo() {
    Icon(
        painter = painterResource(Res.drawable.logo_colored),
        contentDescription = "Zoned Logo",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(50.dp)
    )
}

@Composable
fun Back() {
    Icon(
        painter = painterResource(Res.drawable.arrow_back_24px),
        contentDescription = "Back",
        tint = MaterialTheme.colorScheme.onBackground,
    )
}
