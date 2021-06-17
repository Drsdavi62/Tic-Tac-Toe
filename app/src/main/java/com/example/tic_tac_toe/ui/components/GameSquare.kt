package com.example.tic_tac_toe.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.tic_tac_toe.Move

@ExperimentalMaterialApi
@Composable
fun GameSquare (
    cardSize: Dp,
    touchAvailable: Boolean,
    processMove: (Int) -> Unit,
    boardIndex: Int,
    indicator: ImageVector?
) {
    Box() {
        Card(
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .size(cardSize)
                .padding(5.dp),
            contentColor = Color.White,
            onClick = {
                if (!touchAvailable) return@Card
                processMove(boardIndex)
            },
            border = BorderStroke(1.dp, color = MaterialTheme.colors.onSurface)
        ) {}
        indicator?.let { icon ->
            Icon(
                icon,
                contentDescription = "Move indicator",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(cardSize / 2),
                tint = MaterialTheme.colors.onSurface
            )
        }
    }
}