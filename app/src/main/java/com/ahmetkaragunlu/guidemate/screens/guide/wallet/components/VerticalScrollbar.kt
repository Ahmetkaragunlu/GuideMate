package com.ahmetkaragunlu.guidemate.screens.guide.wallet.components


import androidx.compose.foundation.ScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.simpleVerticalScrollbar(state: ScrollState, width: Dp = 4.dp): Modifier =
    drawWithContent {
        drawContent()
        val totalHeight = state.maxValue + size.height
        if (state.maxValue > 0) {
            val thumbHeight = size.height * (size.height / totalHeight)
            val thumbY = (size.height - thumbHeight) * (state.value / state.maxValue.toFloat())

            drawRoundRect(
                color = Color(0xFFF0F0F0),
                topLeft = Offset(size.width - width.toPx(), 0f),
                size = Size(width.toPx(), size.height),
                cornerRadius = CornerRadius(width.toPx() / 2, width.toPx() / 2)
            )
            drawRoundRect(
                color = Color.LightGray,
                topLeft = Offset(size.width - width.toPx(), thumbY),
                size = Size(width.toPx(), thumbHeight),
                cornerRadius = CornerRadius(width.toPx() / 2, width.toPx() / 2)
            )
        }
    }