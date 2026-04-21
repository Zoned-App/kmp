package com.github.zoned.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.github.zoned.app.data.Game
import com.github.zoned.app.data.Quest
import com.github.zoned.app.data.games
import com.github.zoned.app.data.quests
import com.github.zoned.app.MapView
import com.github.zoned.app.components.Header
import kotlinx.serialization.Serializable

@Composable
fun HomePage(onGameJoin: () -> Unit) {
    Scaffold(topBar = { Header() }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier.padding(PaddingValues(16.dp, 0.dp)),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
fun JoinGame(onGameJoin: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(), shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
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
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(24.dp))
    ) {
        MapView(
            modifier = Modifier.fillMaxSize(), game.lat, game.lon
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        0f to MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.9f), // Matches map base
                        0.75f to Color.Transparent // Ends halfway for logo safety
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
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(quest.description, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { quest.progress },
                modifier = Modifier.fillMaxWidth(),
                strokeCap = StrokeCap.Round // Makes the progress bar look more "finished"
            )
        }
    }
}

@Serializable
data object HomeRoute : NavKey