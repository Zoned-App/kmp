package com.github.zoned.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.github.zoned.app.MapView
import com.github.zoned.app.Permissions
import com.github.zoned.app.data.testLobbyDetails
import kotlinx.serialization.Serializable

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LobbyPage(code: String, onExitGame: () -> Unit) {
    val lobby = testLobbyDetails.copy(code = code)
    val isHost = true
    val lat = 40.78280644117304
    val lon = -73.96557470937626

    LaunchedEffect(Unit) {
        Permissions.notifications.showNotification()
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Game Lobby", style = MaterialTheme.typography.headlineLargeEmphasized) },
            )
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.surface) {
                if (isHost) {
                    Button(
                        onClick = { /* Start Game */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .height(56.dp)
                            .navigationBarsPadding()
                    ) {
                        Text("Start Game", style = MaterialTheme.typography.titleMedium)
                    }
                } else {
                    Text(
                        "Waiting for host to start the game...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .navigationBarsPadding(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Game Code",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            lobby.code,
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            item {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    MapView(modifier = Modifier.fillMaxSize(), lat = lat, lon = lon)
                }
            }

            item {
                OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Game Details",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Host:", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(lobby.host, fontWeight = FontWeight.Medium)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Location:", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(lobby.location, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            item {
                Text(
                    "Players (${lobby.players.size})",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            items(lobby.players) { player ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ListItem(
                        headlineContent = {
                            Text(
                                player,
                                fontWeight = if (player == lobby.host) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        trailingContent = {
                            if (player == lobby.host) {
                                Badge { Text("Host") }
                            }
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                        )
                    )
                }
            }
        }
    }
}

@Serializable
data class LobbyPageRoute(val code: String) : NavKey