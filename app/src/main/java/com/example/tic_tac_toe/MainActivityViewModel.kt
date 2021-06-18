package com.example.tic_tac_toe

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(getApplication())

    var moves = mutableStateListOf<Move?>(null, null, null, null, null, null, null, null, null)
    val gameEnded = mutableStateOf<EndGame?>(null)

    private var playerStarts = true
    val touchAvailable = mutableStateOf(playerStarts)

    val statistics = mutableStateOf(getInitialStatistics())

    private val winPatterns = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
        listOf(0, 4, 8), listOf(2, 4, 6)
    )

    fun processMove(position: Int) {
        if (isSquareOccupied(position)) return
        moves[position] = Move(Player.HUMAN, position)

        if (checkEnd(Player.HUMAN)) return

        touchAvailable.value = false

        viewModelScope.launch {
            delay(500)
            val computerPosition = determineComputerMovePosition()
            moves[computerPosition] = Move(Player.COMPUTER, computerPosition)

            if (checkEnd(Player.COMPUTER)) return@launch

            touchAvailable.value = true
        }
    }

    private fun getInitialStatistics(): Statistics {
        return Statistics(
            prefs.getInt(Statistics.WINS_KEY, 0),
            prefs.getInt(Statistics.LOSSES_KEY, 0),
            prefs.getInt(Statistics.DRAWS_KEY, 0),
        )
    }

    private fun checkEnd(player: Player): Boolean {
        if (checkWin(player)) {
            endGame(if (player == Player.HUMAN) EndGame.HUMAN_WIN else EndGame.COMPUTER_WIN)
            return true
        }
        if (checkDraw()) {
            endGame(EndGame.DRAW)
            return true
        }
        return false
    }

    fun endGame(endGame: EndGame) {
        gameEnded.value = endGame
        when (endGame) {
            EndGame.HUMAN_WIN -> {
                statistics.value.wins += 1
            }
            EndGame.COMPUTER_WIN -> {
                statistics.value.losses += 1
            }
            EndGame.DRAW -> {
                statistics.value.draws += 1
            }
        }
        writeToPrefs()
    }

    fun resetGame() {
        gameEnded.value = null
        moves = mutableStateListOf(null, null, null, null, null, null, null, null, null)
        touchAvailable.value = true
        if (playerStarts) {
            viewModelScope.launch {
                delay(300)
                val computerPosition = (0 until 9).random()
                moves[computerPosition] = Move(Player.COMPUTER, computerPosition)
            }
        }
        playerStarts = !playerStarts
    }

    private fun isSquareOccupied(position: Int): Boolean {
        return moves.any { it?.boardIndex == position }
    }

    private fun determineComputerMovePosition(): Int {
        //win
        val computerPositions =
            moves.filter { it?.player == Player.COMPUTER }.mapNotNull { it?.boardIndex!! }
        for (pattern in winPatterns) {
            val filteredPattern = pattern.filter { !computerPositions.contains(it) }
            if (filteredPattern.size == 1 && !isSquareOccupied(filteredPattern[0])) {
                return filteredPattern[0]
            }
        }

        //block
        val playerPositions =
            moves.filter { it?.player == Player.HUMAN }.mapNotNull { it?.boardIndex!! }
        for (pattern in winPatterns) {
            val filteredPattern = pattern.filter { !playerPositions.contains(it) }
            if (filteredPattern.size == 1 && !isSquareOccupied(filteredPattern[0])) {
                return filteredPattern[0]
            }
        }

        //middle square
        if (!isSquareOccupied(4)) return 4

        //random
        val usedPositions = moves.mapNotNull { it?.boardIndex }
        val nine = (0 until 9).toMutableList()
        return nine.filter { !usedPositions.contains(it) }.random()
    }

    private fun checkWin(player: Player): Boolean {
        val playerPositions =
            moves.filter { it != null && it.player == player }.map { it?.boardIndex!! }

        for (pattern in winPatterns) {
            if (pattern.none { !playerPositions.contains(it) }) {
                return true
            }
        }

        return false
    }

    private fun checkDraw(): Boolean {
        if (moves.filterNotNull().size == 9) {
            return true
        }
        return false
    }

    private fun writeToPrefs() {
        val editor = prefs.edit()
        editor.putInt(Statistics.WINS_KEY, statistics.value.wins)
        editor.putInt(Statistics.LOSSES_KEY, statistics.value.losses)
        editor.putInt(Statistics.DRAWS_KEY, statistics.value.draws)
        editor.apply()
    }
}

class Move(val player: Player, val boardIndex: Int) {
    val indicator: ImageVector
        get() = if (player == Player.HUMAN) Icons.Filled.Close else Icons.Outlined.Circle
}

enum class Player {
    HUMAN, COMPUTER
}

enum class EndGame {
    HUMAN_WIN, COMPUTER_WIN, DRAW
}