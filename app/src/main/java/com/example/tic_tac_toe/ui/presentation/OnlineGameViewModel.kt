package com.example.tic_tac_toe.ui.presentation

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.tic_tac_toe.BuildConfig
import com.example.tic_tac_toe.models.EndGame
import com.example.tic_tac_toe.models.Move
import com.example.tic_tac_toe.models.Player
import com.example.tic_tac_toe.models.Statistics
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//class OnlineGameViewModel(application: Application) : BaseViewModel(application) {
//
//    private val socket: Socket = IO.socket(BuildConfig.SOCKET_URL)
//
//    private var userReset = false
//    private var opponentReset = false
//
//    fun setupSocket() {
//        val onUpdateMove = Emitter.Listener {
//            val movePosition = it[0] as Int
//            if (isSquareOccupied(movePosition)) return@Listener
//            moves[movePosition] = Move(Player.OPPONENT, movePosition)
//            if (checkEnd(Player.OPPONENT)) return@Listener
//            isTurn.value = true
//        }
//        val onUpdateTurn = Emitter.Listener {
//            val playersTurn = it[0] as Boolean
//            isTurn.value = playersTurn
//            playerStarted = playersTurn
//        }
//        val onPlayerAmount = Emitter.Listener {
//            val players = it[0] as Int
//            waitingForOpponent.value = players == 1
//            isTurn.value = true
//            resetBoard()
//        }
//        val onReset = Emitter.Listener {
//            opponentReset = true
//            resetGame()
//        }
//        val onOpponentDisconnected = Emitter.Listener {
//            socket.emit("opponentDisconnected")
//        }
//        with(socket) {
//            on("updateMoves", onUpdateMove)
//            on("updateTurn", onUpdateTurn)
//            on("playersAmount", onPlayerAmount)
//            on("resetGame", onReset)
//            on("opponentDisconnected", onOpponentDisconnected)
//            connect()
//        }
//    }
//
//    override fun processMove(position: Int) {
//        if (isSquareOccupied(position)) return
//        moves[position] = Move(Player.USER, position)
//
//        socket.emit("move", position)
//
//        if (checkEnd(Player.USER)) return
//
//        isTurn.value = false
//    }
//
//    override fun onResetGame() {
//        userReset = true
//        gameEnded.value = null
//        isTurn.value = false
//        socket.emit("resetGame")
//        resetGame()
//    }
//
//    override fun resetGame() {
//        if (!userReset || !opponentReset) return
//        userReset = false
//        opponentReset = false
//        gameEnded.value = null
//        resetBoard()
//        isTurn.value = !playerStarted
//        playerStarted = !playerStarted
//    }
//
//    override fun toggleMode() {
//        super.toggleMode()
//        socket.disconnect()
//    }
//}