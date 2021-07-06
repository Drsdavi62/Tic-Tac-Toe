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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tic_tac_toe.R
import com.example.tic_tac_toe.models.Statistics

@Composable
fun StatisticsCard(
    statistics: Statistics
) {
    Card(
        shape = RoundedCornerShape(10),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        contentColor = MaterialTheme.colors.surface,
        border = BorderStroke(2.dp, MaterialTheme.colors.onSurface)
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)) {
            Text(
                text = stringResource(R.string.statistics_card_title),
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primaryVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            StatisticsText(text = stringResource(R.string.statistics_matches, statistics.matches))
            StatisticsText(text = stringResource(R.string.statistics_wins, statistics.wins))
            StatisticsText(text = stringResource(R.string.statistics_losses, statistics.losses))
            StatisticsText(text = stringResource(R.string.statistics_draws, statistics.draws))
        }
    }
}

@Composable
private fun StatisticsText(text: String) {
    Text(text = text, style = MaterialTheme.typography.h6, color = MaterialTheme.colors.onSurface,)
}