package com.example.tic_tac_toe.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.example.tic_tac_toe.ui.theme.Purple200

@Composable
fun WinCanvas(
    draw: Boolean,
    cardSize: Dp,
    winPatternPosition: Int
) {

    val cardSizeValue = with(LocalDensity.current) { cardSize.toPx() }

    val max = cardSizeValue * 3

    var startX = 0f
    var endX = 0f

    var startY = 0f
    var endY = 0f

    var generalOffset = 0f

    if (winPatternPosition == 0 || winPatternPosition == 3) { //height
        generalOffset = cardSizeValue / 2
    } else if (winPatternPosition == 1 || winPatternPosition == 4) {
        generalOffset = max / 2
    } else if (winPatternPosition == 2 || winPatternPosition == 5) {
        generalOffset = max - (cardSizeValue / 2)
    }

    when {
        winPatternPosition < 3 -> { //horizontal
            startY = generalOffset
            endY = generalOffset
            endX = max
        }
        winPatternPosition in 3..5 -> { //vertical
            startX = generalOffset
            endX = generalOffset
            endY = max
        }
        winPatternPosition == 6 -> {
            endX = max
            endY = max
        }
        winPatternPosition == 7 -> {
            startX = max
            endY = max
        }
    }

    val lineColor = MaterialTheme.colors.primaryVariant

    val animationSpec = tween<Float>(durationMillis = 200, easing = LinearEasing)

    val animationStateX = animateFloatAsState(
        targetValue = if (draw) endX else startX,
        animationSpec = animationSpec
    )

    val animationStateY = animateFloatAsState(
        targetValue = if (draw) endY else startY,
        animationSpec = animationSpec
    )

    Canvas(modifier = Modifier.size(cardSize * 3)) {
        drawLine(
            start = Offset(x = startX, y = startY),
            end = Offset(x = animationStateX.value, y = animationStateY.value),
            color = lineColor,
            strokeWidth = 20f
        )
    }
}