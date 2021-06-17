package com.example.tic_tac_toe.ui.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.example.tic_tac_toe.EndGame

@Composable
fun EndGameAlertDialog(
    resetGame: () -> Unit,
    endGame: EndGame
) {
    var title = ""
    var message = ""
    var buttonTitle = ""

    when (endGame) {
        EndGame.HUMAN_WIN -> {
            title = "You Win!"
            message = "You are so smart. You beat your own AI"
            buttonTitle = "Hell yeah!"
        }
        EndGame.COMPUTER_WIN -> {
            title = "You lost!"
            message = "You programmed a super AI"
            buttonTitle = "Rematch!"
        }
        EndGame.DRAW -> {
            title = "Draw!"
            message = "What a battle of wits we have here..."
            buttonTitle = "Try again"
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