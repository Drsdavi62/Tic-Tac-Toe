package com.example.tic_tac_toe.business

import com.example.tic_tac_toe.models.Move
import com.example.tic_tac_toe.models.Player
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OfflineGameHelper(gameEventListener: GameEventListener,
                        moves: List<Move?>, coroutineScope: CoroutineScope
) : GameHelper(gameEventListener, moves, coroutineScope) {

    override fun onPlayerMove(position: Int, moves: List<Move?>, winMove: Boolean) {
        super.onPlayerMove(position, moves, winMove)
        if (winMove) return
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
}