package com.example.tic_tac_toe.business

import com.example.tic_tac_toe.models.Move
import com.example.tic_tac_toe.models.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class GameHelper(
    protected val gameEventListener: GameEventListener,
    protected var moves: List<Move?>,
    protected val coroutineScope: CoroutineScope
) {
    protected val winPatterns = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
        listOf(0, 4, 8), listOf(2, 4, 6)
    )

    protected var userReset = false
    protected var opponentReset = true

    open fun onPlayerMove(position: Int, moves: List<Move?>) {
        this.moves = moves
    }

    protected fun isSquareOccupied(position: Int): Boolean {
        return moves.any { it?.boardIndex == position }
    }
}

interface GameEventListener {
    fun onOpponentMove(position: Int)
    fun onUpdateTurn(playersTurn: Boolean)
    fun onPlayerAmount(playersAmount: Int)
    fun onReset()
}