package com.github.zoned.app.data

val games = listOf(
    Game("Central Park", "neeleshpoli", 40.78280644117304, -73.96557470937626),
    Game("Frisco Commons", "aarush49", 33.15567735480183, -96.8144192461819),
    Game("UTD", "MRBLACKLUFFY", 32.98802776982712, -96.75100654430815)
)
val quests = listOf(
    Quest("Stay in a hiding spot for 5 minutes", 0.31428f),
    Quest("Find 5 hiders", 0.6f),
)

data class LobbyDetails(
    val code: String,
    val host: String,
    val location: String,
    val players: List<String>
)

val testLobbyDetails = LobbyDetails(
    code = "XYZ123",
    host = "neeleshpoli",
    location = "Central Park",
    players = listOf("neeleshpoli", "aarush49", "MRBLACKLUFFY", "player4", "hider123")
)
