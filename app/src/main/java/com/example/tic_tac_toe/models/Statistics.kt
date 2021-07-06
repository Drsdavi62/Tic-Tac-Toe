package com.example.tic_tac_toe.models

data class Statistics (
    var wins: Int,
    var losses: Int,
    var draws: Int
) {
    val matches: Int
        get() = wins + losses + draws

    companion object {
        const val WINS_KEY = "wins"
        const val LOSSES_KEY = "losses"
        const val DRAWS_KEY = "draws"
    }

}