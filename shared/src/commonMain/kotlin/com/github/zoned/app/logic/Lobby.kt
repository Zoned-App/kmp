package com.github.zoned.app.logic

import com.github.zoned.app.Permissions
import com.github.zoned.app.data.GameStatus

class Lobby(private val gameCode: String) {
    init {
        Permissions.notifications.showNotification()
    }

    private var status: GameStatus = GameStatus.Lobby

    fun status(): GameStatus {
        return status
    }
}