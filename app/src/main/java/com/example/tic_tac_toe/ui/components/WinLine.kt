package com.example.tic_tac_toe.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WinLine(
    cardSize: Dp,
    show: Boolean,
    winPatternPosition: Int
) {
    val max = cardSize * 3

    var width: Dp = 0.dp
    var height: Dp = 0.dp
    var offsetX: Dp = 0.dp
    var offsetY: Dp = 0.dp

    var generalOffset: Dp = 0.dp

    if (winPatternPosition == 0 || winPatternPosition == 3) {
        generalOffset = cardSize / 2
    } else if (winPatternPosition == 1 || winPatternPosition == 4) {
        generalOffset = max / 2
    } else if (winPatternPosition == 2 || winPatternPosition == 5) {
        generalOffset = max - (cardSize / 2)
    }

    if (winPatternPosition < 3) { //horizontal
        width = if (!show) 0.dp else max
        height = 5.dp
        offsetY = generalOffset
    } else if (winPatternPosition in 3..5) { //vertical
        width = 5.dp
        height = if (!show) 0.dp else max
        offsetX = generalOffset
    }

    Divider(
        color = MaterialTheme.colors.primaryVariant, modifier = Modifier
            .width(width)
            .height(height)
            .offset(x = offsetX, y = offsetY)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 200,
                    easing = LinearEasing
                )
            )
    )
}