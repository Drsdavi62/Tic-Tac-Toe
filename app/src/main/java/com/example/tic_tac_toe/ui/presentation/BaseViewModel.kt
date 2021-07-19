package com.example.tic_tac_toe.ui.presentation

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.tic_tac_toe.business.GameEventListener
import com.example.tic_tac_toe.business.GameHelper
import com.example.tic_tac_toe.models.EndGame
import com.example.tic_tac_toe.models.Move
import com.example.tic_tac_toe.models.Player
import com.example.tic_tac_toe.models.Statistics
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BaseViewModel(application: Application) : AndroidViewModel(application), GameEventListener {
    private val prefs: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(getApplication())

    var moves = mutableStateListOf<Move?>(null, null, null, null, null, null, null, null, null)
    val gameEnded = mutableStateOf<EndGame?>(null)
    val waitingForOpponent = mutableStateOf(false)

    var playerStarted = true
    val isTurn = mutableStateOf(true)

    val statistics = mutableStateOf(getInitialStatistics())

    var gameHelper = GameHelper(this, moves, viewModelScope)
        set(value) {
            field = value
            resetGame()
        }

    private val winPatterns = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
        listOf(0, 4, 8), listOf(2, 4, 6)
    )

    fun processMove(position: Int) {
        moves[position] = Move(Player.USER, position)
        gameHelper.onPlayerMove(moves)
        checkEnd(Player.USER)
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

    private fun endGame(endGame: EndGame) {
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

    open fun onResetGame() {
        resetGame()
    }

    fun resetGame() {
        gameEnded.value = null
        resetBoard()
        isTurn.value = true
        if (playerStarted) {
            viewModelScope.launch {
                delay(300)
                val computerPosition = (0 until 9).random()
                moves[computerPosition] = Move(Player.OPPONENT, computerPosition)
            }
        }
        playerStarted = !playerStarted
    }

    protected fun resetBoard() {
        for (i in 0 until moves.size) {
            moves[i] = null
        }
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

    override fun onOpponentMove(position: Int) {
        moves[position] = Move(Player.OPPONENT, position)
        checkEnd(Player.OPPONENT)
    }
}