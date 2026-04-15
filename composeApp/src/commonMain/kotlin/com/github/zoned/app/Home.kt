package com.github.zoned.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import org.jetbrains.compose.resources.painterResource
import zoned.composeapp.generated.resources.Res
import zoned.composeapp.generated.resources.logo_colored

@Composable
fun Home() {
    Scaffold(topBar = { Header() }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            Column(modifier = Modifier.padding(16.dp)) {
                JoinGame()
                Spacer(modifier = Modifier.height(16.dp))
                NearbyGames()
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

@Composable
fun Logo() {
    Icon(
        painter = painterResource(Res.drawable.logo_colored),
        contentDescription = "Zoned Logo",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(50.dp)
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun JoinGame() {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(), shape = RoundedCornerShape(24.dp)
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

@Composable
fun NearbyGames() {
    Column {
        Text("Nearby Games", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(24.dp))
        ) {
            MapView(
                modifier = Modifier
                    .fillMaxSize(),
                32.997690495963475,
                -96.76083616157189
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            0f to MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                            0.75f to Color.Transparent
                        )
                    )
            )
            Text(
                "Central Park",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
