package com.github.zoned.app.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LobbyPage() {
    Scaffold(topBar = { CenterAlignedTopAppBar(title = { Text("Lobby Page") }) }) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            item {
                OutlinedCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Game Code:")
                        Text("TestCode")
                    }
                }
            }
        }
    }
}