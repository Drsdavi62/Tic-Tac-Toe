package com.example.tic_tac_toe

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.tic_tac_toe.ui.theme.TicTacToeTheme
import java.time.chrono.MinguoDate.of
import java.util.EnumSet.of

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
                        .background(color = Color.Red)
                ) {

                    val cardSize = (maxWidth - 20.dp) / 3

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        if (gameEnded != null) {
                            EndGameAlertDialog(resetGame = viewModel::resetGame, endGame = gameEnded!!)
                        }

                        LazyVerticalGrid(
                            cells = GridCells.Fixed(3),
                            contentPadding = PaddingValues(5.dp)
                        ) {
                            items(9) { i ->
                                Box() {
                                    Card(
                                        shape = MaterialTheme.shapes.small,
                                        modifier = Modifier
                                            .size(cardSize)
                                            .padding(5.dp),
                                        contentColor = Color.White,
                                        onClick = {
                                            if (!touchAvailable) return@Card
                                            viewModel.processMove(i)
                                        }
                                    ) {}
                                    moves[i]?.indicator?.let { icon ->
                                        Icon(
                                            icon,
                                            contentDescription = "X",
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .size(cardSize / 2),
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
