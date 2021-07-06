package com.example.tic_tac_toe.ui.presentation

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.tic_tac_toe.models.EndGame
import com.example.tic_tac_toe.models.Move
import com.example.tic_tac_toe.models.Player
import com.example.tic_tac_toe.models.Statistics
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(getApplication())

    var moves = mutableStateListOf<Move?>(null, null, null, null, null, null, null, null, null)
    val gameEnded = mutableStateOf<EndGame?>(null)
    val waitingForOpponent = mutableStateOf(false)

    var playerStarted = true
    val touchAvailable = mutableStateOf(true)

    val statistics = mutableStateOf(getInitialStatistics())

    private val winPatterns = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
        listOf(0, 4, 8), listOf(2, 4, 6)
    )

    private val socket: Socket = IO.socket("http://192.168.1.101:5000/")

    init {
        setupSocket()
    }

    private fun setupSocket() {
        val onUpdateMove = Emitter.Listener {
            val movePosition = it[0] as Int
            if (isSquareOccupied(movePosition)) return@Listener
            moves[movePosition] = Move(Player.OPPONENT, movePosition)
            if (checkEnd(Player.OPPONENT)) return@Listener
            touchAvailable.value = true
        }
        val onUpdateTurn = Emitter.Listener {
            val playersTurn = it[0] as Boolean
            touchAvailable.value = playersTurn
            playerStarted = playersTurn
        }
        val onPlayerAmount = Emitter.Listener {
            val players = it[0] as Int
            waitingForOpponent.value = players == 1
        }
        socket.on("updateMoves", onUpdateMove)
        socket.on("updateTurn", onUpdateTurn)
        socket.on("playersAmount", onPlayerAmount)
        socket.connect()
    }

    fun processMove(position: Int) {
        if (isSquareOccupied(position)) return
        moves[position] = Move(Player.USER, position)

        viewModelScope.launch {
            delay(300)
            socket.emit("move", position)
        }

        if (checkEnd(Player.USER)) return

        touchAvailable.value = false
    }

    private fun getInitialStatistics(): Statistics {
        return Statistics(
            prefs.getInt(Statistics.WINS_KEY, 0),
            prefs.getInt(Statistics.LOSSES_KEY, 0),
            prefs.getInt(Statistics.DRAWS_KEY, 0),
        )
    }

    private fun checkEnd(player: Player): Boolean {
        val checkWin = checkWin(player)
        if (checkWin != -1) {
            endGame(EndGame.Win(player, checkWin))
            return true
        }
        if (checkDraw()) {
            endGame(EndGame.Draw)
            return true
        }
        return false
    }

    fun endGame(endGame: EndGame) {
        gameEnded.value = endGame
        when (endGame) {
            is EndGame.Win -> {
                if (endGame.player == Player.USER) {
                    statistics.value.wins += 1
                } else {
                    statistics.value.losses += 1
                }
            }
            EndGame.Draw -> {
                statistics.value.draws += 1
            }
        }
        writeToPrefs()
    }

    fun resetGame() {
        gameEnded.value = null
        moves = mutableStateListOf(null, null, null, null, null, null, null, null, null)
        touchAvailable.value = !playerStarted
        playerStarted = !playerStarted
    }

    private fun isSquareOccupied(position: Int): Boolean {
        return moves.any { it?.boardIndex == position }
    }

    private fun checkWin(player: Player): Int {
        val playerPositions =
            moves.filter { it != null && it.player == player }.map { it?.boardIndex!! }

        for ((index, pattern) in winPatterns.withIndex()) {
            if (pattern.none { !playerPositions.contains(it) }) {
                return index
            }
        }

        return -1
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