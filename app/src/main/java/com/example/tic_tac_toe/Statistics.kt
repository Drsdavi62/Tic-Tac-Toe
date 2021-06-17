package com.example.tic_tac_toe

data class Statistics (
    var matches: Int,
    var wins: Int,
    var losses: Int,
    var draws: Int
) {
    companion object {
        const val MATCHES_KEY = "matches"
        const val WINS_KEY = "wins"
        const val LOSSES_KEY = "losses"
        const val DRAWS_KEY = "draws"
    }

}