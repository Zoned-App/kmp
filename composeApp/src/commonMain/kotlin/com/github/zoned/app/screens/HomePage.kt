package com.github.zoned.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import com.github.zoned.app.MapView
import com.github.zoned.app.components.Header
import com.github.zoned.app.data.Game
import com.github.zoned.app.data.Quest
import com.github.zoned.app.data.games
import com.github.zoned.app.data.quests
import kotlinx.serialization.Serializable

@Composable
fun HomePage(
    onGameJoin: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    isLoggedIn: Boolean = false
) {
    Scaffold(topBar = { Header() }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier.padding(PaddingValues(16.dp, 0.dp)),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (!isLoggedIn) {
                    item { AuthCard(onNavigateToLogin, onNavigateToSignUp) }
                }

                item { JoinGame(onGameJoin) }
                item { Text("Nearby Games", style = MaterialTheme.typography.titleLarge) }
                items(games.size) { game ->
                    ActiveGame(games[game])
                }
                item {
                    Text(
                        "Quests",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                items(quests.size) { quest ->
                    QuestItemCard(quests[quest])
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AuthCard(
    onNavigateToLogin: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Join the fun!", style = MaterialTheme.typography.displaySmall)
            Spacer(Modifier.height(4.dp))
            Text(
                "Log in or create an account to get started",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onNavigateToLogin,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Log In", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                OutlinedButton(
                    onClick = onNavigateToSignUp,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Sign Up", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun JoinGame(onGameJoin: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Ready to play?", style = MaterialTheme.typography.displaySmall)
            Spacer(Modifier.height(8.dp))
            Button(onClick = onGameJoin) { Text(text = "Join Game", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
        }
    }
}

@Composable
fun ActiveGame(game: Game) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(24.dp))
    ) {
        MapView(
            modifier = Modifier.fillMaxSize(), game.lat, game.lon
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        0f to MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.9f),
                        0.75f to Color.Transparent
                    )
                )
        )
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                game.location,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(game.host)
        }
    }
}

@Composable
fun QuestItemCard(quest: Quest) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(quest.description, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { quest.progress },
                modifier = Modifier.fillMaxWidth(),
                strokeCap = StrokeCap.Round
            )
        }
    }
}

@Serializable
data object HomeRoute : NavKey