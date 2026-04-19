package com.github.zoned.app.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.github.zoned.app.components.Header

@Composable
fun JoinGamePage() {
    Scaffold(topBar = { Header() }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier.padding(PaddingValues(16.dp, 0.dp)),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Text("Joining Game") }
            }
        }
    }
}

data object JoinGamePageRoute : NavKey {}
