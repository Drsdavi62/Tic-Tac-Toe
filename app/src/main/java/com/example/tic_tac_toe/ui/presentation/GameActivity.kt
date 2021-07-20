package com.example.tic_tac_toe.ui.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.tic_tac_toe.R
import com.example.tic_tac_toe.models.GameMode
import com.example.tic_tac_toe.ui.components.*
import com.example.tic_tac_toe.ui.setFullScreen
import com.example.tic_tac_toe.ui.theme.TicTacToeTheme


class GameActivity : ComponentActivity() {

    private lateinit var viewModel: BaseViewModel
    private var gameMode: GameMode = GameMode.NOT_STARTED

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TicTacToe_NoActionBar)
        viewModel = ViewModelProvider(this@GameActivity).get(BaseViewModel::class.java)
        setContent { EmptyView() }

        showGameModeDialog()
    }

    @ExperimentalFoundationApi
    @ExperimentalMaterialApi
    private fun showGameModeDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.choose_game_mode))
            .setMessage(getString(R.string.online_computer_question))
            .setPositiveButton(getString(R.string.online)) { dialog, _ ->
                if (gameMode == GameMode.ONLINE) return@setPositiveButton
                gameMode = GameMode.ONLINE
                viewModel.setGameMode(gameMode)
                showGameScreen()
            }
            .setNegativeButton(getString(R.string.computer)) { dialog, _ ->
                if (gameMode == GameMode.OFFLINE) return@setNegativeButton
                gameMode = GameMode.OFFLINE
                viewModel.setGameMode(gameMode)
                showGameScreen()
            }
            .setCancelable(false)
            .create().show()
    }

    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    private fun showGameScreen() {
        setFullScreen()
        setContent {
            TicTacToeTheme {
                val moves = viewModel.moves
                val gameEnded = viewModel.gameEnded.value
                val isTurn = viewModel.isTurn.value
                val waitingOpponent = viewModel.waitingForOpponent.value
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

                    Column(modifier = Modifier.fillMaxSize()) {
                        SettingsRow { showGameModeDialog() }

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
                                        viewModel.onResetGame()
                                        showDialog.value = false
                                        drawLine.value = false
                                    },
                                    endGame = it
                                )
                            }

                            if (waitingOpponent) {
                                AlertDialog(
                                    onDismissRequest = { },
                                    title = { Text(text = getString(R.string.you_are_alone_message)) },
                                    text = {
                                        Text(
                                            text = getString(R.string.waiting_opponent_message)
                                        )
                                    },
                                    confirmButton = {}
                                )
                            }

                            StatisticsCard(statistics = statistics)

                            Spacer(modifier = Modifier.padding(8.dp))

                            GameBox(
                                cardSize = cardSize,
                                touchAvailable = isTurn,
                                processMove = viewModel::processMove,
                                moves = moves,
                                gameEnded = gameEnded,
                                lifecycleCoroutineScope = lifecycleScope,
                                drawLine = drawLine
                            ) {
                                showDialog.value = true
                            }

                            Spacer(modifier = Modifier.padding(8.dp))

                            Text(
                                text = if (isTurn) getString(R.string.your_turn_message) else getString(
                                    R.string.wait_opponent_message
                                ),
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}