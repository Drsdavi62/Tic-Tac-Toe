package com.example.tic_tac_toe.ui.presentation

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.tic_tac_toe.models.Move
import com.example.tic_tac_toe.models.Player
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//class OfflineGameViewModel(application: Application) : BaseViewModel(application) {
//
//    init {
//        resetBoard()
//    }
//
//    override fun processMove(position: Int) {
//        if (isSquareOccupied(position)) return
//        moves[position] = Move(Player.USER, position)
//
//        if (checkEnd(Player.USER)) return
//
//        isTurn.value = false
//
//        viewModelScope.launch {
//            delay(500)
//            val computerPosition = determineComputerMovePosition()
//            moves[computerPosition] = Move(Player.OPPONENT, computerPosition)
//
//            if (checkEnd(Player.OPPONENT)) return@launch
//
//            isTurn.value = true
//        }
//    }
//
//    private fun determineComputerMovePosition(): Int {
//        //win
//        val computerPositions =
//            moves.filter { it?.player == Player.OPPONENT }.mapNotNull { it?.boardIndex!! }
//        for (pattern in winPatterns) {
//            val filteredPattern = pattern.filter { !computerPositions.contains(it) }
//            if (filteredPattern.size == 1 && !isSquareOccupied(filteredPattern[0])) {
//                return filteredPattern[0]
//            }
//        }
//
//        //block
//        val playerPositions =
//            moves.filter { it?.player == Player.USER }.mapNotNull { it?.boardIndex!! }
//        for (pattern in winPatterns) {
//            val filteredPattern = pattern.filter { !playerPositions.contains(it) }
//            if (filteredPattern.size == 1 && !isSquareOccupied(filteredPattern[0])) {
//                return filteredPattern[0]
//            }
//        }
//
//        //middle square
//        if (!isSquareOccupied(4)) return 4
//
//        //random
//        val usedPositions = moves.mapNotNull { it?.boardIndex }
//        val nine = (0 until 9).toMutableList()
//        return nine.filter { !usedPositions.contains(it) }.random()
//    }
//
//    override fun resetGame() {
//        gameEnded.value = null
//        resetBoard()
//        isTurn.value = true
//        if (playerStarted) {
//            viewModelScope.launch {
//                delay(300)
//                val computerPosition = (0 until 9).random()
//                moves[computerPosition] = Move(Player.OPPONENT, computerPosition)
//            }
//        }
//        playerStarted = !playerStarted
//    }
//
//    override fun toggleMode() {
//        resetGame()
//    }
//
//}