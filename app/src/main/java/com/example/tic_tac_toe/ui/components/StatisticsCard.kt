package com.example.tic_tac_toe.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tic_tac_toe.Statistics

@Composable
fun StatisticsCard(
    statistics: Statistics
) {
    Card(
        shape = RoundedCornerShape(20),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        contentColor = MaterialTheme.colors.surface,
        border = BorderStroke(2.dp, MaterialTheme.colors.onSurface)
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            StatisticsText(text = "Matches Played: ${statistics.matches}")
            StatisticsText(text = "Wins: ${statistics.wins}")
            StatisticsText(text = "Losses: ${statistics.losses}")
            StatisticsText(text = "Draws: ${statistics.draws}")
        }
    }
}

@Composable
private fun StatisticsText(text: String) {
    Text(text = text, style = MaterialTheme.typography.h6, color = MaterialTheme.colors.onSurface,)
}