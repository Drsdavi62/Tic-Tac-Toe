package com.example.tic_tac_toe.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.tic_tac_toe.R
import com.example.tic_tac_toe.models.EndGame
import com.example.tic_tac_toe.models.Player

@Composable
fun EndGameAlertDialog(
    show: Boolean,
    resetGame: () -> Unit,
    endGame: EndGame
) {
    if (!show) return
    var title = ""
    var message = ""
    var buttonTitle = ""

    when (endGame) {
        is EndGame.Win -> {
            if (endGame.player == Player.HUMAN) {
                title = stringResource(R.string.end_dialog_win_title)
                message = stringResource(R.string.end_dialog_win_message)
                buttonTitle = stringResource(R.string.end_dialog_win_button)
            } else {
                title = stringResource(R.string.end_dialog_loss_title)
                message = stringResource(R.string.end_dialog_loss_message)
                buttonTitle = stringResource(R.string.end_dialog_loss_button)
            }
        }
        EndGame.Draw -> {
            title = stringResource(R.string.end_dialog_draw_title)
            message = stringResource(R.string.end_dialog_draw_message)
            buttonTitle = stringResource(R.string.end_dialog_draw_button)
        }
    }

    AlertDialog(
        onDismissRequest = { resetGame() },
        title = { Text(text = title) },
        text = { Text(
            text = message
        ) },

        confirmButton = {
            Button(onClick = {
               resetGame()
            }) {
                Text(text = buttonTitle)
            }
        },
    )
}