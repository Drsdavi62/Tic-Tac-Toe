package com.example.tic_tac_toe.ui.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.tic_tac_toe.R
import com.example.tic_tac_toe.models.EndGame
import com.example.tic_tac_toe.ui.components.EndGameAlertDialog
import com.example.tic_tac_toe.ui.components.GameSquare
import com.example.tic_tac_toe.ui.components.StatisticsCard
import com.example.tic_tac_toe.ui.components.WinCanvas
import com.example.tic_tac_toe.ui.theme.TicTacToeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TicTacToe_NoActionBar)

        setContent {
            TicTacToeTheme {

                val moves = viewModel.moves
                val gameEnded = viewModel.gameEnded.value
                val touchAvailable = viewModel.touchAvailable.value
                val showDialog = remember { mutableStateOf(false) }

                val statistics = viewModel.statistics.value

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colors.primary)
                ) {

                    val maxWidth = maxWidth
                    val cardSize = (maxWidth) / 3

                    val drawLine = remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        gameEnded?.let {
                            EndGameAlertDialog(
                                show = showDialog.value,
                                resetGame = {
                                    viewModel.resetGame()
                                    showDialog.value = false
                                    drawLine.value = false
                                },
                                endGame = it
                            )
                        }

                        StatisticsCard(statistics = statistics)

                        Spacer(modifier = Modifier.padding(16.dp))

                        Box {
                            LazyVerticalGrid(
                                cells = GridCells.Fixed(3),

                                ) {
                                items(9) { i ->
                                    GameSquare(
                                        cardSize = cardSize,
                                        touchAvailable = touchAvailable,
                                        processMove = viewModel::processMove,
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
                                    lifecycleScope.launch {
                                        delay(350)
                                        showDialog.value = true
                                    }
                                } else {
                                    showDialog.value = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
