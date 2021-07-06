package com.example.tic_tac_toe.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.ui.graphics.vector.ImageVector

class Move(val player: Player, val boardIndex: Int) {
    val indicator: ImageVector
        get() = if (player == Player.HUMAN) Icons.Filled.Close else Icons.Outlined.Circle
}