package com.github.zoned.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Home() {
    Scaffold(topBar = { Header() }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                modifier = Modifier.padding(PaddingValues(16.dp, 0.dp)),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { JoinGame() }
                item { Text("Nearby Games", style = MaterialTheme.typography.titleLarge) }
                items(games.size) { game ->
                    ActiveGame(games[game])
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Header() {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
            ) {
                Logo()
                Spacer(Modifier.width(8.dp))
                Text("Zoned", style = MaterialTheme.typography.headlineLargeEmphasized)
            }
        }, modifier = Modifier.clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun JoinGame() {
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
            Button(onClick = {}) { Text(text = "Join Game", fontWeight = FontWeight.Bold, fontSize = 16.sp) }
        }
    }
}

val games = listOf(
    Game("Central Park", "neeleshpoli", 40.78280644117304, -73.96557470937626),
    Game("Frisco Commons", "aarush49", 33.15567735480183, -96.8144192461819),
    Game("UTD", "MRBLACKLUFFY", 32.98802776982712, -96.75100654430815)
)

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
