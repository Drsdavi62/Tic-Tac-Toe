package com.example.tic_tac_toe.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.LifecycleCoroutineScope
import com.example.tic_tac_toe.models.EndGame
import com.example.tic_tac_toe.models.Move
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun GameBox(
    cardSize: Dp,
    touchAvailable: Boolean,
    processMove: (Int) -> Unit,
    moves: SnapshotStateList<Move?>,
    gameEnded: EndGame?,
    lifecycleCoroutineScope: LifecycleCoroutineScope,
    drawLine: MutableState<Boolean>,
    onShowDialog: () -> Unit,
) {
    Box {
        LazyVerticalGrid(
            cells = GridCells.Fixed(3),

            ) {
            items(9) { i ->
                GameSquare(
                    cardSize = cardSize,
                    touchAvailable = touchAvailable,
                    processMove = processMove,
                    boardIndex = i,
                    indicator = moves[i]?.indicator
                )
            }
        }

        gameEnded?.let {
            if (it is EndGame.Win) {
                WinCanvas(
                    draw = drawLine.value,
                    cardSize = cardSize,
                    winPatternPosition = it.winPatternPosition
                )
                drawLine.value = true
                lifecycleCoroutineScope.launch {
                    delay(350)
                    onShowDialog()
                }
            } else {
                onShowDialog()
            }
        }
    }
}