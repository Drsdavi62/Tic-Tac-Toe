package com.example.tic_tac_toe.models

sealed class EndGame {
    data class Win(val player: Player, val winPatternPosition: Int): EndGame()
    object Draw: EndGame()
}