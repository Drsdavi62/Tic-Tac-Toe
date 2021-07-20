package com.example.tic_tac_toe.business

import com.example.tic_tac_toe.BuildConfig
import com.example.tic_tac_toe.models.Move
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope

class OnlineGameHelper(
    gameEventListener: GameEventListener,
    moves: List<Move?>,
    coroutineScope: CoroutineScope,
) : GameHelper(gameEventListener, moves, coroutineScope) {

    private val socket: Socket? = IO.socket(BuildConfig.SOCKET_URL)

    init {
        opponentReset = false
        setupSocket()
    }

    override fun onPlayerMove(position: Int, moves: List<Move?>, winMove: Boolean) {
        super.onPlayerMove(position, moves, winMove)
        socket?.emit("move", position)
    }

    private fun setupSocket() {
        val onUpdateMove = Emitter.Listener {
            val movePosition = it[0] as Int
            if (isSquareOccupied(movePosition)) return@Listener
            gameEventListener.onOpponentMove(movePosition)
        }
        val onUpdateTurn = Emitter.Listener {
            val playersTurn = it[0] as Boolean
            gameEventListener.onUpdateTurn(playersTurn)
        }
        val onPlayerAmount = Emitter.Listener {
            val players = it[0] as Int
            gameEventListener.onPlayerAmount(players)
        }
        val onReset = Emitter.Listener {
            opponentReset = true
            if (userReset && opponentReset) {
                gameEventListener.onReset()
            }
        }
        val onOpponentDisconnected = Emitter.Listener {
            socket?.emit("opponentDisconnected")
        }

        socket?.let {
            with(it) {
                on("updateMoves", onUpdateMove)
                on("updateTurn", onUpdateTurn)
                on("playersAmount", onPlayerAmount)
                on("resetGame", onReset)
                on("opponentDisconnected", onOpponentDisconnected)
                connect()
            }
        }
    }

    override fun resetForUser() {
        super.resetForUser()
        socket?.emit("resetGame")
    }

    override fun resetGame() {
        super.resetGame()
        opponentReset = false
    }

    fun disconnect() {
        socket?.disconnect()
    }

}