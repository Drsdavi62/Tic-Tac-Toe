package com.example.tic_tac_toe.business

import com.example.tic_tac_toe.models.Move
import com.example.tic_tac_toe.models.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameHelper(
    private val gameEventListener: GameEventListener,
    var moves: List<Move?>,
    val coroutineScope: CoroutineScope
) {

    private val winPatterns = listOf(
        listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8),
        listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8),
        listOf(0, 4, 8), listOf(2, 4, 6)
    )

    fun onPlayerMove(moves: List<Move?>) {
        this.moves = moves
        coroutineScope.launch {
            delay(500)
            gameEventListener.onOpponentMove(determineComputerMovePosition())
        }
    }

    private fun determineComputerMovePosition(): Int {
        //win
        val computerPositions =
            moves.filter { it?.player == Player.OPPONENT }.mapNotNull { it?.boardIndex!! }
        for (pattern in winPatterns) {
            val filteredPattern = pattern.filter { !computerPositions.contains(it) }
            if (filteredPattern.size == 1 && !isSquareOccupied(filteredPattern[0])) {
                return filteredPattern[0]
            }
        }

        //block
        val playerPositions =
            moves.filter { it?.player == Player.USER }.mapNotNull { it?.boardIndex!! }
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

    private fun isSquareOccupied(position: Int): Boolean {
        return moves.any { it?.boardIndex == position }
    }
}

interface GameEventListener {
    fun onOpponentMove(position: Int)
}