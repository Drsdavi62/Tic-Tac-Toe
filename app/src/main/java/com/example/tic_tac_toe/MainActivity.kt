package com.example.tic_tac_toe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tic_tac_toe.ui.components.EndGameAlertDialog
import com.example.tic_tac_toe.ui.components.GameSquare
import com.example.tic_tac_toe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            TicTacToeTheme {

                val moves = viewModel.moves
                val gameEnded = viewModel.gameEnded.value
                val touchAvailable = viewModel.touchAvailable.value

                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colors.primary)
                ) {

                    val cardSize = (maxWidth - 20.dp) / 3

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        gameEnded?.let {
                            EndGameAlertDialog(resetGame = viewModel::resetGame, endGame = it)
                        }

                        LazyVerticalGrid(
                            cells = GridCells.Fixed(3),
                            contentPadding = PaddingValues(5.dp)
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
                    }
                }
            }
        }
    }
}
