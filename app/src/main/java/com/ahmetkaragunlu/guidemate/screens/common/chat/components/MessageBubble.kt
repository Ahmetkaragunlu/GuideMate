package com.ahmetkaragunlu.guidemate.screens.common.chat.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.ahmetkaragunlu.guidemate.R
import com.ahmetkaragunlu.guidemate.screens.common.chat.model.MessageUiModel


@Composable
fun MessageBubble(message: MessageUiModel) {
    val isMe = message.isFromMe
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(horizontalAlignment = if (isMe) Alignment.End else Alignment.Start) {
            Box(
                modifier = Modifier
                    .widthIn(max = 300.dp)
                    .background(
                        color = if (isMe) colorResource(R.color.brand_color) else Color(0xFFF2F2F2),
                        shape = RoundedCornerShape(
                            topStart = 16.dp, topEnd = 16.dp,
                            bottomStart = if (isMe) 16.dp else 0.dp,
                            bottomEnd = if (isMe) 0.dp else 16.dp
                        )
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = message.text,
                    color = if (isMe) Color.White else Color.Black,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = message.time,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp)
            )
        }
    }
}
